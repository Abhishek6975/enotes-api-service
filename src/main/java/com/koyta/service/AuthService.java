package com.koyta.service;

import com.koyta.dto.LoginRequest;
import com.koyta.dto.LoginResponse;
import com.koyta.dto.UserRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

	public Boolean register(UserRequest userDto, HttpServletRequest httpServletRequest) throws Exception;

	public LoginResponse login(LoginRequest loginRequest);

}
