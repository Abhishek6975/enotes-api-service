package com.koyta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koyta.entity.FilesDetails;

public interface FileRepository  extends JpaRepository<FilesDetails, Integer>{
	

}
