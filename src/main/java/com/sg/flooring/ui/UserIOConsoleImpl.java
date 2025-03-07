package com.sg.flooring.ui;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserIOConsoleImpl implements UserIO {
    Scanner input = new Scanner(System.in);

    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public boolean readBoolean(String prompt) {
        System.out.println(prompt);
        boolean isValid = false;

        String answer = "";

        while (!isValid) {
            answer = input.nextLine().toUpperCase();
            if (answer.equals("YES") || answer.equals("NO")) {
                isValid = true;
            }
        }

        if (answer.equals("YES")) {
            return true;
        }
        else {
            return false;
        }
    }

    public int readInt(String prompt) {
        System.out.println(prompt);
        int answer = input.nextInt();
        input.nextLine();
        return answer;
    }

    public int readInt(String prompt, int min, int max) {
        System.out.println(prompt);
        boolean isValid = false;
        int answer = 0;
        while (!isValid) {
            answer = input.nextInt();
            input.nextLine();
            if (answer <= max && answer >= min) {
                isValid = true;
            }
            else {
                System.out.println("Invalid number. Please choose numbers from " + min + " and " + max);
            }

        }
        return answer;
    }

    public String readString(String prompt) {
        System.out.println(prompt);
        return input.nextLine();
    }

    public String readString(String prompt, int minChars, int maxChars) {
        System.out.println(prompt + " (" + minChars + "-" + maxChars + ").");
        return input.nextLine();
    }


    public LocalDate readLocalDate(String prompt) {
        System.out.println(prompt);
        System.out.println(" Format: (mm/dd/yyyy)");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        boolean isValid = false;
        String answer;
        String datePattern = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$";
        Pattern pattern = Pattern.compile(datePattern);
        LocalDate userDateAnswer = null;

        while (!isValid) {
            answer = input.nextLine();
            Matcher matcher = pattern.matcher(answer);

            if (!matcher.matches()) {
                System.out.println("Invalid date. Must be in format: mm/dd/yyyy");
                continue;
            }

            userDateAnswer = LocalDate.parse(answer, formatter);
            isValid = true;
        }
        return userDateAnswer;
    }


    public LocalDate readLocalDate(String prompt, LocalDate minDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        System.out.print(prompt + " Minimum Date being: " + minDate.format(formatter));
        System.out.println(" Format: (mm/dd/yyyy)");

        boolean isValid = false;
        String answer;
        String datePattern = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$";
        Pattern pattern = Pattern.compile(datePattern);
        LocalDate userDateAnswer = null;

        while (!isValid) {
            answer = input.nextLine();
            Matcher matcher = pattern.matcher(answer);

            if (!matcher.matches()) {
                System.out.println("Invalid date. Must be in format: mm/dd/yyyy");
                continue;
            }

            userDateAnswer = LocalDate.parse(answer, formatter);
            Period diff = minDate.until(userDateAnswer);
            if (diff.getDays() >= 0) {
                isValid = true;
            }
            else {
                System.out.println("Invalid date. Must be today or a future date.");
            }
        }

        return userDateAnswer;
    }


    public BigDecimal readBigDecimal(String prompt) {
        System.out.println(prompt);
        BigDecimal answer = input.nextBigDecimal();
        input.nextLine();
        return answer;
    }


    public BigDecimal readBigDecimal(String prompt, int scale, BigDecimal min) {
        boolean isValid = false;
        System.out.println(prompt + " Minimum: " + min);
        BigDecimal answer = null;
        while (!isValid) {
            answer = input.nextBigDecimal();
            input.nextLine();

            if (answer.compareTo(min) == 1 || answer.compareTo(min) == 0) {
                isValid = true;
            }
            else {
                System.out.println("Invalid. Minimum of " + min + "sqft needed.");
            }
        }

        return answer;
    }


}