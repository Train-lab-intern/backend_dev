package com.trainlab.dto;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.model.testapi.Question;
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
@Schema(description = "Test info response")
public class TestDTO {
    private Long id;
    private String title;
    private String description;
    private List<QuestionDTO> questions;
    private eSpecialty specialty;

}
