package io.teamscala.java.core.util;

import org.springframework.util.Assert;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 정규식을 이용한 파일 이름 필터 클래스.
 */
public class RegexFilenameFilter implements java.io.FilenameFilter {

    // 파일이름 정규식 패턴
    private String pattern = null;

    /**
     * 생성자.
     *
     * @param pattern 파일이름 정규식 패턴
     */
    public RegexFilenameFilter(String pattern) {
        Assert.hasText(pattern, "Pattern must not be empty");
        this.pattern = pattern;
    }

    /**
     * 파일 이름을 검사하는 메서드.
     *
     * @param dir  디렉토리
     * @param name 파일이름
     * @return true or false
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    @Override
    public boolean accept(File dir, String name) {
        return Pattern.matches(this.pattern, name);
    }
}
