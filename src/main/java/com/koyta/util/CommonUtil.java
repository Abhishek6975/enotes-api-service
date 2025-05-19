package com.koyta.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import com.koyta.config.security.CustomUserDetails;
import com.koyta.entity.User;
import com.koyta.handler.GenericResponse;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtil {

	private CommonUtil() {}

	public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status) {

		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status(AppConstants.STATUS_SUCCESS)
				.message("message")
				.data(data)
				.build();

		return response.create();

	}

	public static ResponseEntity<?> createBuildResponseMessage(String message, HttpStatus status) {

		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status(AppConstants.STATUS_SUCCESS)
				.message(message)
				.build();

		return response.create();

	}

	public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status) {

		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status(AppConstants.STATUS_FAILED)
				.message(AppConstants.STATUS_FAILED)
				.data(data)
				.build();

		return response.create();

	}

	public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {

		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status(AppConstants.STATUS_FAILED)
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

	public static String getUrl(HttpServletRequest request) {

		String apiUrl = request.getRequestURL().toString(); // http://localhost:8080/api/v1/auth/

//		String string = request.getServletPath().toString(); // /api/v1/auth/

		apiUrl = apiUrl.replace(request.getServletPath(), ""); // http://localhost:8080

		return apiUrl;
	}

	public static User getLoggedInUser() {
		try {
			CustomUserDetails logUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			return logUser.getUser();

		} catch (Exception e) {

			throw new IllegalStateException("Failed to get logged in user from security context", e);
		}

	}

}
