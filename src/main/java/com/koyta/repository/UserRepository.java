package com.koyta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koyta.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Boolean existsByEmail(String email);

}
