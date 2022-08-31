package com.example.iremboback.config;

import java.util.Random;

public final class Constant {

    public final static String AUTH_PATH = "/api/v1/auth";

    public final static String EMAIL_RESET_PASSWORD_LINK = "http://EMAIL-MICROSERVICE/email-microservice/api/v1/email-services/reset";

    public final static String EMAIL_REGISTRATION_VERIFICATION_LINK = "http://EMAIL-MICROSERVICE/email-microservice/api/v1/email-services/verification";

    public final static String EMAIL_OTP_LINK = "http://EMAIL-MICROSERVICE/email-microservice/api/v1/email-services/otp";


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
