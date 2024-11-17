package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Submit dto", example = """
              {  "results": {
                "1": 1,
                "2": 1,
                "3": 2,
                "4":2,
                "5":1,
                "6":1,
                "7":2,
                "8":2,
                "9":1
                },
                "time": 800000,
                "userId": 1
              }
        """)
public class SubmitDTO {

    private Map<Long, Long> results;
    // мапа из Номера вопроса и номера ответа !! не ID а номера
    private long time;
    private long userId;
}
//{
//        "results": {
//        "1": 1,
//        "2": 1,
//        "3": 2,
//        "4":2,
//        "5":1,
//        "6":1,
//        "7":2,
//        "8":2,
//        "9":1
//        },
//        "time": 800000,
//        "userId": 1
//        }