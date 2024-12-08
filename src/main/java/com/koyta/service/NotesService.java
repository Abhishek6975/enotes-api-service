package com.koyta.service;

import java.util.List;

import com.koyta.dto.NotesDto;

public interface NotesService {
	
	public Boolean saveNotes(NotesDto notesDto) throws Exception;
	
	public List<NotesDto> getAllNotes();

}
