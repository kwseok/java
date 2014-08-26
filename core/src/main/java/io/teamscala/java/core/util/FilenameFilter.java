package io.teamscala.java.core.util;

import java.io.File;

/**
 * 파일이름 필터 클래스.
 *
 */
public class FilenameFilter implements java.io.FilenameFilter {

    private String prefix; // 접두사
    private String suffix; // 접미사

    /**
     * 생성자.
     *
     * @param suffix 파일이름의 접미사
     */
    public FilenameFilter(String suffix) { this.suffix = suffix; }

    /**
     * 생성자.
     *
     * @param prefix 파일이름의 접두사
     * @param suffix 파일이름의 접미사
     */
    public FilenameFilter(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * 파일 이름을 검사하는 메서드.
     *
     * @param dir 디렉토리
     * @param name 파일이름
     * @return true or false
     *
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    @Override
    public boolean accept(File dir, String name) {
        return !(prefix != null && !name.startsWith(prefix)) &&
                !(suffix != null && !name.endsWith(suffix));
    }
}
