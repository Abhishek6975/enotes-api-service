package com.koyta.service.Impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.koyta.dto.NotesDto;
import com.koyta.dto.NotesDto.CategoryDto;
import com.koyta.entity.Notes;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.repository.CategoryRepository;
import com.koyta.repository.NotesRepository;
import com.koyta.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Boolean saveNotes(NotesDto notesDto) throws Exception {

		// Validation Notes Checking
		checkCategoryExist(notesDto.getCategory());

		Notes notes = modelMapper.map(notesDto, Notes.class);

		Notes saveNotes = notesRepository.save(notes);

		if (!ObjectUtils.isEmpty(saveNotes)) {

			return true;
		}

		return false;

	}

	private void checkCategoryExist(CategoryDto category) throws Exception {

		categoryRepository.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Category id invalid"));

	}

	@Override
	public List<NotesDto> getAllNotes() {

		return notesRepository.findAll().stream().map(note -> modelMapper.map(note, NotesDto.class)).toList();

	}

}
