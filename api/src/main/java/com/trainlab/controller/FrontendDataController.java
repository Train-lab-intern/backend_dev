package com.trainlab.controller;

import com.trainlab.model.FrontendData;
import com.trainlab.repository.FrontendDataRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.FrontendDataService;
import com.trainlab.service.SessionService;
import com.trainlab.util.RandomValuesGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/front")
@RequiredArgsConstructor
@Tag(name = "FrontendDataController", description = "Frontend data")
@Slf4j
public class FrontendDataController {

    private final FrontendDataService frontendDataService;
    @Operation(
            summary = "Get Main Page Data",
            description = "Get main page data by range",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded main page data",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FrontendData.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "No main page data found"
                    )
            }
    )
    @GetMapping("/pages/{page}")
    public ResponseEntity<Map<String, String>> getMainPageData(@PathVariable int page) {
        Map<String, String> mainPageDataMap = frontendDataService.getDataByPage(page);

        return new ResponseEntity<>(mainPageDataMap, HttpStatus.OK);
    }
}
