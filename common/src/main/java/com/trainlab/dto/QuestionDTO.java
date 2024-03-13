package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
@Schema(description = "Question info response")
public class QuestionDTO {
    private Long id;
    String questionTxt;
    int questionNum;
    List<AnswerDTO> answers;
}
