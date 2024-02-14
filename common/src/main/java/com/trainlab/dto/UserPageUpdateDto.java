package com.trainlab.dto;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.Enum.eUserLevel;
import com.trainlab.validation.ValidName;
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

    @ValidName(nullable = true)
    private String username;

    @ValidName(nullable = true)
    private String surname;

    private eUserLevel userLevel;

    private eSpecialty specialty;
}

