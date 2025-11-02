package com.grabit.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
public class MemberUrlMapper {

    @Getter(AccessLevel.NONE)
    String domainUrl;

    public MemberUrlMapper(String url){
        this.domainUrl=url;
    }

    public String createMemberUrl(){
        return domainUrl+"/member/create";
    }
}
