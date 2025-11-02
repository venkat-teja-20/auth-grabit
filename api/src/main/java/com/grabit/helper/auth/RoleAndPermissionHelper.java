package com.grabit.helper.auth;

import com.grabit.Utilities.Utility;
import com.grabit.exception.CustomException;

public class RoleAndPermissionHelper {

    public Boolean isValid(String id,String name,String service){
        if(Utility.isNumeric(id))
            return true;
        name=name.toUpperCase();
        throw new CustomException(Utility.buildErrorObject("INVALID_"+name,name+" id : "+id+" is not valid",400,"service"));
    }
}
