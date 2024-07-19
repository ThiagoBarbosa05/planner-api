package com.thiago.planner.exceptions;

public class ActivitySameTimeException extends RuntimeException{

    public ActivitySameTimeException(String message) {
        super(message);
    }
}
