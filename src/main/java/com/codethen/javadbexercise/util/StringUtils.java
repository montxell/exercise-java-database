package com.codethen.javadbexercise.util;

// SIMULACIÓN DE StringUtils de la dependency commons.lang

public class StringUtils {

    public static String join(Iterable<?> iterable, String strOptionalForIter, String separator) {

        StringBuilder sb = new StringBuilder();

        String currentSeparator = "";

        for (Object iter : iterable) {

            sb.append(currentSeparator).append(iter).append(strOptionalForIter);

            currentSeparator = separator;

        }

        return sb.toString();
    }



    public static String repeat(String strToRepeat, String separator, int times) {

        StringBuilder sb = new StringBuilder();

        String currentSeparator = "";

        for (int i = 1; i <= times; i++) {

            sb.append(currentSeparator).append(strToRepeat);

            currentSeparator = separator;
        }

        return sb.toString();
    }



    public static String capitalize(String input) {

        return input.substring(0, 1).toUpperCase() + input.substring(1);

    }

}
