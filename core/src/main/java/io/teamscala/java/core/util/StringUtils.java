package io.teamscala.java.core.util;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 문자열 관련 유틸리티 클래스.
 * 
 * @author 석기원
 */
public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 문자열 토큰을 포함하는지 확인한다.
     *
     * @param input 입력 문자열
     * @param searchStr 검색 문자열
     * @return 확인결과
     */
    public static boolean containsToken(String input, String searchStr) { return containsToken(input, searchStr, null, false); }

    /**
     * 문자열 토큰을 포함하는지 확인한다.
     *
     * @param input 입력 문자열
     * @param searchStr 검색 문자열
     * @param delim 구분자
     * @return 확인결과
     */
    public static boolean containsToken(String input, String searchStr, String delim) { return containsToken(input, searchStr, delim, false); }

    /**
     * 문자열 토큰을 포함하는지 확인한다.
     *
     * @param input 입력 문자열
     * @param searchStr 검색 문자열
     * @param ignoreCase 대소문자 무시여부
     * @return 확인결과
     */
    public static boolean containsToken(String input, String searchStr, boolean ignoreCase) { return containsToken(input, searchStr, null, ignoreCase); }

    /**
     * 문자열 토큰을 포함하는지 확인한다.
     *
     * @param input 입력 문자열
     * @param searchStr 검색 문자열
     * @param delim 구분자
     * @param ignoreCase 대소문자 무시여부
     * @return 확인결과
     */
    public static boolean containsToken(String input, String searchStr, String delim, boolean ignoreCase) {
        if (input == null || searchStr == null)
            return false;

        StringTokenizer token = (delim == null) ? new StringTokenizer(input) : new StringTokenizer(input, delim);
        while (token.hasMoreTokens()) {
            if (ignoreCase) {
                if (token.nextToken().equalsIgnoreCase(searchStr)) return true;
            } else {
                if (token.nextToken().equals(searchStr)) return true;
            }
        }
        return false;
    }

    /**
     * 문자열을 주어진 배열에 따라 바꾼다.
     *
     * <pre>
     * StringUtils.replace(null, *)               = null
     * StringUtils.replace("", *)                 = ""
     * StringUtils.replace("any", null)           = "any"
     * StringUtils.replace("abca", new String[][]{
     *     {"a", "z"},
     *     {"b", "x"}
     * })                                         = "zxcz"
     * </pre>
     *
     * @param input 입력 문자열
     * @param replaces 바꿀 문자열 배열
     * @return 바뀐 문자열
     */
    public static String replace(String input, String[][] replaces) { return replace(input, replaces, -1); }

    /**
     * 문자열을 주어진 배열에 따라 바꾼다.
     *
     * <pre>
     * StringUtils.replace(null, *, *)            = null
     * StringUtils.replace("", *, *)              = ""
     * StringUtils.replace("any", null, *)        = "any"
     * StringUtils.replace("any", *, 0)           = "any"
     * StringUtils.replace("abca", new String[][]{
     *     {"a", "z"},
     *     {"b", "x"}
     * }, 0)                                      = "abca"
     * StringUtils.replace("abca", new String[][]{
     *     {"a", "z"},
     *     {"b", "x"}
     * }, 1)                                      = "zbca"
     * StringUtils.replace("abca", new String[][]{
     *     {"a", "z"},
     *     {"b", "x"}
     * }, -1)                                     = "zxcz"
     * </pre>
     *
     * @param input 입력 문자열
     * @param replaces 바꿀 문자열 배열
     * @param max 최대 갯수
     * @return 바뀐 문자열
     */
    public static String replace(String input, String[][] replaces, int max) {
        if (isEmpty(input) || replaces == null || replaces.length == 0 || max == 0) {
            return input;
        }

        StringBuilder sb = new StringBuilder(input.length() * 2);
        OUTTER: for (int i = 0; i < input.length(); i++) {
            if (max == 0) {
                sb.append(input.substring(i));
                break;
            }
            for (String[] r : replaces) {
                if (r.length < 2 || r[0] == null || r[0].isEmpty() || r[1] == null || r[1].isEmpty()) {
                    continue;
                }
                if (input.startsWith(r[0], i)) {
                    sb.append(r[1]);
                    i += r[0].length() - 1;
                    if (max > 0) max--;
                    continue OUTTER;
                }
            }
            sb.append(input.charAt(i));
        }
        return sb.toString();
    }

    /**
     * 태그를 제거한다.
     *
     * @param input 입력 문자열
     * @return 태그가 제거된 문자열
     */
    public static String stripTags(String input) {
        if (StringUtils.isNotEmpty(input)) {
            input = input.replaceAll("<[/!]*?[^<>]*?>", "");
        }
        return input;
    }

    /**
     * 문자열을 일정길이만큼 잘라서 가져온다.
     *
     * @param input 입력 문자열
     * @param length 잘라낼 길이
     * @return 처리된 문자열
     */
    public static String cutstring(String input, int length) { return cutstring(input, length, null); }

    /**
     * 문자열을 일정길이만큼 잘라서 가져온다.
     *
     * @param input 입력 문자열
     * @param length 잘라낼 길이
     * @param suffix 접미사
     * @return 처리된 문자열
     */
    public static String cutstring(String input, int length, String suffix) {
        if (input == null || input.length() <= length) {
            return input;
        }
        return input.substring(0, length) + defaultString(suffix);
    }

    /**
     * 문자열 행마다 앞쪽에 문자열을 붙여준다.
     *
     * @param input 입력 문자열
     * @param str 붙여줄 문자열
     * @return 변형된 문자열
     */
    public static String prependEachLine(String input, String str) {
        if (StringUtils.isNotEmpty(input)) {
            StringBuilder sb = new StringBuilder((int) (input.length() * 1.5));
            StringTokenizer token = new StringTokenizer(input, "\r\n");
            while (token.hasMoreTokens()) {
                sb.append(str).append(token.nextToken());
                if (token.hasMoreTokens())
                    sb.append("\n");
            }
            input = sb.toString();
        }
        return input;
    }

    /**
     * Quote string.
     *
     * @param input the input string
     * @return the quoted string.
     */
    public static String quote(String input) {
        if (input == null || input.length() == 0) {
            return "\"\"";
        }

        char c = '\0';
        int len = input.length();
        StringBuilder output = new StringBuilder(len * 2);
        char chars[] = input.toCharArray();
        char buffer[] = new char[1030];
        int bufferIndex = 0;

        output.append('"');
        for (int i = 0; i < len; i++) {
            if (bufferIndex > 1024) {
                output.append(buffer, 0, bufferIndex);
                bufferIndex = 0;
            }
            char b = c;
            c = chars[i];
            switch (c) {
            case 34: // '"'
            case 92: // '\\'
                buffer[bufferIndex++] = '\\';
                buffer[bufferIndex++] = c;
                break;

            case 47: // '/'
                if (b == '<')
                    buffer[bufferIndex++] = '\\';
                buffer[bufferIndex++] = c;
                break;

            default:
                if (c < ' ') {
                    switch (c) {
                    case 8: // '\b'
                        buffer[bufferIndex++] = '\\';
                        buffer[bufferIndex++] = 'b';
                        break;

                    case 9: // '\t'
                        buffer[bufferIndex++] = '\\';
                        buffer[bufferIndex++] = 't';
                        break;

                    case 10: // '\n'
                        buffer[bufferIndex++] = '\\';
                        buffer[bufferIndex++] = 'n';
                        break;

                    case 12: // '\f'
                        buffer[bufferIndex++] = '\\';
                        buffer[bufferIndex++] = 'f';
                        break;

                    case 13: // '\r'
                        buffer[bufferIndex++] = '\\';
                        buffer[bufferIndex++] = 'r';
                        break;

                    case 11: // '\013'
                    default:
                        String t = "000" + Integer.toHexString(c);
                        int tLength = t.length();
                        buffer[bufferIndex++] = '\\';
                        buffer[bufferIndex++] = 'u';
                        buffer[bufferIndex++] = t.charAt(tLength - 4);
                        buffer[bufferIndex++] = t.charAt(tLength - 3);
                        buffer[bufferIndex++] = t.charAt(tLength - 2);
                        buffer[bufferIndex++] = t.charAt(tLength - 1);
                        break;
                    }
                } else {
                    buffer[bufferIndex++] = c;
                }
                break;
            }
        }

        output.append(buffer, 0, bufferIndex);
        output.append('"');
        return output.toString();
    }

    /**
     * 인코딩 변경을 테스트 한다.
     *
     * @param value 값
     * @param encoding 변경 테스트 할 인코딩
     * @return 잘못된 값의 리스트
     * @throws UnsupportedEncodingException {@link java.io.UnsupportedEncodingException}
     */
    public static List<String> checkTranscoding(String value, String encoding) throws UnsupportedEncodingException {
        List<String> invalidChars = new ArrayList<>();
        for (int i = 0; i < value.length(); i++) {
            String c = Character.toString(value.charAt(i));
            String encodingChar = new String(c.getBytes(encoding), encoding);
            String convertedChar = new String(encodingChar.getBytes());
            if (!c.equals(convertedChar) && !invalidChars.contains(c)) {
                invalidChars.add(c);
            }
        }
        return invalidChars;
    }

    /**
     * Underscore and hyphen convert to camel case.
     *
     * @param input the input string
     * @return the converted string
     */
    public static String toCamelCase(String input) {
        if (input == null) {
            return null;
        }
        input = input.toLowerCase();
        StringBuffer output = new StringBuffer();
        Pattern pattern = Pattern.compile("[-_ ]([\\da-z])", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matcher.appendReplacement(output, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(output);
        return output.toString();
    }

    /**
     * String convert to Map.
     *
     * @param input the input string.
     * @return the converted Map.
     */
    public static Map<String, String> toMap(String input) { return toMap(input, false); }

    /**
     * String convert to Map.
     *
     * @param input the input string.
     * @param trimValue true if trim value.
     * @return the converted Map.
     */
    public static Map<String, String> toMap(String input, boolean trimValue) {
        if (input == null) return Collections.emptyMap();

        if (input.charAt(0) == '{')
            input = input.substring(1);

        if (input.charAt(input.length() - 1) == '}')
            input = input.substring(0, input.length() - 1);

        Map<String, String> map = new LinkedHashMap<>();

        int inputLength = input.length();
        int keyStart = 0;
        int eqPos;
        int valEnd;

        while (true) {
            if (keyStart > inputLength) break;

            eqPos = input.indexOf("=", keyStart);
            if (eqPos == -1)
                throw new IllegalArgumentException("No = after " + keyStart);

            valEnd = input.indexOf(", ", eqPos);
            if (valEnd == -1) valEnd = input.length();

            // check that the next valEnd occurs after the next eqPos

            String keyValue = input.substring(keyStart, eqPos);
            String valValue = input.substring(eqPos +1, valEnd);
            map.put(keyValue, trimValue ? valValue.trim() : valValue);
            keyStart = valEnd + 2;
        }

        return map;
    }
}