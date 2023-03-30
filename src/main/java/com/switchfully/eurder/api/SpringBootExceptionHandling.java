package com.switchfully.eurder.api;

import com.switchfully.eurder.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SpringBootExceptionHandling {
    @ExceptionHandler(InvalidIdFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    String handleInvalidIdException(Exception exception) {
        return "InvalidIdFormatException: " + exception.getMessage();
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

    @ExceptionHandler(NoCustomersException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    String handleNoCustomersException(Exception exception){
        return "NoCustomersException: " + exception.getMessage();
    }

    @ExceptionHandler(NoItemsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    String handleNoItemsException(Exception exception){
        return "NoItemsException: " + exception.getMessage();
    }
}