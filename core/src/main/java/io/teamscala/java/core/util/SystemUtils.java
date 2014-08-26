package io.teamscala.java.core.util;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.UnsupportedCharsetException;
import java.util.UUID;

/**
 * 시스템 관련 유틸리티 클래스.
 
 */
public abstract class SystemUtils extends org.apache.commons.lang3.SystemUtils {

    /**
     * 시스템 콘솔에 명령을 실행하고 결과를 가져온다
     *
     * @param cmd 명령어
     * @return 실행결과
     * @throws IOException {@link java.io.IOException}
     */
    public static String execute(String cmd) throws IOException {
        Process process = Runtime.getRuntime().exec(cmd);
        InputStream in = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, getCharset(in)));

        StringBuilder result = new StringBuilder(100);
        String temp;
        while ((temp = reader.readLine()) != null) {
            result.append(temp).append("\n");
        }

        reader.close();
        in.close();

        return result.toString();
    }

    public static String getCharset(InputStream in) throws IOException {
        CharsetDetector cd = new CharsetDetector().setText(in);
        CharsetMatch cm = cd.detect();

        if (cm != null) return cm.getName();

        throw new UnsupportedCharsetException("");
    }

    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];

        for (int i = 0; i < 8; i++) {
            buffer[i] = (byte) (msb >>> 8 * (7 - i));
        }
        for (int i = 8; i < 16; i++) {
            buffer[i] = (byte) (lsb >>> 8 * (7 - i));
        }
        return new String(Hex.encodeHex(buffer));
    }
}
