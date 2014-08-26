package io.teamscala.java.core.util;

/**
 * 한글 처리를 위한 유틸리티 클래스
 * 유니코드 2.0 한글의 범위 * AC00(가) ~ D7A3(힣)
 *
 */
public abstract class HangulUtils {

    /** 초성 테이블 */
    private static final char[] INITIAL_CONSONANTS = {
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
        'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    /** 중성 테이블 */
    private static final char[] VOWELS = {
        'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
        'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    };

    /** 종성 테이블 */
    private static final char[] FINAL_CONSONANTS = {
        ' ' , 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ',
        'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ',
        'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    /**
     * 숫자 테이블
     */
    private static final char[] NUMBERS = {
        '영', '일', '이', '삼', '사', '오', '육', '칠', '팔', '구'
    };

    /**
     * 숫자 단위 테이블 ('극' 단위 까지만 처리)
     *
     * <p>일, 십, 백, 천, 만, 억, 조, 경, 해, 자, 양, 구, 간, 정, 재, 극,
     * 항하사, 아승기, 나유타, 불가사의, 무량대수(10의68제곱)</p>
     */
    private static final char[] NUMBER_UNITS = {
        '십', '백', '천', '만', '억', '조', '경', '해', '자', '양', '구', '간', '정', '재', '극'
    };

    /**
     * 문자 하나가 한글인지 검사한다 ('가' ~ '힣')
     *
     * @param c 문자
     * @return true or false
     */
    public static boolean isHangul(char c) { return !(c < 0xAC00 || c > 0xD7A3); }

    /**
     * 문자열이 한글인지 검사한다
     *
     * @param str 문자열
     * @return true or false
     */
    public static boolean isHangul(String str) {
        if (StringUtils.isEmpty(str)) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!isHangul(str.charAt(i))) return false;
        }
        return true;
    }

    /**
     * 한글 한문자에서 초성을 분리해서 가져온다
     *
     * @param c 문자
     * @return 초성
     */
    public static char getInitial(char c) {
        if (!isHangul(c)) return c;
        return INITIAL_CONSONANTS[((c & 0xFFFF) - 0xAC00) / (21 * 28)];
    }

    /**
     * 문자열의 첫번째 문자의 초성을 분리해서 가져온다
     *
     * @param str 문자열
     * @return 첫번째 문자의 초성
     */
    public static String getFirstInitial(String str) {
        if (StringUtils.isEmpty(str)) return str;
        return String.valueOf(getInitial(str.charAt(0)));
    }

    /**
     * 한글 한문자에서 중성을 분리해서 가져온다
     *
     * @param c 문자
     * @return 중성
     */
    public static char getVowel(char c) {
        if (!isHangul(c)) return c;
        return VOWELS[(((c & 0xFFFF) - 0xAC00) % (21 * 28)) / 28];
    }

    /**
     * 한글 한문자에서 종성을 분리해서 가져온다
     *
     * @param c 문자
     * @return 종성
     */
    public static char getUnder(char c) {
        if (!isHangul(c)) return c;
        return FINAL_CONSONANTS[(((c & 0xFFFF) - 0xAC00) % (21 * 28)) % 28];
    }

    /**
     * 한문자의 종성의 유무를 검사한다
     *
     * @param c 문자
     * @return true or false
     */
    public static boolean hasUnder(char c) {
        return (isHangul(c) && getUnder(c) != FINAL_CONSONANTS[0]);
    }

    /**
     * 문자열의 마지막 문자의 종성 유무를 검사한다
     *
     * @param str 문자열
     * @return true or false
     */
    public static boolean hasLastUnder(String str) {
        if (StringUtils.isEmpty(str)) return false;
        return hasUnder(str.charAt(str.length() - 1));
    }

    /**
     * 숫자를 한글형식으로 바꿔준다
     *
     * @param number 숫자
     * @return 한글숫자
     */
    public static String toHangulNumber(int number) { return toHangulNumber(number, null); }

    /**
     * 숫자를 한글형식으로 바꿔준다
     *
     * @param number 숫자
     * @return 한글숫자
     */
    public static String toHangulNumber(String number) { return toHangulNumber(number, null); }

    /**
     * 숫자를 한글형식으로 바꿔준다
     *
     * @param number 숫자
     * @param delimiter 구분자
     * @return 한글숫자
     */
    public static String toHangulNumber(int number, String delimiter) { return toHangulNumber(String.valueOf(number), delimiter); }

    /**
     * 숫자를 한글형식으로 바꿔준다
     *
     * @param number 숫자
     * @param delimiter 구분자
     * @return 한글숫자
     */
    public static String toHangulNumber(String number, String delimiter) {
        if (!StringUtils.isNumeric(number)) return number;

        // 숫자가 0으로 시작할 경우 0이 아닐때까지 건너 뛴다
        if (number.charAt(0) == '0') {
            number = StringUtils.stripStart(number, "0");
            if (number.isEmpty()) {
                return String.valueOf(NUMBERS[0]);
            }
        }

        // 숫자가 한자리일 경우
        if (number.length() == 1) return String.valueOf(NUMBERS[number.charAt(0) - '0']);

        int unit = NUMBER_UNITS.length - 3; // (십, 백, 천) 3자리를 빼준다.
        int rest = number.length() - 1;

        // 숫자의 자릿수가 표현 단위를 초과하였을 경우
        if (rest / 4 > unit) {
            throw new IllegalArgumentException("Number is overflow [" + number + "]");
        }

        StringBuilder result = new StringBuilder(100);
        for (int i = 0, ignoreNumber = 1; i < number.length(); i++, rest--) {
            int c = number.charAt(i) - '0';

            if ((unit = rest % 4) == 0) {
                if (c > 0) result.append(NUMBERS[c]);
                if (rest > 0) {
                    if (c >= ignoreNumber) {
                        result.append(NUMBER_UNITS[rest / 4 + 2]);
                        ignoreNumber = 1;
                    }
                    if (delimiter != null) {
                        result.append(delimiter);
                    }
                }
            } else {
                if (c > 1) result.append(NUMBERS[c]);
                if (c > 0) {
                    result.append(NUMBER_UNITS[unit - 1]);
                    ignoreNumber = 0;
                }
            }
        }

        return result.toString();
    }


    // 테스트
    public static void main(String[] args) {
        String s = "석기원";
        char c = '석';

        System.out.println(toHangulNumber("1111111111"));
        System.out.println(toHangulNumber("00000000000001"));
        System.out.println(toHangulNumber("00000010000000000"));
        System.out.println(toHangulNumber("111111111111111111111", " "));
        System.out.println(toHangulNumber("1346430412632951232"));
        System.out.print(getInitial(c));
        System.out.print(getVowel(c));
        System.out.println(getUnder(c));
        System.out.println(getFirstInitial(s));
        System.out.println(hasLastUnder(s));
        System.out.println(isHangul("가나다라마바사아자차카타파하"));
    }
}
