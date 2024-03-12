package com.trainlab.service.impl;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.dto.*;
import com.trainlab.mapper.TestMapper;
import com.trainlab.model.testapi.Answer;
import com.trainlab.model.testapi.Question;
import com.trainlab.model.testapi.Test;
import com.trainlab.model.testapi.UserTestResult;
import com.trainlab.repository.*;
import com.trainlab.service.TestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private  final AnswerRepository answerRepository;
    private  final TestMapper testMapper;
    private  final UserRepository userRepository;
    private  final UserTestResultRepository userTestResultRepository;

    @Override
    public TestDTO getTest(Long id) {
        Test test = testRepository.findById(id).orElseThrow();
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
    public QuestionDTO addQuestion(Long testId, QuestionCreateDTO questionDTO) {
        Test test = testRepository.findById(testId).orElseThrow();
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
        Test test = testRepository.findById(testId).orElseThrow();
        Question question = findQuestionByNum(testId,questionNum);
        Answer answer = testMapper.toEntity(answerDTO);
        answer.setQuestion(question);
        if(answerDTO.isCorrect() == true){
            test.addRightAnswer(answer);
        }
        if(question.getAnswers().isEmpty()){
            answer.setAnswerNum(1);
        }else{
            answer.setAnswerNum(question.getAnswers().size()+1);
        }
        answerRepository.saveAndFlush(answer);
        question.getAnswers().add(answer);
        questionRepository.saveAndFlush(question);
        return testMapper.toDTO(answer);
    }

    @Override
    public QuestionDTO updateQuestion(Long testId, int questionNum, QuestionCreateDTO questionDTO) {
        Question question = findQuestionByNum(testId,questionNum);
        if(questionDTO.getQuestionTxt() != null){
            question.setQuestionTxt(questionDTO.getQuestionTxt());
        }
        questionRepository.saveAndFlush(question);
        return testMapper.toDTO(question);
    }

    @Override
    public AnswerDTO updateAnswer(Long testId, int questionNum, int answerNum, AnswerCreateDTO answerDTO) {
        Answer answer = findQuestionByNum(testId,questionNum).getAnswers().get(answerNum);
        if(answerDTO.getAnswerTxt() != null){
            answer.setAnswerTxt(answerDTO.getAnswerTxt());
        }
        if(answer.isCorrect() != answerDTO.isCorrect()){
            answer.setCorrect(answerDTO.isCorrect());
        }
        answerRepository.saveAndFlush(answer);
        return testMapper.toDTO(answer);
    }

    private  Question findQuestionByNum(Long testId, int questionNum){
        Test test = testRepository.findById(testId).orElseThrow();
        return test.getQuestions().get(questionNum-1);
    }

    public  UserTestResult processResult(Long testId, Map<Long, Long> results, long time) {
        Test test = testRepository.findById(testId).orElseThrow();

        int correctAnswers = 0;
        for(Answer answer: test.getRightAnswers()){
            long questionId = answer.getQuestion().getId();
            long userAnswerId =answer.getId();

            if(results.get(questionId) == userAnswerId)
                correctAnswers++;
        }


        UserTestResult userTestResult = UserTestResult.builder()
                //todo убрать заглушку
                .user(userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse("trainlab@gmail.com").orElseThrow(() -> new EntityNotFoundException("User could not be found")))
                .test(testRepository.findById(testId).orElseThrow(() -> new EntityNotFoundException("no tests")))
                .score(correctAnswers)
                .completeTime(time)
                .build();

        userTestResultRepository.save(userTestResult);
        return userTestResult;
    }
}
