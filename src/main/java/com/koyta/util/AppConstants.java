package com.koyta.util;

public final class AppConstants {

	public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
	
	public static final String MOBILENO_REGEX = "^[7-9][0-9]{9}$";

	public static final String VERIFY_PATH = "/api/v1/home/verify";

	public static final String PSWD_REST_PATH = "/api/v1/home/verify-pswd-link";

	private AppConstants() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	// Method to generate the verification URL
	public static String getVerificationUrl(String url) {
		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("Base URL cannot be null or empty");
		}
		return url + VERIFY_PATH;
	}

	// Method to generate the password reset URL
	public static String getPasswordResetUrl(String url) {
		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("Base URL cannot be null or empty");
		}
		return url + PSWD_REST_PATH;
	}

}
