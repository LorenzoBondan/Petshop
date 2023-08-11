package com.projects.petshop.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) 
public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String customMessage;

    public UnauthorizedException(String customMessage) {
        super(customMessage);
        this.customMessage = customMessage;
    }

    public String getCustomMessage() {
        return customMessage;
    }
}
