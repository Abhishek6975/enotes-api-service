package com.koyta.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.koyta.dto.NotesDto;
import com.koyta.dto.NotesResponse;
import com.koyta.entity.FilesDetails;
import com.koyta.exception.ResourceNotFoundException;

public interface NotesService {
	
	public Boolean saveNotes(String notes , MultipartFile file) throws Exception;
	
	public List<NotesDto> getAllNotes();

	public byte[] downloadFile(FilesDetails fileDetails) throws ResourceNotFoundException, IOException;

	public FilesDetails getFileDetails(Integer id) throws Exception;

	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

	public void softDeleteNotes(Integer id) throws Exception;

	public void restoreNotes(Integer id) throws Exception;

	public List<NotesDto> getUserRecycleBinNotes(Integer userId);

	public void hardDeleteNotes(Integer id) throws Exception;

	public void emptyRecycleBin(Integer userId);

}
