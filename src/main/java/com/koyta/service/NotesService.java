package com.koyta.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.koyta.dto.FavouriteNotesDto;
import com.koyta.dto.NotesDto;
import com.koyta.dto.NotesResponse;
import com.koyta.entity.FilesDetails;
import com.koyta.exception.NoteSaveException;
import com.koyta.exception.ResourceNotFoundException;

public interface NotesService {

	public Boolean saveNotes(String notes, MultipartFile file) throws ResourceNotFoundException, NoteSaveException;

	public List<NotesDto> getAllNotes();

	public byte[] downloadFile(FilesDetails fileDetails) throws ResourceNotFoundException, IOException;

	public FilesDetails getFileDetails(Integer id) throws ResourceNotFoundException;

	public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize);

	public NotesResponse getNotesByUserSearch(Integer pageNo, Integer pageSize, String keyword);

	public void softDeleteNotes(Integer id) throws ResourceNotFoundException;

	public void restoreNotes(Integer id) throws ResourceNotFoundException;

	public List<NotesDto> getUserRecycleBinNotes() throws ResourceNotFoundException;

	public void hardDeleteNotes(Integer id) throws ResourceNotFoundException;

	public void emptyRecycleBin() throws ResourceNotFoundException;

	public void favouriteNotes(Integer noteId) throws ResourceNotFoundException;

	public void unFavouriteNotes(Integer noteId) throws ResourceNotFoundException;

	public List<FavouriteNotesDto> getUserFavouriteNotes();

	public Boolean copyNotes(Integer id) throws ResourceNotFoundException;

}
