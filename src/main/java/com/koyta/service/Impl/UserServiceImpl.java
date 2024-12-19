package com.koyta.service.Impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

	@Override
	public Boolean register(UserDto userDto) throws Exception {

		validation.userValidation(userDto);

		User user = modelMapper.map(userDto, User.class);

		setRoles(userDto, user);

		User save = userRepository.save(user);

		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}

		return false;

	}

	private void setRoles(UserDto userDto, User user) {

		List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();

		List<Role> roles = roleRepository.findAllById(reqRoleId);

		user.setRoles(roles);

	}

}