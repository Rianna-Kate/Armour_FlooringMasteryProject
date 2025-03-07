package com.sg.flooring.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIO {

    void print(String message);

    boolean readBoolean(String prompt);

    int readInt(String prompt);
    int readInt(String prompt, int min, int max);

    String readString(String prompt);
    String readString(String prompt, int minChars, int maxChars);

    LocalDate readLocalDate(String prompt);
    LocalDate readLocalDate(String prompt, LocalDate minDate);

    BigDecimal readBigDecimal(String prompt);
    BigDecimal readBigDecimal(String prompt, int scale, BigDecimal min);
}
