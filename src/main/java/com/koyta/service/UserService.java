package com.koyta.service;

import com.koyta.dto.UserDto;

public interface UserService {
	
	public Boolean register(UserDto userDto,String url) throws Exception;
	
	

}
