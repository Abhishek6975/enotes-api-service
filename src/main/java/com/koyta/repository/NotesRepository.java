package com.koyta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koyta.entity.Notes;

public interface NotesRepository extends JpaRepository<Notes, Integer> {
	

}
