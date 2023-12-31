package com.trainlab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.common.aliasing.qual.Unique;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
@ToString
@EqualsAndHashCode
public class AuthenticationInfo {
    @Unique
    private String email;

    @JsonIgnore
    private String userPassword;
}
