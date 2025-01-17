package com.koyta.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.koyta.entity.AccountStatus;
import com.koyta.entity.User;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.exception.SuccessException;
import com.koyta.repository.UserRepository;
import com.koyta.service.HomeService;

@Component
public class HomeServiceImpl implements HomeService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception {

		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("invalid user"));

		if (user.getStatus().getVerificationCode() == null) {
			throw new SuccessException("Account already verified");
		}

		if (user.getStatus().getVerificationCode().equals(verificationCode)) {
			AccountStatus status = user.getStatus();
			status.setIsActive(true);
			status.setVerificationCode(null);

			userRepository.save(user);

			return true;
		}

		return false;
	}

}
