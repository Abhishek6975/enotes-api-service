package com.koyta.service.Impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.koyta.config.security.CustomUserDetails;
import com.koyta.dto.EmailRequest;
import com.koyta.dto.LoginRequest;
import com.koyta.dto.LoginResponse;
import com.koyta.dto.UserDto;
import com.koyta.entity.AccountStatus;
import com.koyta.entity.Role;
import com.koyta.entity.User;
import com.koyta.repository.RoleRepository;
import com.koyta.repository.UserRepository;
import com.koyta.service.UserService;
import com.koyta.util.AppConstants;
import com.koyta.util.Validation;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private Validation validation;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Boolean register(UserDto userDto, String url) throws Exception {

		validation.userValidation(userDto);

		User user = modelMapper.map(userDto, User.class);

		setRoles(userDto, user);
		
		AccountStatus accountStatus = AccountStatus.builder()
				.isActive(false)
				.verificationCode(UUID.randomUUID().toString())
				.build();

		user.setStatus(accountStatus);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		User save = userRepository.save(user);

		if (!ObjectUtils.isEmpty(save)) {

			// send Email

			emailSend(save, url);

			return true;
		}

		return false;

	}

	private void emailSend(User saveUser, String url) throws Exception {
		
		String baseUrl = AppConstants.getBaseUrl(url);
		String verificationUrl = baseUrl + "?id="
				+ URLEncoder.encode(String.valueOf(saveUser.getId()), StandardCharsets.UTF_8) + "&vc="
				+ URLEncoder.encode(saveUser.getStatus().getVerificationCode(), StandardCharsets.UTF_8);

		String message = "Hi,<b>" + saveUser.getFirstName() + "</b> "
				+ "<br> Your Account Registration Sucessfully. <br>"
				+ "<br> Click the below link verify & Active your account <br>"
				+ "<a href='" + verificationUrl + "'> Click Here </a> <br><br>" 
				+ "Thanks,<br>Koyta.com";

		EmailRequest emailRequest = EmailRequest.builder()
				.to(saveUser.getEmail())
				.title("Account Confirmation")
				.subject("Account Created Success")
				.message(message)
				.build();
		
		emailService.sendEmail(emailRequest);

	}

	private void setRoles(UserDto userDto, User user) {

		List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();

		List<Role> roles = roleRepository.findAllById(reqRoleId);

		user.setRoles(roles);

	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) {

		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		
		
		if(authenticate.isAuthenticated()) {
			
			CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();
			
			String token = "akdsdluelqgecbsurklagdqnmdqadkadmdzcmqwulkcnzzxvcnmzlapgwvan";
			LoginResponse loginResponse = LoginResponse.builder()
					.user(modelMapper.map(customUserDetails.getUser(), UserDto.class))
					.token(token)
					.build();
			
			return loginResponse;
		}

		return null;
	}

}