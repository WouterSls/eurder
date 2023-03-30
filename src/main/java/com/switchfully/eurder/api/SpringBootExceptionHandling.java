package com.switchfully.eurder.api;

import com.switchfully.eurder.exception.InvalidIdException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import com.switchfully.eurder.exception.NoCustomersException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SpringBootExceptionHandling {
    @ExceptionHandler(InvalidIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    String handleInvalidIdException(Exception exception) {
        return "Oops: " + exception.getMessage();
    }

    @ExceptionHandler(NoCustomersException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    String handleNoCustomersException(Exception exception){
        return exception.getMessage();
    }


    @ExceptionHandler(MandatoryFieldException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    String handleMandatoryFieldException(Exception exception){
        return exception.getMessage();
    }
}