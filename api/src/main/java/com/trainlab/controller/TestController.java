package com.trainlab.controller;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.dto.*;
import com.trainlab.model.Role;
import com.trainlab.model.testapi.Test;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@Tag(name = "TestController", description = "Test management methods")
public interface TestController {

    @Operation(
            summary = "Search test by id",
            description = "Receive test by id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded test",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TestDTO.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "User not found"
                    )
            }
    )
    ResponseEntity<TestDTO> getTest(@PathVariable Long id);

    @Operation(
            summary = "Find all tests",
            description = "Find all tests without limitations",
            responses =
            @ApiResponse(
                    responseCode = "OK",
                    description = "Successfully loaded tests",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TestDTO.class)))
            )
    )
    ResponseEntity<List<TestDTO>> getAllTests();

    @Operation(
            summary = "Find all tests by speciality",
            description = "Find all tests by speciality",
            responses =
            @ApiResponse(
                    responseCode = "OK",
                    description = "Successfully loaded tests",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TestDTO.class)))
            )
    )
    ResponseEntity<List<TestDTO>> getAllTestsBySpeciality(@PathVariable eSpecialty specialty );

    @Operation(
            summary = "Spring Data create test",
            description = "create Test based on given request body",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully created Test",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TestCreateDTO.class)))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    ResponseEntity<TestDTO> createTest(@Valid @RequestBody TestCreateDTO createTestDTO);

    @Operation(
            summary = "Add question",
            description = "Add question to already exited test",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "Question created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(allOf = {QuestionCreateDTO.class})
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    ),
                    @ApiResponse(
                            responseCode = "INTERNAL_SERVER_ERROR",
                            description = "Question generation error"
                    )
            }
    )
    ResponseEntity<QuestionDTO> addQuestion (@PathVariable Long testId ,@Valid @RequestBody QuestionCreateDTO questionDTO);

    @Operation(
            summary = "Add answer",
            description = "Add answer to already exited test",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "answer created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(allOf = {AnswerCreateDTO.class})
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    ),
                    @ApiResponse(
                            responseCode = "INTERNAL_SERVER_ERROR",
                            description = "Question generation error"
                    )
            }
    )
    ResponseEntity<AnswerDTO> addAnswer (@PathVariable Long testId, int questionNum, @Valid @RequestBody AnswerCreateDTO answerDTO);

    @Operation(
            summary = "Update question",
            description = "Update question using data",
            responses = {
                    @ApiResponse(
                            responseCode = "UPDATED",
                            description = "Question updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(allOf = {QuestionCreateDTO.class})
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    ),
                    @ApiResponse(
                            responseCode = "INTERNAL_SERVER_ERROR",
                            description = "Question update error"
                    )
            }
    )
    ResponseEntity<QuestionDTO> updateQuestion (@PathVariable Long testId, int questionNum, @RequestBody QuestionCreateDTO questionCreateDTO);

    @Operation(
            summary = "Update answer",
            description = "Update answer using data",
            responses = {
                    @ApiResponse(
                            responseCode = "UPDATED",
                            description = "Answer updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(allOf = {AnswerCreateDTO.class})
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    ),
                    @ApiResponse(
                            responseCode = "INTERNAL_SERVER_ERROR",
                            description = "Answer update error"
                    )
            }
    )
    ResponseEntity<AnswerDTO> updateAnswer (@PathVariable Long testId, int questionNum, int answerNum, @RequestBody AnswerCreateDTO answerCreateDTO);

    @Operation(
            summary = "Submit test",
            description = "Submit test and attach it to user",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "Submitted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(allOf = {SubmitDTO.class})
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    ),
                    @ApiResponse(
                            responseCode = "INTERNAL_SERVER_ERROR",
                            description = "Submition error"
                    )
            }
    )
    ResponseEntity<Integer> submitQuiz(@PathVariable Long testId, @RequestBody SubmitDTO submitDTO);

    @Operation(
            summary = "Delete test",
            description = "Delete test by id",
            responses = {
                    @ApiResponse(
                            responseCode = "DELETED",
                            description = "Test deleted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(allOf = {Test.class})
                            )
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    ResponseEntity<String> deleteTest(@PathVariable Long testId);

    @Operation(
            summary = "Spring Data Update test and refresh it on CACHE ",
            description = "Updating test based on given id and request body AND REFRESHING IT ON CACHE",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Test has successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TestCreateDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    ResponseEntity<TestDTO> updateAndRefreshTest(@PathVariable Long testId, TestCreateDTO testCreateDTO);

    @Operation(
            summary = "Updating test in cache ",
            description = "Updating test class in cache",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Cache updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TestDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    ResponseEntity<TestDTO> refreshTest(@PathVariable Long id);
}
