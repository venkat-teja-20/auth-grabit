package com.grabit.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.grabit.Utilities.Utility;
import com.grabit.exception.CustomException;

public enum AccessLevel {
    member,

    system_user;

    @JsonCreator
    public static AccessLevel fromValue(String value){
        for(AccessLevel accessLevel:values()){
            if(value.equalsIgnoreCase(accessLevel.name()))
                return accessLevel;
        }
        throw new CustomException(Utility.buildErrorObject("INVALID_ACCESS_LEVEL","No such Access Level exists : "+value,400,"Role"));
    }

    @JsonValue
    public String toValue(){
        return this.name();
    }
}
