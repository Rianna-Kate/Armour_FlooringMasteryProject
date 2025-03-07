package com.sg.flooring.dao;

public class StateTaxNotFoundException extends RuntimeException {
    public StateTaxNotFoundException(String message) {
        super(message);
    }

  public StateTaxNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
