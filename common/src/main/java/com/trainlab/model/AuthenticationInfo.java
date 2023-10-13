package com.trainlab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class AuthenticationInfo {
    @Unique
    private String email;

    @JsonIgnore
    private String userPassword;
}
