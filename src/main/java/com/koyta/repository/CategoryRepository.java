package com.koyta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.koyta.entity.Category;

@Repository
@EnableJpaRepositories
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	public List<Category> findByIsActiveTrue();

	public Optional<Category> findByIdAndIsDeletedFalse(Integer id);

	public List<Category> findByIsDeletedFalse();

	public List<Category> findByIsActiveTrueAndIsDeletedFalse();

	public Boolean existsByName(String name);

}
