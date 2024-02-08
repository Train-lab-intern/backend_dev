package com.trainlab.dto;

import com.trainlab.model.Speciality;
import com.trainlab.model.UserLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "UserPage response")
public class UserPageDto {

    private Long id;

    private String generatedName;

    private String username;

    private String surname;

    private String userLevel;

    private String speciality;

    private List<RoleDto> roles;
}
