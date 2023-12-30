package com.trainlab.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.trainlab.enums.Speciality;
import com.trainlab.enums.UserLevel;
import com.trainlab.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User Response")
public class UserDto {
    private Long id;

    private String username;

    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Minsk")
    private Timestamp created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Minsk")
    private Timestamp changed;

    private List<Role> roles;

    private String firstName;

    private  String secondName;

    private UserLevel userLevel;

    private Speciality speciality;
}
