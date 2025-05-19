package com.koyta.service;

import com.koyta.dto.LoginRequest;
import com.koyta.dto.LoginResponse;
import com.koyta.dto.UserRequest;
import com.koyta.exception.AuthenticationFailedException;
import com.koyta.exception.UserRegistrationException;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

	public Boolean register(UserRequest userDto, HttpServletRequest httpServletRequest) throws UserRegistrationException;

	public LoginResponse login(LoginRequest loginRequest) throws AuthenticationFailedException ;

}
