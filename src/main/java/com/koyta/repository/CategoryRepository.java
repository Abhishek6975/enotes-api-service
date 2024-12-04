package com.koyta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koyta.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	
}
