package com.koyta.service;

import com.koyta.exception.ResourceNotFoundException;

public interface HomeService {

	public Boolean verifyAccount(Integer userId, String verificationCode) throws ResourceNotFoundException;
}
