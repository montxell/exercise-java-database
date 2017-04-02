package com.codethen.javadbexercise;

public class StringUtils {

    public static String join(Iterable<?> iterable, String iterComplement, String separator) {

        StringBuilder sb = new StringBuilder();

        String currentSeparator = "";

        for (Object iter : iterable) {

            sb.append(currentSeparator).append(iter).append(iterComplement);

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

}
