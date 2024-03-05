package com.trainlab.service.impl;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.dto.*;
import com.trainlab.mapper.TestMapper;
import com.trainlab.model.testapi.Answer;
import com.trainlab.model.testapi.Question;
import com.trainlab.model.testapi.Test;
import com.trainlab.repository.AnswerRepository;
import com.trainlab.repository.QuestionRepository;
import com.trainlab.repository.TestRepository;
import com.trainlab.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private  final AnswerRepository answerRepository;
    private  final TestMapper testMapper;

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
        questionRepository.saveAndFlush(question);
        test.getQuestions().add(question);
        testRepository.saveAndFlush(test);
        return testMapper.toDTO(question);
    }

    @Override
    public AnswerDTO addAnswer(Long questionId, AnswerCreateDTO answerDTO) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        Answer answer = testMapper.toEntity(answerDTO);
        answerRepository.saveAndFlush(answer);
        question.getAnswers().add(answer);
        questionRepository.saveAndFlush(question);
        return testMapper.toDTO(answer);
    }

    @Override
    public QuestionDTO updateQuestion(Long questionId, QuestionCreateDTO questionDTO) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        if(questionDTO.getQuestionTxt() != null){
            question.setQuestionTxt(questionDTO.getQuestionTxt());
        }
        questionRepository.saveAndFlush(question);
        return testMapper.toDTO(question);
    }

    @Override
    public AnswerDTO updateAnswer(Long answerId, AnswerCreateDTO answerDTO) {
        Answer answer = answerRepository.findById(answerId).orElseThrow();
        if(answerDTO.getAnswerTxt() != null){
            answer.setAnswerTxt(answerDTO.getAnswerTxt());
        }
        if(answer.isCorrect() != answerDTO.isCorrect()){
            answer.setCorrect(answerDTO.isCorrect());
        }
        answerRepository.saveAndFlush(answer);
        return testMapper.toDTO(answer);
    }
}
