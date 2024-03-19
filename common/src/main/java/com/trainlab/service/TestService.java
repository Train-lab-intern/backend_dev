package com.trainlab.service;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.dto.*;
import com.trainlab.model.testapi.UserTestResult;

import java.util.List;
import java.util.Map;

public interface TestService {

    TestDTO getTest(Long id);
    List<TestDTO> getAll();

    List<TestDTO> getAllBySpeciality(eSpecialty specialty);

    TestDTO create(TestCreateDTO createTestDTO);

    QuestionDTO addQuestion (Long testId, QuestionCreateDTO questionDTO);
    AnswerDTO addAnswer (Long testId, int questionNum, AnswerCreateDTO answerDTO);

    QuestionDTO updateQuestion(Long testId, int questionNum, QuestionCreateDTO questionDTO);
    AnswerDTO updateAnswer(Long testId, int questionNum, int answerNum, AnswerCreateDTO answerDTO);

   UserTestResult processResult(Long testId, Map<Long, Long> results, long time, long userId);
    String deleteTest(Long id);
    TestDTO updateAndRefreshTest(Long id, TestCreateDTO update);
    TestDTO refreshTest(Long id);

    String deleteAnswer (Long id);
    String deleteQuestion (Long id);
}
