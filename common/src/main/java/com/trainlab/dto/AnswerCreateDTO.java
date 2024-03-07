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
@Schema(description = "Answer create")
public class AnswerCreateDTO {
    private  String answerTxt;

    private boolean isCorrect;
}
