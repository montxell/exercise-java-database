package com.codethen.javadbexercise.util;

public class StringUtils {

    public static String join(Iterable<?> iterable, String complementOfIter, String separator) {

        StringBuilder sb = new StringBuilder();

        String currentSeparator = "";

        for (Object iter : iterable) {

            sb.append(currentSeparator).append(iter).append(complementOfIter);

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
