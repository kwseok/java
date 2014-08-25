package io.teamscala.java.core.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 암호화 유틸리티 클래스.
 *
 * @author 석기원
 */
public abstract class EncryptUtils {

	/**
	 * MD5.
	 *
	 * @param input 입력 문자열
	 * @return 암호화된 문자열
	 * @throws NoSuchAlgorithmException {@link java.security.NoSuchAlgorithmException}
	 */
	public static String md5(String input) throws NoSuchAlgorithmException { return encrypt(input, "MD5"); }

	/**
	 * SHA.
	 *
	 * @param input 입력
	 * @return 암호화된 문자열
     * @throws NoSuchAlgorithmException {@link java.security.NoSuchAlgorithmException}
	 */
	public static String sha(String input) throws NoSuchAlgorithmException { return encrypt(input, "SHA"); }

	/**
	 * SHA-256.
	 *
	 * @param input 입력
	 * @return 암호화된 문자열
     * @throws NoSuchAlgorithmException {@link java.security.NoSuchAlgorithmException}
	 */
	public static String sha256(String input) throws NoSuchAlgorithmException { return encrypt(input, "SHA-256"); }

	/**
	 * SHA-384.
	 *
	 * @param input 입력
	 * @return 암호화된 문자열
     * @throws NoSuchAlgorithmException {@link java.security.NoSuchAlgorithmException}
	 */
	public static String sha384(String input) throws NoSuchAlgorithmException { return encrypt(input, "SHA-384"); }

	/**
	 * SHA-512.
	 *
	 * @param input 입력
	 * @return 암호화된 문자열
     * @throws NoSuchAlgorithmException {@link java.security.NoSuchAlgorithmException}
	 */
	public static String sha512(String input) throws NoSuchAlgorithmException { return encrypt(input, "SHA-512"); }

	/**
	 * 암호화.
	 *
	 * @param input 입력 문자열
	 * @param algorithm 알고리즘
	 * @return 암호화된 문자열
     * @throws NoSuchAlgorithmException {@link java.security.NoSuchAlgorithmException}
	 */
	public static String encrypt(String input, String algorithm) throws NoSuchAlgorithmException {
		if (input != null && !input.isEmpty()) {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(input.getBytes());
			input = new String(Hex.encodeHex(md.digest()));
		}
		return input;
	}

	// 테스트
	public static void main(String[] args) throws Exception {
		System.out.println("["+ md5("koala") +"]");
		System.out.println("["+ sha("koala") +"]");
		System.out.println("["+ sha256("koala") +"]");
		System.out.println("["+ sha384("koala") +"]");
		System.out.println("["+ sha512("koala") +"]");
	}

}
