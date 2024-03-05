package com.trainlab.service;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.dto.*;

import java.util.List;

public interface TestService {

    TestDTO getTest(Long id);
    List<TestDTO> getAll();

    List<TestDTO> getAllBySpeciality(eSpecialty specialty);

    TestDTO create(TestCreateDTO createTestDTO);

    QuestionDTO addQuestion (Long testId, QuestionCreateDTO questionDTO);
    AnswerDTO addAnswer (Long questionId, AnswerCreateDTO answerDTO);

    QuestionDTO updateQuestion(Long questionId, QuestionCreateDTO questionDTO);
    AnswerDTO updateAnswer(Long answerId, AnswerCreateDTO answerDTO);

}
