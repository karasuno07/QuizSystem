package com.fsoft.quizsystem.object.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("You do not have authorization to modify this resource.");
    }
}
