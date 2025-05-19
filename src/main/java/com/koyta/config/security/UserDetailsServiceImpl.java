package com.koyta.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.koyta.entity.User;
import com.koyta.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("invalid email"));
		
		return new CustomUserDetails(user);
	
	}

}
