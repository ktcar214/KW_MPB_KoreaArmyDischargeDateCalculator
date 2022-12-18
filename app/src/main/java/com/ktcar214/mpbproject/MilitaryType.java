package com.ktcar214.mpbproject;

import java.util.Arrays;

public enum MilitaryType {
    UNKNOWN("오류",0,0),
    ARMY("육군",1,6),
    NAVY("해군",1,8),
    MARINE("해병대",1,6),
    AIRFORCE("공군",1,9),
    SOCIALSVC("사회복지요원(공익)",2,0);

    private final Integer serviceYear;
    private final Integer serviceMonth;
    private final String serviceName;
    MilitaryType(String serviceName, Integer serviceYear, Integer serviceMonth){
        this.serviceName=serviceName;
        this.serviceYear=serviceYear;
        this.serviceMonth=serviceMonth;
    }
    public String serviceName(){
        return serviceName;
    }
    public Integer getServiceYear(){
        return serviceYear;
    }
    public Integer getServiceMonth(){return serviceMonth;}
    public MilitaryType valueByServiceName(String input){
        return Arrays.stream(values())
                .filter(value -> value.serviceName.equals(input))
                .findAny()
                .orElse(null);
    }

}
