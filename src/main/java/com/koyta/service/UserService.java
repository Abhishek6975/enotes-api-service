package com.koyta.service;

import com.koyta.dto.PasswordChngRequest;
import com.koyta.dto.PswdResetRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

	public void changePassword(PasswordChngRequest passwordRequest) throws Exception;

	public void sendEmailPasswordReset(String email, HttpServletRequest httpServletRequest) throws Exception;

	public void verifyPswdRestLink(Integer id, String vc) throws Exception;

	public void resetPassword(PswdResetRequest pswdResetRequest) throws Exception;
}
