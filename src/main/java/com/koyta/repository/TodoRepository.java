package com.koyta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.koyta.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer>{

	List<Todo> findByCreatedBy(Integer userId);

}
