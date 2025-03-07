package com.sg.flooring.dao;

public class FlooringDataPersistenceException extends RuntimeException {
    public FlooringDataPersistenceException(String message) {
        super(message);
    }

    public FlooringDataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
