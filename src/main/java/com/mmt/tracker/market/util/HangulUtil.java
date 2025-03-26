package com.mmt.tracker.market.util;

public class HangulUtil {

    private static final char[] INITIAL = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    private HangulUtil() {
        throw new IllegalStateException("유틸리티 클래스입니다.");
    }

    public static String getInitial(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= 0xAC00 && c <= 0xD7A3) {
                int index = (c - 0xAC00) / (21 * 28);
                sb.append(INITIAL[index]);
            } else if (Character.isWhitespace(c)) {
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String removeSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }
}
