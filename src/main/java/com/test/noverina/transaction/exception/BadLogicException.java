package com.test.noverina.transaction.exception;

import lombok.Getter;

@Getter
public class BadLogicException extends RuntimeException {
    public BadLogicException(String message) {
        super(message);
    }

}
