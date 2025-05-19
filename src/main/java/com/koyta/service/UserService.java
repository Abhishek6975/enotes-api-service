package com.koyta.service;

import com.koyta.dto.PasswordChngRequest;
import com.koyta.dto.PswdResetRequest;
import com.koyta.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

	public void changePassword(PasswordChngRequest passwordRequest) throws IllegalAccessException;

	public void sendEmailPasswordReset(String email, HttpServletRequest httpServletRequest) throws ResourceNotFoundException;

	public void verifyPswdRestLink(Integer id, String vc) throws ResourceNotFoundException;

	public void resetPassword(PswdResetRequest pswdResetRequest) throws ResourceNotFoundException;
}
