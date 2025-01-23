package com.airBnb.project.AirBnbWebApp.exception;

public class UnAuthorisedException extends RuntimeException{
    public UnAuthorisedException(String message){
        super(message);
    }
}
