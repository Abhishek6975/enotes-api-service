package com.koyta.service;

import com.koyta.entity.User;

public interface JwtService {
	
	public String generateToken(User user);

}
