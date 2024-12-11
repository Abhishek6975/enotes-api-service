package com.koyta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.koyta.dto.NotesDto;
import com.koyta.entity.FilesDetails;
import com.koyta.service.NotesService;
import com.koyta.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

	@Autowired
	private NotesService notesService;

	@PostMapping("/")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception {

		Boolean saveNotes = notesService.saveNotes(notes, file);

		if (saveNotes) {
			return CommonUtil.createBuildResponseMessage("Note's Saved Succes", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Notes Not Saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable("id") Integer id) throws Exception {

		FilesDetails fileDetails = notesService.getFileDetails(id);

		byte[] data = notesService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));

		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return ResponseEntity.ok().headers(headers).body(data);

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
