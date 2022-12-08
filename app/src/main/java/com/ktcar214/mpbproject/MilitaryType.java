package com.ktcar214.mpbproject;

import java.util.Arrays;

public enum MilitaryType {
    UNKNOWN("오류"),
    ARMY("육군"),
    NAVY("해군"),
    MARINE("해병대"),
    AIRFORCE("공군"),
    SOCIALSVC("사회복지요원(공익)");

    private final String serviceName;
    MilitaryType(String serviceName){
        this.serviceName=serviceName;
    }
    public String serviceName(){
        return serviceName;
    }

    public MilitaryType valueByServiceName(String input){
        return Arrays.stream(values())
                .filter(value -> value.serviceName.equals(input))
                .findAny()
                .orElse(null);
    }

}
