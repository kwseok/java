package io.teamscala.java.core.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 날짜 관련 유틸리티 클래스.
 */
public abstract class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * 현재 날짜를 주어진 형식으로 파싱하여 가져온다.
     *
     * @param pattern 날짜 패턴
     * @return 날짜 객체
     * @throws ParseException {@link java.text.ParseException}
     */
    public static Date parse(String pattern) throws ParseException {
        return parse(pattern, TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * 현재 날짜를 주어진 형식으로 파싱하여 가져온다.
     *
     * @param pattern 날짜 패턴
     * @param locale  {@link Locale}
     * @return 날짜 객체
     * @throws ParseException {@link java.text.ParseException}
     */
    public static Date parse(String pattern, Locale locale) throws ParseException {
        return parse(pattern, TimeZone.getDefault(), locale);
    }

    /**
     * 현재 날짜를 주어진 형식으로 파싱하여 가져온다.
     *
     * @param pattern  날짜 패턴
     * @param timeZone {@link TimeZone}
     * @return 날짜 객체
     * @throws ParseException {@link java.text.ParseException}
     */
    public static Date parse(String pattern, TimeZone timeZone) throws ParseException {
        return parse(pattern, timeZone, Locale.getDefault());
    }

    /**
     * 현재 날짜를 주어진 형식으로 파싱하여 가져온다.
     *
     * @param pattern  날짜 패턴
     * @param timeZone {@link TimeZone}
     * @param locale   {@link Locale}
     * @return 날짜 객체
     * @throws ParseException {@link java.text.ParseException}
     */
    public static Date parse(String pattern, TimeZone timeZone, Locale locale) throws ParseException {
        String date = format(Calendar.getInstance(), pattern, timeZone);
        return parse(date, pattern, timeZone, locale);
    }

    /**
     * 문자열 형식의 날짜를 파싱하여 Date 객체로 가져온다.
     *
     * @param date    날짜형식의 문자열
     * @param pattern 날짜 패턴
     * @return 날짜 객체
     * @throws ParseException {@link java.text.ParseException}
     */
    public static Date parse(String date, String pattern) throws ParseException {
        return parse(date, pattern, TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * 문자열 형식의 날짜를 파싱하여 Date 객체로 가져온다.
     *
     * @param date    날짜형식의 문자열
     * @param pattern 날짜 패턴
     * @param locale  {@link Locale}
     * @return 날짜 객체
     * @throws ParseException {@link java.text.ParseException}
     */
    public static Date parse(String date, String pattern, Locale locale) throws ParseException {
        return parse(date, pattern, TimeZone.getDefault(), locale);
    }

    /**
     * 문자열 형식의 날짜를 파싱하여 Date 객체로 가져온다.
     *
     * @param date     날짜형식의 문자열
     * @param pattern  날짜 패턴
     * @param timeZone {@link TimeZone}
     * @return 날짜 객체
     * @throws ParseException {@link java.text.ParseException}
     */
    public static Date parse(String date, String pattern, TimeZone timeZone) throws ParseException {
        return parse(date, pattern, timeZone, Locale.getDefault());
    }

    /**
     * 문자열 형식의 날짜를 파싱하여 Date 객체로 가져온다.
     *
     * @param date     날짜형식의 문자열
     * @param pattern  날짜 패턴
     * @param timeZone {@link TimeZone}
     * @param locale   {@link Locale}
     * @return 날짜 객체
     * @throws ParseException {@link java.text.ParseException}
     */
    public static Date parse(String date, String pattern, TimeZone timeZone, Locale locale) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat(pattern, locale);
        parser.setLenient(true);
        parser.setTimeZone(timeZone);
        return parser.parse(date);
    }

    /**
     * 현재 날짜를 문자열로 변환하여 가져온다.
     *
     * @param pattern 날짜 패턴
     * @return 날짜형식의 문자열
     */
    public static String format(String pattern) {
        return format(pattern, TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * 현재 날짜를 문자열로 변환하여 가져온다.
     *
     * @param pattern 날짜 패턴
     * @param locale  {@link Locale}
     * @return 날짜형식의 문자열
     */
    public static String format(String pattern, Locale locale) {
        return format(pattern, TimeZone.getDefault(), locale);
    }

    /**
     * 현재 날짜를 문자열로 변환하여 가져온다.
     *
     * @param pattern  날짜 패턴
     * @param timeZone {@link TimeZone}
     * @return 날짜형식의 문자열
     */
    public static String format(String pattern, TimeZone timeZone) {
        return format(pattern, timeZone, Locale.getDefault());
    }

    /**
     * 현재 날짜를 문자열로 변환하여 가져온다.
     *
     * @param pattern  날짜 패턴
     * @param timeZone {@link TimeZone}
     * @param locale   {@link Locale}
     * @return 날짜형식의 문자열
     */
    public static String format(String pattern, TimeZone timeZone, Locale locale) {
        return format(Calendar.getInstance().getTime(), pattern, timeZone, locale);
    }

    /**
     * 날짜를 문자열로 변환하여 가져온다.
     *
     * @param date    날짜 객체
     * @param pattern 날짜 패턴
     * @return 날짜형식의 문자열
     */
    public static String format(Date date, String pattern) {
        return format(date, pattern, TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * 날짜를 문자열로 변환하여 가져온다.
     *
     * @param date    날짜 객체
     * @param pattern 날짜 패턴
     * @param locale  {@link Locale}
     * @return 날짜형식의 문자열
     */
    public static String format(Date date, String pattern, Locale locale) {
        return format(date, pattern, TimeZone.getDefault(), locale);
    }

    /**
     * 날짜를 문자열로 변환하여 가져온다.
     *
     * @param date     날짜 객체
     * @param pattern  날짜 패턴
     * @param timeZone {@link TimeZone}
     * @return 날짜형식의 문자열
     */
    public static String format(Date date, String pattern, TimeZone timeZone) {
        return format(date, pattern, timeZone, Locale.getDefault());
    }

    /**
     * 날짜를 문자열로 변환하여 가져온다.
     *
     * @param date     날짜 객체
     * @param pattern  날짜 패턴
     * @param timeZone {@link TimeZone}
     * @param locale   {@link Locale}
     * @return 날짜형식의 문자열
     */
    public static String format(Date date, String pattern, TimeZone timeZone, Locale locale) {
        return DateFormatUtils.format(date, pattern, timeZone, locale);
    }

    /**
     * 날짜를 문자열로 변환하여 가져온다.
     *
     * @param calendar {@link Calendar}
     * @param pattern  날짜 패턴
     * @return 날짜형식의 문자열
     */
    public static String format(Calendar calendar, String pattern) {
        return format(calendar, pattern, TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * 날짜를 문자열로 변환하여 가져온다.
     *
     * @param calendar {@link Calendar}
     * @param pattern  날짜 패턴
     * @param locale   {@link Locale}
     * @return 날짜형식의 문자열
     */
    public static String format(Calendar calendar, String pattern, Locale locale) {
        return format(calendar, pattern, TimeZone.getDefault(), locale);
    }

    /**
     * 날짜를 문자열로 변환하여 가져온다.
     *
     * @param calendar {@link Calendar}
     * @param pattern  날짜 패턴
     * @param timeZone {@link TimeZone}
     * @return 날짜형식의 문자열
     */
    public static String format(Calendar calendar, String pattern, TimeZone timeZone) {
        return format(calendar, pattern, timeZone, Locale.getDefault());
    }

    /**
     * 날짜를 문자열로 변환하여 가져온다.
     *
     * @param calendar {@link Calendar}
     * @param pattern  날짜 패턴
     * @param timeZone {@link TimeZone}
     * @param locale   {@link Locale}
     * @return 날짜형식의 문자열
     */
    public static String format(Calendar calendar, String pattern, TimeZone timeZone, Locale locale) {
        return DateFormatUtils.format(calendar, pattern, timeZone, locale);
    }

    /**
     * 날짜 이하의 시간을 잘라낸다.
     *
     * @param date {@link Date} or {@link Calendar}
     * @return {@link Date}
     */
    public static Date truncateDate(Object date) {
        return truncate(date, Calendar.DATE);
    }


    /**
     * 두 날짜의 차이를 밀리초로 가져온다.
     *
     * @param fromDate from date
     * @param toDate   to date
     * @return 두 날짜의 차이 (Millis)
     */
    public static long diffTimes(Date fromDate, Date toDate) {
        return toDate.getTime() - fromDate.getTime();
    }

    /**
     * 두 날짜의 차이를 밀리초로 가져온다.
     *
     * @param fromDate from date
     * @param toDate   to date
     * @param pattern  날짜 패턴
     * @return 두 날짜의 차이 (Millis)
     * @throws ParseException {@link java.text.ParseException}
     */
    public static long diffTimes(String fromDate, String toDate, String pattern) throws ParseException {
        return diffTimes(parse(fromDate, pattern), parse(toDate, pattern));
    }

    /**
     * 두 날짜의 차이가 몇일인지 가져온다.
     *
     * @param fromDate from date
     * @param toDate   to date
     * @return 두 날짜의 차이 (Days)
     */
    public static long diffDays(Date fromDate, Date toDate) {
        return diffTimes(fromDate, toDate) / 86400000/*(24 * 60 * 60 * 1000)*/;
    }

    /**
     * 두 날짜의 차이가 몇일인지 가져온다.
     *
     * @param fromDate from date
     * @param toDate   to date
     * @param pattern  날짜 패턴
     * @return 두 날짜의 차이 (Days)
     * @throws ParseException {@link java.text.ParseException}
     */
    public static long diffDays(String fromDate, String toDate, String pattern) throws ParseException {
        return diffTimes(fromDate, toDate, pattern) / 86400000/*(24 * 60 * 60 * 1000)*/;
    }

    /**
     * 두 날짜의 차이가 몇시간인지 가져온다.
     *
     * @param fromDate from date
     * @param toDate   to date
     * @return 두 날짜의 차이 (Hours)
     */
    public static long diffHours(Date fromDate, Date toDate) {
        return diffTimes(fromDate, toDate) / 3600000/*(60 * 60 * 1000)*/;
    }

    /**
     * 두 날짜의 차이가 몇시간인지 가져온다.
     *
     * @param fromDate from date
     * @param toDate   to date
     * @param pattern  날짜 패턴
     * @return 두 날짜의 차이 (Hours)
     * @throws ParseException {@link java.text.ParseException}
     */
    public static long diffHours(String fromDate, String toDate, String pattern) throws ParseException {
        return diffTimes(fromDate, toDate, pattern) / 3600000/*(60 * 60 * 1000)*/;
    }

    /**
     * 두 날짜의 차이가 몇분인지 가져온다.
     *
     * @param fromDate from date
     * @param toDate   to date
     * @return 두 날짜의 차이 (Minutes)
     */
    public static long diffMinutes(Date fromDate, Date toDate) {
        return diffTimes(fromDate, toDate) / 60000/*(60 * 1000)*/;
    }

    /**
     * 두 날짜의 차이가 몇분인지 가져온다.
     *
     * @param fromDate from date
     * @param toDate   to date
     * @param pattern  날짜 패턴
     * @return 두 날짜의 차이 (Minutes)
     * @throws ParseException {@link java.text.ParseException}
     */
    public static long diffMinutes(String fromDate, String toDate, String pattern) throws ParseException {
        return diffTimes(fromDate, toDate, pattern) / 60000/*(60 * 1000)*/;
    }

    /**
     * 두 날짜의 차이가 몇초인지 가져온다.
     *
     * @param fromDate from date
     * @param toDate   to date
     * @return 두 날짜의 차이 (Seconds)
     */
    public static long diffSeconds(Date fromDate, Date toDate) {
        return diffTimes(fromDate, toDate) / 1000;
    }

    /**
     * 두 날짜의 차이가 몇초인지 가져온다.
     *
     * @param fromDate from date
     * @param toDate   to date
     * @param pattern  날짜 패턴
     * @return 두 날짜의 차이 (Seconds)
     * @throws ParseException {@link java.text.ParseException}
     */
    public static long diffSeconds(String fromDate, String toDate, String pattern) throws ParseException {
        return diffTimes(fromDate, toDate, pattern) / 1000;
    }

    /**
     * 만료일을 구한다.
     *
     * @param date 날짜
     * @return 해당 날짜의 23:59:59 반환
     */
    public static Date getExpirationDate(Date date) {
        if (date == null) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    // 테스트
    public static void main(String[] args) throws Exception {
        System.out.println(format("yyyy-MM-dd"));
        System.out.println(parse("yyyy-MM-dd"));
        System.out.println(truncateDate(Calendar.getInstance()));
        System.out.println(diffTimes("2007-10-01", "2007-10-02", "yyyy-MM-dd"));
        System.out.println(diffDays("2007-10-01", "2007-10-02", "yyyy-MM-dd"));
        System.out.println(diffHours("2007-10-01", "2007-10-02", "yyyy-MM-dd"));
        System.out.println(diffMinutes("2007-10-01", "2007-10-02", "yyyy-MM-dd"));
        System.out.println(diffSeconds("2007-10-01", "2007-10-02", "yyyy-MM-dd"));
    }
}