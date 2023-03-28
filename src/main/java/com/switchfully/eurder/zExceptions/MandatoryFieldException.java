package com.switchfully.eurder.zExceptions;

public class MandatoryFieldException extends NullPointerException {
    public MandatoryFieldException(String s){
        super(s);
    }
}
