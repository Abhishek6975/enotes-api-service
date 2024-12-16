package com.koyta.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.koyta.dto.FavouriteNotesDto;
import com.koyta.dto.NotesDto;
import com.koyta.dto.NotesResponse;
import com.koyta.entity.FavouriteNotes;
import com.koyta.entity.FilesDetails;
import com.koyta.exception.ResourceNotFoundException;

public interface NotesService {

	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;

	public List<NotesDto> getAllNotes();

	public byte[] downloadFile(FilesDetails fileDetails) throws ResourceNotFoundException, IOException;

	public FilesDetails getFileDetails(Integer id) throws Exception;

	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

	public void softDeleteNotes(Integer id) throws Exception;

	public void restoreNotes(Integer id) throws Exception;

	public List<NotesDto> getUserRecycleBinNotes(Integer userId);

	public void hardDeleteNotes(Integer id) throws Exception;

	public void emptyRecycleBin(Integer userId);

	public void favouriteNotes(Integer noteId) throws Exception;
	
	public void unFavouriteNotes(Integer noteId) throws Exception;
	
	public List<FavouriteNotesDto> getUserFavouriteNotes();

	public Boolean copyNotes(Integer id) throws Exception; 
	
	

}
