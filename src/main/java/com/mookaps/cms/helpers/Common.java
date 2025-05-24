package com.mookaps.cms.helpers;

import java.security.SecureRandom;
import java.time.*;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpServletRequest;

public class Common {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String currentDateTime() {
        LocalDateTime date = LocalDateTime.now();
        String[] list = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).split("T");
        return list[0] + " " + list[1].substring(0, 12);
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static String getBaseUrl(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() +
                (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
                request.getContextPath();

        return baseUrl;
    }
}
