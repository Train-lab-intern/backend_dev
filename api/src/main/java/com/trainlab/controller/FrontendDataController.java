package com.trainlab.controller;

import com.trainlab.model.FrontendData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Tag(name = "FrontendDataController", description = "Frontend data")
public interface FrontendDataController {
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
    ResponseEntity<Map<String, String>> getMainPageData(Integer page);
}
