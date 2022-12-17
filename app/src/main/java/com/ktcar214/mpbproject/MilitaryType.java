package com.ktcar214.mpbproject;

import java.util.Arrays;

public enum MilitaryType {
    UNKNOWN("오류",0),
    ARMY("육군",123),
    NAVY("해군",123),
    MARINE("해병대",123),
    AIRFORCE("공군",123),
    SOCIALSVC("사회복지요원(공익)",123);

    private final Integer serviceDuration;
    private final String serviceName;
    MilitaryType(String serviceName, Integer serviceDuration){
        this.serviceDuration=serviceDuration;
        this.serviceName=serviceName;
    }
    public String serviceName(){
        return serviceName;
    }
    public Integer serviceDuration(){
        return serviceDuration;
    }
    public MilitaryType valueByServiceName(String input){
        return Arrays.stream(values())
                .filter(value -> value.serviceName.equals(input))
                .findAny()
                .orElse(null);
    }

}
