package com.trainlab.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Speciality {
//1. BA / SA
//2. Front-end
//3. Back-end
//4. Design
//5. AQA
//6. QA
//7. PM

    BA("BA/SA"),
    FRONT("Front-end"),
    BACK("Back-end"),
    DESIGN("Design"),
    AQA("AQA"),
    QA("QA"),
    PM("PM");

    @Getter
    private final String val;
}
