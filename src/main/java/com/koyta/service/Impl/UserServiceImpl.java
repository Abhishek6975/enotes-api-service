package com.koyta.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.koyta.dto.PasswordChngRequest;
import com.koyta.entity.User;
import com.koyta.repository.UserRepository;
import com.koyta.service.UserService;
import com.koyta.util.CommonUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void changePassword(PasswordChngRequest passwordRequest) throws Exception {

		User loggedInUser = CommonUtil.getLoggedInUser();

		if (!passwordEncoder.matches(passwordRequest.getOldPassword(), loggedInUser.getPassword())) {
			throw new IllegalAccessException("Old Password Is Incorrect");
		}

		String encodePswd = passwordEncoder.encode(passwordRequest.getNewPassword());

		loggedInUser.setPassword(encodePswd);

		userRepository.save(loggedInUser);

	}

}
