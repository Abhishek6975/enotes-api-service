package com.koyta.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.koyta.handler.GenericResponse;

public class CommonUtil {

	public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status) {

		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("succes")
				.message("message")
				.data(data)
				.build();

		return response.create();

	}
	
	public static ResponseEntity<?> createBuildResponseMessage(String message, HttpStatus status) {

		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("succes")
				.message(message)
				.build();

		return response.create();

	}
	
	public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status) {

		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("failed")
				.message("failed")
				.data(data)
				.build();

		return response.create();

	}
	
	public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {

		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("failed")
				.message(message)
				.build();

		return response.create();

	}

	public static String getContentType(String originalFileName) {

		String extension = FilenameUtils.getExtension(originalFileName);

		switch (extension) {
		case "pdf":
			return "application/pdf";
		case "xlsx":
			return "application/sheet";
		case "txt":
			return "text/plan";
		case "png":
			return "image/png";
		case "jpeg":
			return "image/jpeg";
		case "java":
			return "application/java";
		default:
			return "application/octet-strem";
		}

	}
	

}
