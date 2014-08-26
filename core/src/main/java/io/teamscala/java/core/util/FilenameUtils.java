package io.teamscala.java.core.util;

import org.springframework.util.Assert;

import java.io.File;

/**
 * 파일이름 유틸리티 클래스.
 * 
 */
public abstract class FilenameUtils extends org.apache.commons.io.FilenameUtils {

    /**
     * 중복되지 않은 파일 명을 가져온다.
     *
     * @param dir 대상 디렉토리
     * @param filename 업로드 할 파일명
     * @return 중복되지 않는 파일명(1,2,3..).확장자
     */
    public static String getNonComflictFilename(String dir, String filename) {
        return getNonComflictFilename(new File(dir), filename);
    }

    /**
     * 중복되지 않은 파일 명을 가져온다.
     *
     * @param dir 대상 디렉토리
     * @param filename 업로드 할 파일명
     * @return 중복되지 않는 파일명(1,2,3..).확장자
     */
    public static String getNonComflictFilename(File dir, String filename) {
        Assert.notNull(dir);
        Assert.notNull(filename);

        String baseName = FilenameUtils.getBaseName(filename);
        String extension = FilenameUtils.getExtension(filename);

        File file = new File(dir, filename);
        int count = 1;
        while (file.exists()) {
            file = new File(dir, String.format("%s(%d).%s", baseName, count++, extension));
        }
        return file.getName();
    }
}
