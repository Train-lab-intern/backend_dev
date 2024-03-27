package com.trainlab.service.impl;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.dto.*;
import com.trainlab.exception.ObjectNotFoundException;
import com.trainlab.mapper.TestMapper;
import com.trainlab.model.testapi.Answer;
import com.trainlab.model.testapi.Question;
import com.trainlab.model.testapi.Test;
import com.trainlab.model.testapi.UserTestResult;
import com.trainlab.repository.*;
import com.trainlab.service.TestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
@Service
@CacheConfig(cacheNames = {"tests"})
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private  final AnswerRepository answerRepository;
    private  final TestMapper testMapper;
    private  final UserRepository userRepository;
    private  final UserTestResultRepository userTestResultRepository;

    @Override
    @Cacheable(key = "#id")
    public TestDTO getTest(Long id) {
        log.info("got test from DB with id" + id);
        Test test = testRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Test not found"));
        return  testMapper.toDTO(test);
    }

    @Override
    public List<TestDTO> getAll() {
        List<Test> tests = testRepository.findAll();
        return  tests.stream().map(testMapper::toDTO).toList();
    }

    @Override
    public List<TestDTO> getAllBySpeciality(eSpecialty specialty) {
        List<Test> tests = testRepository.findAllBySpecialty(specialty);
        return  tests.stream().map(testMapper::toDTO).toList();
    }

    @Override
    public TestDTO create(TestCreateDTO createTestDTO) {
        Test test = testMapper.toEntity(createTestDTO);
        testRepository.saveAndFlush(test);
        return  testMapper.toDTO(test);
    }

    @Override
    @CacheEvict(key = "#id")
    public  String deleteTest(Long id){
        Test test = testRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Test not found"));
        testRepository.delete(test);
        return "Test was deleted";
    }

    @Override
    @CachePut(key = "#id")
    public TestDTO updateAndRefreshTest(Long id, TestCreateDTO update){
        Test test = testRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Test not found"));
        if(update.getTitle() != null)
            test.setTitle(update.getTitle());
        if(update.getDescription() != null)
            test.setDescription(update.getDescription());
        if(update.getSpecialty() != null)
            test.setSpecialty(update.getSpecialty());

        testRepository.saveAndFlush(test);
        return testMapper.toDTO(test);
    }



    @Override
    public QuestionDTO addQuestion(Long testId, QuestionCreateDTO questionDTO) {
        Test test = testRepository.findById(testId).orElseThrow(()-> new EntityNotFoundException("Test not found"));
        Question question = testMapper.toEntity(questionDTO);
        if(test.getQuestions().isEmpty()){
            question.setQuestionNum(1);
        }else{
            question.setQuestionNum(test.getQuestions().size()+1);
        }
        questionRepository.saveAndFlush(question);
        test.getQuestions().add(question);
        testRepository.saveAndFlush(test);
        return testMapper.toDTO(question);
    }

    @Override
    public AnswerDTO addAnswer(Long testId, int questionNum, AnswerCreateDTO answerDTO) {
        Test test = testRepository.findById(testId).orElseThrow(()-> new EntityNotFoundException("Test not found"));
        Question question = findQuestionByNum(testId,questionNum).orElseThrow(()-> new EntityNotFoundException("Question not found"));
        Answer answer = testMapper.toEntity(answerDTO);
        answer.setQuestion(question);
        if(answerDTO.isCorrect() == true){
            test.getRightAnswers().add(answer);
        }
        if(question.getAnswers().isEmpty()){
            answer.setAnswerNum(1);
        }else{
            answer.setAnswerNum(question.getAnswers().size()+1);
        }
        answerRepository.saveAndFlush(answer);
        question.getAnswers().add(answer);
        questionRepository.saveAndFlush(question);
        testRepository.saveAndFlush(test);
        return testMapper.toDTO(answer);
    }

    @Override
    public QuestionDTO updateQuestion(Long id, int questionNum, QuestionCreateDTO questionDTO) {
        Question question = findQuestionByNum(id,questionNum).orElseThrow(()-> new EntityNotFoundException("Question not found"));
        if(questionDTO.getQuestionTxt() != null){
            question.setQuestionTxt(questionDTO.getQuestionTxt());
        }
        questionRepository.saveAndFlush(question);
        return testMapper.toDTO(question);
    }

    @Override
    public AnswerDTO updateAnswer(Long testId, int questionNum, int answerNum, AnswerCreateDTO answerDTO) {
        Answer answer = findQuestionByNum(testId,questionNum).orElseThrow(()-> new EntityNotFoundException("Question not found")).getAnswers().get(answerNum);
        if(answerDTO.getAnswerTxt() != null){
            answer.setAnswerTxt(answerDTO.getAnswerTxt());
        }
        if(answer.isCorrect() != answerDTO.isCorrect()){
            answer.setCorrect(answerDTO.isCorrect());
        }
        answerRepository.saveAndFlush(answer);
        return testMapper.toDTO(answer);
    }

    private Optional<Question>  findQuestionByNum(Long testId, int questionNum){
        Test test = testRepository.findById(testId).orElseThrow();
        return Optional.ofNullable(test.getQuestions().get(questionNum - 1));
    }

    public  UserTestResult processResult(Long testId, Map<Long, Long> results, long time, long userId) {
        Test test = testRepository.findById(testId).orElseThrow();

        int correctAnswers = 0;
        for(Answer answer: test.getRightAnswers()){
            long questionId = answer.getQuestion().getId();
            long userAnswerId =answer.getId();

            if(results.get(questionId) != null && results.get(questionId) == userAnswerId )
                correctAnswers++;
        }

        UserTestResult userTestResult = UserTestResult.builder()
                //todo убрать заглушку
                .user(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User could not be found")))
                .test(testRepository.findById(testId).orElseThrow(() -> new EntityNotFoundException("no tests")))
                .score(correctAnswers)
                .completeTime(time)
                .build();

        userTestResultRepository.save(userTestResult);
        return userTestResult;
    }

    @Override
    @CachePut(key = "#id")
    public TestDTO refreshTest(Long id){ //refreshing cache after changing childs
        Test test = testRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Test not found"));
        testRepository.save(test);
        return testMapper.toDTO(test);
    }

    public  String deleteQuestion (Long id){
        questionRepository.deleteById(id);
        return "Question removed";
    }

    @Override
    public String deleteAnswer (Long id){
        Answer answer = answerRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Answer not found"));
        answerRepository.delete(answer);
        return "Answer removed";
    }
}