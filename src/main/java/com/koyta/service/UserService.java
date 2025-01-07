package com.koyta.service;

import com.koyta.dto.PasswordChngRequest;

public interface UserService {

	public void changePassword(PasswordChngRequest passwordRequest) throws Exception;
}
