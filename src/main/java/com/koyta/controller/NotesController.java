package com.koyta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.NotesDto;
import com.koyta.service.NotesService;
import com.koyta.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

	@Autowired
	private NotesService notesService;

	@PostMapping("/")
	public ResponseEntity<?> saveNotes(@RequestBody NotesDto notesDto) throws Exception {

		Boolean saveNotes = notesService.saveNotes(notesDto);

		if (saveNotes) {
			return CommonUtil.createBuildResponseMessage("Note's Saved Succes", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Notes Not Saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllNotes() {

		List<NotesDto> allNotesDto = notesService.getAllNotes();

		if (CollectionUtils.isEmpty(allNotesDto)) {
			return ResponseEntity.noContent().build();
		} else {

			return CommonUtil.createBuildResponse(allNotesDto, HttpStatus.OK);
		}

	}

}
