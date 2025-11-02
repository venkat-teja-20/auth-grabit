package com.grabit.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.grabit.Utilities.Utility;
import com.grabit.exception.CustomException;

public enum MemberActiveStatus {
    TRUE,

    FALSE;

    @JsonCreator
    public static MemberActiveStatus fromValue(String value){
        for(MemberActiveStatus memberActiveStatus:values()){
            if(value.equalsIgnoreCase(memberActiveStatus.name()))
                return memberActiveStatus;
        }
        throw new CustomException(Utility.buildErrorObject("INVALID_ACTIVE_STATUS","No such member active status exists : "+value,400,"MemberActiveStatus"));
    }

    @JsonValue
    public String toValue(){
        return this.name();
    }
}
