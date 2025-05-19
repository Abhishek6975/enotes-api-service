package com.koyta.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.koyta.dto.EmailRequest;
import com.koyta.dto.PasswordChngRequest;
import com.koyta.dto.PswdResetRequest;
import com.koyta.entity.User;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.repository.UserRepository;
import com.koyta.service.UserService;
import com.koyta.util.AppConstants;
import com.koyta.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

	private PasswordEncoder passwordEncoder;

	private UserRepository userRepository;

	private EmailService emailService;

	public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, EmailService emailService) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.emailService = emailService;
	}

	@Override
	public void changePassword(PasswordChngRequest passwordRequest) throws IllegalAccessException {

		User loggedInUser = CommonUtil.getLoggedInUser();

		if (!passwordEncoder.matches(passwordRequest.getOldPassword(), loggedInUser.getPassword())) {
			throw new IllegalAccessException("Old Password Is Incorrect");
		}

		String encodePswd = passwordEncoder.encode(passwordRequest.getNewPassword());

		loggedInUser.setPassword(encodePswd);

		userRepository.save(loggedInUser);

	}

	@Override
	public void sendEmailPasswordReset(String email, HttpServletRequest request) throws ResourceNotFoundException {

		String url = CommonUtil.getUrl(request);

		String pswdRestToken = UUID.randomUUID().toString();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("invalid Email"));
		
		user.getStatus().setPasswordRestToken(pswdRestToken);

		User updateUser = userRepository.save(user);

		sendEmailRequest(updateUser, url);
		
		

	}

	private void sendEmailRequest(User user , String url) {

		String baseUrl = AppConstants.getPasswordResetUrl(url);
		
		String verificationUrl = baseUrl + "?id="
				+ URLEncoder.encode(String.valueOf(user.getId()), StandardCharsets.UTF_8) + "&vc="
				+ URLEncoder.encode(user.getStatus().getPasswordRestToken(), StandardCharsets.UTF_8);

		String message = "Hi,<b>" + user.getFirstName() + "</b> "
				+ "<br> We heard that you lost your Koyta password. Sorry about that! <br>"
				+ "<br> But don’t worry! You can use the following button to reset your password: <br>" 
				+ "<a href=' "+ verificationUrl + " '> Click Here </a> <br><br>"
				+ "Thanks,<br>The koyta Team";

		EmailRequest emailRequest = EmailRequest.builder()
				.to(user.getEmail())
				.title("Koyta Team")
				.subject("[Koyta] Please reset your password")
				.message(message)
				.build();

		emailService.sendEmail(emailRequest);

	}

	@Override
	public void verifyPswdRestLink(Integer id, String vc) throws ResourceNotFoundException {

		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("invalid user"));

		verifyPswdRestToken(user.getStatus().getPasswordRestToken(), vc);

	}

	private void verifyPswdRestToken(String existToken, String reqToken) {

		// request token not null
		if (StringUtils.hasText(reqToken)) {
			
			// password already reset
			if (!StringUtils.hasText(existToken)) {

				throw new IllegalArgumentException("Already Password Reset");
			}
			
			// user reqToken change
			if (!existToken.equals(reqToken)) {

				throw new IllegalArgumentException("invalid url");
			}

		} else {

			throw new IllegalArgumentException("invalid token");
		}

	}

	@Override
	public void resetPassword(PswdResetRequest pswdResetRequest) throws ResourceNotFoundException {

		User user = userRepository.findById(pswdResetRequest.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("invalid user"));

		String encodePswd = passwordEncoder.encode(pswdResetRequest.getNewPassword());
		user.setPassword(encodePswd);
		user.getStatus().setPasswordRestToken(null);
		userRepository.save(user);

	}

}
