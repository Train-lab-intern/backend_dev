package com.trainlab.enums;


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

    private  final  String spec;

    Speciality(String spec) {
        this.spec = spec;
    }

    public  String getSpeciality(){
        return  spec;
    }
}
