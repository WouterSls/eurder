package com.switchfully.eurder.exception;

public class MandatoryFieldException extends NullPointerException {
    public MandatoryFieldException(String s){
        super(s);
    }
}
