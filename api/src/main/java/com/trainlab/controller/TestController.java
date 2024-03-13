package com.trainlab.controller;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@Tag(name = "TestController", description = "Test management methods")
public interface TestController {

    ResponseEntity<TestDTO> getTest(@PathVariable Long id);

    ResponseEntity<List<TestDTO>> getAllTests();

    ResponseEntity<List<TestDTO>> getAllTestsBySpeciality(@PathVariable eSpecialty specialty );

    ResponseEntity<TestDTO> createTest(@Valid @RequestBody TestCreateDTO createTestDTO);

    ResponseEntity<QuestionDTO> addQuestion (@PathVariable Long testId ,@Valid @RequestBody QuestionCreateDTO questionDTO);

    ResponseEntity<AnswerDTO> addAnswer (@PathVariable Long testId, int questionNum, @Valid @RequestBody AnswerCreateDTO answerDTO);

    ResponseEntity<QuestionDTO> updateQuestion (@PathVariable Long testId, int questionNum, @RequestBody QuestionCreateDTO questionCreateDTO);

    ResponseEntity<AnswerDTO> updateAnswer (@PathVariable Long testId, int questionNum, int answerNum, @RequestBody AnswerCreateDTO answerCreateDTO);
}
