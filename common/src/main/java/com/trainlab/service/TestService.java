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
    AnswerDTO addAnswer (Long testId, int questionNum, AnswerCreateDTO answerDTO);

    QuestionDTO updateQuestion(Long testId, int questionNum, QuestionCreateDTO questionDTO);
    AnswerDTO updateAnswer(Long testId, int questionNum, int answerNum, AnswerCreateDTO answerDTO);

}
