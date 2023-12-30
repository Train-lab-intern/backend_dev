package com.trainlab.enums;

public enum UserLevel {
    RAW("Трейни"),
    RARE("Джуниор"),
    MEDIUM_RARE("продвинутый джуниор"),
    MEDIUM("Мидл"),
    MEDIUM_WELL("Сениор"),
    WELL_DONE("Мастер");

    private  final  String level;

    UserLevel(String level) {
        this.level = level;
    }

    public  String getLevel(){
        return level;
    }
}
