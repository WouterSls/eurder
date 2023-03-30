package com.switchfully.eurder.api;

import com.switchfully.eurder.exception.IllegalAmountException;
import com.switchfully.eurder.exception.IllegalPriceException;
import com.switchfully.eurder.exception.InvalidIdException;
import com.switchfully.eurder.exception.MandatoryFieldException;
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
        return "InvalidIdException: " + exception.getMessage();
    }

    @ExceptionHandler(MandatoryFieldException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    String handleMandatoryFieldException(Exception exception){
        return "MandatoryFieldException: " + exception.getMessage();
    }

    @ExceptionHandler(IllegalPriceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    String handleIllegalPriceException(Exception exception){
        return "IllegalPriceException: " + exception.getMessage();
    }

    @ExceptionHandler(IllegalAmountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    String handleIllegalAmountException (Exception exception){
        return "IllegalAmountException: " + exception.getMessage();
    }
}