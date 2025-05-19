package com.koyta.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.koyta.config.security.CustomUserDetails;
import com.koyta.dto.EmailRequest;
import com.koyta.dto.LoginRequest;
import com.koyta.dto.LoginResponse;
import com.koyta.dto.UserRequest;
import com.koyta.dto.UserResponse;
import com.koyta.entity.AccountStatus;
import com.koyta.entity.Role;
import com.koyta.entity.User;
import com.koyta.exception.AuthenticationFailedException;
import com.koyta.exception.UserRegistrationException;
import com.koyta.repository.RoleRepository;
import com.koyta.repository.UserRepository;
import com.koyta.service.AuthService;
import com.koyta.service.JwtService;
import com.koyta.util.AppConstants;
import com.koyta.util.CommonUtil;
import com.koyta.util.Validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private Validation validation;

	private ModelMapper modelMapper;

	private EmailService emailService;

	private AuthenticationManager authenticationManager;

	private PasswordEncoder passwordEncoder;

	private JwtService jwtService;

	public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, Validation validation,
			ModelMapper modelMapper, EmailService emailService, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder, JwtService jwtService) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.validation = validation;
		this.modelMapper = modelMapper;
		this.emailService = emailService;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@Override
	public Boolean register(UserRequest userDto, HttpServletRequest request) throws UserRegistrationException {
		
		log.info("AuthServiceImpl : register() : Start");
		String url = CommonUtil.getUrl(request);

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

		if (ObjectUtils.isEmpty(save)) {
			log.error("User registration failed: user not saved");
	        throw new UserRegistrationException("User could not be saved. Please try again later.");
		}
		
		// send Email
		try {
			emailSendForRegister(save, url);
			log.info("Email sent successfully to user: {}", save.getEmail());
		} catch (Exception e) {
			log.error("Email sending failed for user {}: {}", save.getEmail(), e.getMessage());
			throw new UserRegistrationException("Registration succeeded, but verification email failed to send.");
		}
		
		log.info("User registered successfully: {}", save.getEmail());
		log.info("AuthServiceImpl : register() : End");
		return true;

	}

	private void emailSendForRegister(User saveUser, String url) throws Exception {
		
		String baseUrl = AppConstants.getVerificationUrl(url);
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
				.title("Koyta Team")
				.subject("Verify your account")
				.message(message)
				.build();
		
		emailService.sendEmail(emailRequest);

	}

	private void setRoles(UserRequest userDto, User user) {

		List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();

		List<Role> roles = roleRepository.findAllById(reqRoleId);

		user.setRoles(roles);

	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) throws AuthenticationFailedException {

		try {

			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			if (!authenticate.isAuthenticated()) {
				throw new AuthenticationFailedException("Invalid email or password");
			}

			CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();
			
			String token = jwtService.generateToken(customUserDetails.getUser());
			
			return LoginResponse.builder()
					.user(modelMapper.map(customUserDetails.getUser(), UserResponse.class))
					.token(token)
					.build();
			
		} catch (BadCredentialsException ex) {
			throw new AuthenticationFailedException("Invalid email or password");
		} catch (DisabledException ex) {
			throw new AuthenticationFailedException("Account is disabled. Please verify your email.");
		} catch (Exception ex) {
			throw new AuthenticationFailedException("Login failed due to an unexpected error.");
		}

	}

}