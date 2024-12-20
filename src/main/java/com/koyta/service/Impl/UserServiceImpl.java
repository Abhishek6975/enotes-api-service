package com.koyta.service.Impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.koyta.dto.EmailRequest;
import com.koyta.dto.UserDto;
import com.koyta.entity.Role;
import com.koyta.entity.User;
import com.koyta.repository.RoleRepository;
import com.koyta.repository.UserRepository;
import com.koyta.service.UserService;
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

	@Override
	public Boolean register(UserDto userDto) throws Exception {

		validation.userValidation(userDto);

		User user = modelMapper.map(userDto, User.class);

		setRoles(userDto, user);

		User save = userRepository.save(user);

		if (!ObjectUtils.isEmpty(save)) {

			// send Email

			emailSend(save);

			return true;
		}

		return false;

	}

	private void emailSend(User saveUser) throws Exception {

		String message = "Hi,<b>" + saveUser.getFirstName() + "</b> "
				+ "<br> Your Account Registration Sucessfully. <br>"
				+ "<br> Click the below link verify & Active your account <br>" + "<a href='#'> Click Here </a> <br><br>"
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

}