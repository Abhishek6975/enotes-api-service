package com.koyta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.koyta.dto.FavouriteNotesDto;
import com.koyta.dto.NotesDto;
import com.koyta.dto.NotesResponse;
import com.koyta.endpoint.NotesEndpoint;
import com.koyta.entity.FilesDetails;
import com.koyta.service.NotesService;
import com.koyta.util.CommonUtil;

@RestController
public class NotesController implements NotesEndpoint {

	@Autowired
	private NotesService notesService;

	@Override
	public ResponseEntity<?> saveNotes(String notes, MultipartFile file)
			throws Exception {

		Boolean saveNotes = notesService.saveNotes(notes, file);

		if (saveNotes) {
			return CommonUtil.createBuildResponseMessage("Note's Saved Succes", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Notes Not Saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> downloadFile(Integer id) throws Exception {

		FilesDetails fileDetails = notesService.getFileDetails(id);

		byte[] data = notesService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));

		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return ResponseEntity.ok().headers(headers).body(data);

	}

	@Override
	public ResponseEntity<?> getAllNotes() {

		List<NotesDto> allNotesDto = notesService.getAllNotes();

		if (CollectionUtils.isEmpty(allNotesDto)) {
			return ResponseEntity.noContent().build();
		}

		return CommonUtil.createBuildResponse(allNotesDto, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getAllNotesByUser(Integer pageNo, Integer pageSize) {

		NotesResponse notesDtos = notesService.getAllNotesByUser(pageNo, pageSize);

		/*
		 * if (CollectionUtils.isEmpty(notesDtos)) { return
		 * ResponseEntity.noContent().build(); }
		 */

		return CommonUtil.createBuildResponse(notesDtos, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> searchNotes(String key, Integer pageNo, Integer pageSize) {

		NotesResponse notesDtos = notesService.getNotesByUserSearch(pageNo, pageSize, key);

		return CommonUtil.createBuildResponse(notesDtos, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> deleteNotes(Integer id) throws Exception {
		
		notesService.softDeleteNotes(id);
		
		return CommonUtil.createBuildResponseMessage("delete Success", HttpStatus.OK);
		
	}
	
	@Override
	public ResponseEntity<?> restoreNotes(Integer id) throws Exception {
		
		notesService.restoreNotes(id);
		
		return CommonUtil.createBuildResponseMessage("Notes Restore Success", HttpStatus.OK);		
	}
	
	
	// Get All Deleted User's Notes Available In Recycle Bin. 
	@Override
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
		
		List<NotesDto> notes = notesService.getUserRecycleBinNotes();
		
		if (CollectionUtils.isEmpty(notes)) {

			return CommonUtil.createBuildResponseMessage("Notes not available in Recycle Bin", HttpStatus.OK);
		}

		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);

	}
	
	// Deletion Notes from Recycle Bin.
	@Override
	public ResponseEntity<?> hardDeleteNotes(Integer id) throws Exception {

		notesService.hardDeleteNotes(id);

		return CommonUtil.createBuildResponseMessage("delete Success", HttpStatus.OK);

	}

	// Delete All Notes From Recycle Bin At One Time.
	@Override
	public ResponseEntity<?> emptyUserRecycleBin() throws Exception {

		notesService.emptyRecycleBin();

		return CommonUtil.createBuildResponseMessage("delete Success", HttpStatus.OK);

	}
	
	@Override
	public ResponseEntity<?> favouriteNote(Integer notesId) throws Exception {
		
		notesService.favouriteNotes(notesId);
		
		return CommonUtil.createBuildResponseMessage("Notes added Favourite", HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> unFavouriteNote(Integer favNotesId) throws Exception {

		notesService.unFavouriteNotes(favNotesId);

		return CommonUtil.createBuildResponseMessage("Remove Favourite", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getUserfavouriteNote() {

		List<FavouriteNotesDto> userFavouriteNotes = notesService.getUserFavouriteNotes();

		if (CollectionUtils.isEmpty(userFavouriteNotes)) {

			return ResponseEntity.noContent().build();
		}

		return CommonUtil.createBuildResponse(userFavouriteNotes, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> copyNotes(Integer id) throws Exception {

		Boolean copyNotes = notesService.copyNotes(id);

		if (copyNotes) {
			return CommonUtil.createBuildResponseMessage("Copied Success", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Copy Failed! Try Again", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
