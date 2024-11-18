package com.trainlab.dto;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.Enum.eUserLevel;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTestResultDTO {
    private eSpecialty specialty;
    private int score;
    private LocalDate date;
    private eUserLevel userLevel;
}
