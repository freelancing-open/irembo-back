package com.example.iremboback.config;

import java.util.Random;

public final class Constant {

    public final static String AUTH_PATH = "/api/v1/auth";

    public static String RANDOM_ALPHANUMERIC() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 25;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static Integer RANDOM_NUMBER() {
        Random random = new Random();
        return random.nextInt(100000);
    }
}
