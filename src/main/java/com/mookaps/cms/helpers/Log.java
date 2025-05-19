package com.mookaps.cms.helpers;

public class Log {

    private static final String RESET = "\u001B[0m";

    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";

    public static final String GREY = "\u001B[30m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    private static final String time = GREY + Common.currentDateTime();

    public static void info(String message) {
        System.out.println(time + "  " + GREEN + "INFO" + GREY + " : " + RESET + message);
    }

    public static void error(String message) {
        System.out.println(time + "  " + RED + "ERR" + GREY + "  : " + RESET + message);
    }

    public static void warning(String message) {
        System.out.println(time + "  " + YELLOW + "WARN" + GREY + " : " + RESET + message);
    }

    public static void custom(String message, String color) {
        System.out.println(time + "  " + color + "LOG  : " + RESET + message);
    }
}
