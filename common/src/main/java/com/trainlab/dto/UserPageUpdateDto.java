package com.trainlab.dto;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.Enum.eUserLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
@Schema(description = "UserPage update response")
public class UserPageUpdateDto {

    private String username;

    private String surname;

    private eUserLevel userLevel;

    private eSpecialty specialty;
}

