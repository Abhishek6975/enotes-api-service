package com.koyta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.koyta.dto.FavouriteNotesDto;
import com.koyta.dto.NotesDto;
import com.koyta.dto.NotesResponse;
import com.koyta.entity.FilesDetails;
import com.koyta.service.NotesService;
import com.koyta.util.CommonUtil;

import lombok.Getter;

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
		}

		return CommonUtil.createBuildResponse(allNotesDto, HttpStatus.OK);

	}

	@GetMapping("/user-notes")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize) {

		Integer userId = 1;

		NotesResponse notesDtos = notesService.getAllNotesByUser(userId, pageNo, pageSize);

		/*
		 * if (CollectionUtils.isEmpty(notesDtos)) { return
		 * ResponseEntity.noContent().build(); }
		 */

		return CommonUtil.createBuildResponse(notesDtos, HttpStatus.OK);
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteNotes(@PathVariable("id") Integer id) throws Exception {
		
		notesService.softDeleteNotes(id);
		
		return CommonUtil.createBuildResponseMessage("delete Success", HttpStatus.OK);
		
	}
	
	@GetMapping("/restore/{id}")
	public ResponseEntity<?> restoreNotes(@PathVariable("id") Integer id) throws Exception {
		
		notesService.restoreNotes(id);
		
		return CommonUtil.createBuildResponseMessage("Notes Restore Success", HttpStatus.OK);		
	}
	
	
	// Get All Deleted User's Notes Available In Recycle Bin. 
	@GetMapping("/recycle-bin")
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
		
		Integer userId = 1;
		List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
		
		if (CollectionUtils.isEmpty(notes)) {

			return CommonUtil.createBuildResponseMessage("Notes not available in Recycle Bin", HttpStatus.OK);
		}

		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);

	}
	
	// Deletion Notes from Recycle Bin.
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable("id") Integer id) throws Exception {

		notesService.hardDeleteNotes(id);

		return CommonUtil.createBuildResponseMessage("delete Success", HttpStatus.OK);

	}

	// Delete All Notes From Recycle Bin At One Time.
	@DeleteMapping("/delete")
	public ResponseEntity<?> emptyRecycleBin() throws Exception {

		Integer userId = 1;

		notesService.emptyRecycleBin(userId);

		return CommonUtil.createBuildResponseMessage("delete Success", HttpStatus.OK);

	}
	
	@GetMapping("/fav/{notesId}")
	public ResponseEntity<?> favouriteNote(@PathVariable("notesId") Integer notesId) throws Exception {
		
		notesService.favouriteNotes(notesId);
		
		return CommonUtil.createBuildResponseMessage("Notes added Favourite", HttpStatus.CREATED);
	}

	@DeleteMapping("/un-fav/{favNotesId}")
	public ResponseEntity<?> unFavouriteNote(@PathVariable("favNotesId") Integer favNotesId) throws Exception {

		notesService.unFavouriteNotes(favNotesId);

		return CommonUtil.createBuildResponseMessage("Remove Favourite", HttpStatus.OK);
	}

	@GetMapping("/fav-notes")
	public ResponseEntity<?> getUserfavouriteNote() {

		List<FavouriteNotesDto> userFavouriteNotes = notesService.getUserFavouriteNotes();

		if (CollectionUtils.isEmpty(userFavouriteNotes)) {

			return ResponseEntity.noContent().build();
		}

		return CommonUtil.createBuildResponse(userFavouriteNotes, HttpStatus.OK);
	}
	
	@GetMapping("/copy/{id}")
	public ResponseEntity<?> copyNotes(@PathVariable("id") Integer id) throws Exception {

		Boolean copyNotes = notesService.copyNotes(id);

		if (copyNotes) {
			return CommonUtil.createBuildResponseMessage("Copied Success", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Copy Failed! Try Again", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
