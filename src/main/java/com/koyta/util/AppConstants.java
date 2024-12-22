package com.koyta.util;

public class AppConstants {

	public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
	public static final String MOBILENO_REGEX = "^[7-9][0-9]{9}$";
	
	public static final String VERIFY_PATH = "/api/v1/home/verify";
	
	 // Method to generate the base URL
    public static String getBaseUrl(String url) {
        return url + VERIFY_PATH;
    }

}
