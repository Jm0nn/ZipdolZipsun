package com.ssafy.zip.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class Encryption {
	
	private static final int SALT_SIZE = 16;
	private static final int LEN = 8;

	// 비밀번호 해싱
	public static String hashing(byte[] password, String salt) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		for (int i = 0; i < 1000; ++i) {
			String tmp = byteToString(password) + salt; // 패스워드와 salt를 합쳐 새로운 문자열 생성
			md.update(tmp.getBytes()); // tmp의 문자열을 해싱하여 md에 저장
			password = md.digest(); // md 객체의 다이제스트를 얻어 password를 갱신
		}

		return byteToString(password);
	}

	// salt 값 생성
	public static String getSalt() {
		SecureRandom rnd = new SecureRandom();
		byte[] tmp = new byte[SALT_SIZE];
		rnd.nextBytes(tmp);

		return byteToString(tmp);
	}

	// byte 값을 16진수로 변경
	public static String byteToString(byte[] tmp) {
		StringBuilder sb = new StringBuilder();
		for (byte a : tmp) {
			sb.append(String.format("%02x", a));
		}
		return sb.toString();
	}

	// 랜덤 비밀번호 생성
	public static String randomPassword() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();

		// 루프의 각 반복은 주어진 문자에서 무작위로 문자를 선택
		// ASCII 범위를 `StringBuilder` 인스턴스에 추가
		// 8자리 랜덤 비밀번호를 생성
		for (int i = 0; i < LEN; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}

		return sb.toString();
	}

}
