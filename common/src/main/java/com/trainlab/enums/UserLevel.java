package com.trainlab.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserLevel {
    RAW("Traine"),
    RARE("Junior"),
    MEDIUM_RARE("Junior plus"),
    MEDIUM("Middle"),
    MEDIUM_WELL("Senior"),
    WELL_DONE("Master");

    @Getter
    private final String value;
}
