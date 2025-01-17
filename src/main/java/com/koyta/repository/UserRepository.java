package com.koyta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koyta.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Boolean existsByEmail(String email);

	Optional<User> findByEmail(String username);
	
	

}
