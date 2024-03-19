package com.trainlab.controller;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.dto.*;
import com.trainlab.model.testapi.UserTestResult;
import com.trainlab.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tests")
@RequiredArgsConstructor
public class TestControllerImpl implements TestController {
    private final TestService testService;


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TestDTO> getTest(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.getTest(id));
    }

    @Override
    @GetMapping("/")
    public ResponseEntity<List<TestDTO>> getAllTests() {
      return   ResponseEntity.status(HttpStatus.OK).body(testService.getAll());
    }

    @Override
    @GetMapping("/category/{specialty}")
    public ResponseEntity<List<TestDTO>> getAllTestsBySpeciality(@PathVariable eSpecialty specialty) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.getAllBySpeciality(specialty));
    }

    @Override
    @PostMapping("/")
    public ResponseEntity<TestDTO> createTest(@Valid @RequestBody TestCreateDTO createTestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.create(createTestDTO));
    }

    @Override
    @PostMapping("/question/{testId}")
    public ResponseEntity<QuestionDTO> addQuestion(@PathVariable Long testId ,@Valid @RequestBody  QuestionCreateDTO questionDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.addQuestion(testId,questionDTO));
    }

    @Override
    @PostMapping("/{testId}/{questionNum}/")
    public ResponseEntity<AnswerDTO> addAnswer(@PathVariable Long testId, @PathVariable int questionNum, @Valid @RequestBody AnswerCreateDTO answerDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.addAnswer(testId,questionNum,answerDTO));
    }

    @Override
    @PutMapping("/{testId}/{questionNum}/")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long testId,@PathVariable  int questionNum, @RequestBody QuestionCreateDTO questionCreateDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.updateQuestion(testId,questionNum,questionCreateDTO));
    }

    @Override
    @PutMapping("/{testId}/{questionNum}/{answerNum}/")
    public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable Long testId, @PathVariable int questionNum,@PathVariable  int answerNum,@RequestBody AnswerCreateDTO answerCreateDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.updateAnswer(testId,questionNum,answerNum,answerCreateDTO));
    }

    @Override
    @PostMapping("/submit/{testId}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Long testId, @RequestBody SubmitDTO submitDTO) {
       UserTestResult userTestResult = testService.processResult(testId,submitDTO.getResults(),submitDTO.getTime(),submitDTO.getUserId());
        return ResponseEntity.ok(userTestResult.getScore());
    }

    @Override
    @DeleteMapping("/{testId}")
    public  ResponseEntity<String> deleteTest(@PathVariable Long testId){
        return ResponseEntity.ok(testService.deleteTest(testId));
    }

    @Override
    @PutMapping("/{testId}")
    public  ResponseEntity<TestDTO> updateAndRefreshTest(@PathVariable Long testId, TestCreateDTO testCreateDTO){
        return ResponseEntity.ok(testService.updateAndRefreshTest(testId,testCreateDTO));
    }

    @Override
    @PutMapping("/cache/{id}")
    public  ResponseEntity<TestDTO> refreshTest(@PathVariable Long id){
        return ResponseEntity.ok(testService.refreshTest(id));
    }

    @Override
    @DeleteMapping("/answer/{answerId}")
    public ResponseEntity<String> deleteAnswer(@PathVariable Long answerId){
        return ResponseEntity.ok(testService.deleteAnswer(answerId));
    }

    @Override
    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId){


        return ResponseEntity.ok(testService.deleteQuestion(questionId));
    }
}
