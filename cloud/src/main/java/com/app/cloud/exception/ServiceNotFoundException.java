package com.app.cloud.exception;

public class ServiceNotFoundException extends RuntimeException{
    public ServiceNotFoundException() {
        super("Service not found");
    }
}
