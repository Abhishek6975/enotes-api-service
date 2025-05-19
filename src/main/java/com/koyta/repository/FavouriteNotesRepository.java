package com.koyta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koyta.entity.FavouriteNotes;

public interface FavouriteNotesRepository extends JpaRepository<FavouriteNotes, Integer>{

	List<FavouriteNotes> findByUserId(Integer userId);

}
