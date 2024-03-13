package com.trainlab.dto;

import com.trainlab.Enum.eSpecialty;
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
@Schema(description = "Test create")
public class TestCreateDTO {
    private String title;
    private String description;
    private eSpecialty specialty;
}
