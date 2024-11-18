package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
@Setter
@Schema(description = "Answer info response")
public class AnswerDTO {
    private Long id;
    private  String answerTxt;
    private int answerNum;
     private   boolean isCorrect;
}
