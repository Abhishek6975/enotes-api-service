package com.koyta.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.koyta.entity.User;

public interface JwtService {

	public String generateToken(User user);

	public String extractUserName(String token);

	public Boolean validateToken(String token, UserDetails userDetails);

}
