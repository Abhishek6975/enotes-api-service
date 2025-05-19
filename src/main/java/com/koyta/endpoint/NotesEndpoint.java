package com.koyta.endpoint;

import static com.koyta.util.AppConstants.DEFAULT_PAGE_NO;
import static com.koyta.util.AppConstants.DEFAULT_PAGE_SIZE;
import static com.koyta.util.AppConstants.ROLE_ADMIN;
import static com.koyta.util.AppConstants.ROLE_USER;
import static com.koyta.util.AppConstants.ROLE_USER_ADMIN;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.koyta.dto.NotesRequest;
import com.koyta.exception.NoteSaveException;
import com.koyta.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notes",description = "All Notes Operations API's")
@RequestMapping("/api/v1/notes")
public interface NotesEndpoint {

	@Operation(summary = "Save Notes", tags = { "User" }, description = "User Save Notes")
	@PostMapping(value = "/", consumes = "multipart/form-data")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saveNotes(
			@RequestParam 
			@Parameter(description = "Json String Notes", required = true, content = @Content(schema = @Schema(implementation = NotesRequest.class))) 
			String notes, @RequestParam(required = false) MultipartFile file) throws ResourceNotFoundException,NoteSaveException;

	@Operation(summary = "Download Notes", tags = { "User" }, description = "USER & ADMIN are Download Notes, Provide File Id")
	@GetMapping("/download/{id}")
	@PreAuthorize(ROLE_USER_ADMIN)
	public ResponseEntity<?> downloadFile(@PathVariable("id") Integer id) throws ResourceNotFoundException ,IOException;
	
	@Operation(summary = "Get All Notes", description = "Only ADMIN Get All Notes")
	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllNotes();
	
	@Operation(summary = "Get All Notes", tags = { "User" }, description = "Only USER Get All Notes")
	@GetMapping("/user-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllNotesByUser(
			@RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);

	@Operation(summary = "Search Notes", tags = { "User" }, description = "USER Can Search Notes BY Name")
	@GetMapping("/search")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> searchNotes(@RequestParam(name = "key", defaultValue = "") String key,
			@RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);
	
	@Operation(summary = "Delete Notes", tags = { "User" }, description = "USER Delete Notes By Id")
	@GetMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> deleteNotes(@PathVariable("id") Integer id) throws ResourceNotFoundException;
		
	@Operation(summary = "Restore Notes", tags = { "User" }, description = "USER Can Restore Notes From RECYCLE BIN")
	@GetMapping("/restore/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> restoreNotes(@PathVariable("id") Integer id) throws ResourceNotFoundException;
	
	@Operation(summary = "Get All Recycle Bin Notes", tags = { "User" }, 
			description = "USER Can Get All Notes Which Currently present in the RECYCLE BIN")
	@GetMapping("/recycle-bin")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserRecycleBinNotes() throws ResourceNotFoundException;
	
	@Operation(summary = "Hard Deletion Notes", tags = { "User" }, description = "Fully Hard Deletion From Data-Base")
	@DeleteMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> hardDeleteNotes(@PathVariable("id") Integer id) throws ResourceNotFoundException;
	
	@Operation(summary = "Empty Recycle Bin", tags = { "User" }, 
			description = "USER Can Delete All Notes From Recycle Bin At One Time.")
	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyUserRecycleBin() throws ResourceNotFoundException;
	
	@Operation(summary = "Favourite Note", tags = { "User" }, description = "USER Can Make Favourite Note By Id")
	@GetMapping("/fav/{notesId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> favouriteNote(@PathVariable("notesId") Integer notesId) throws ResourceNotFoundException;
	
	@Operation(summary = "Un-Favourite Note", tags = { "User" }, description = "USER Make Un-Favourite Note")
	@DeleteMapping("/un-fav/{favNotesId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> unFavouriteNote(@PathVariable("favNotesId") Integer favNotesId) throws ResourceNotFoundException;

	@Operation(summary = "Get Users Favourite Note", tags = { "User" }, description = "Get All USER Favourite Notes")
	@GetMapping("/fav-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserfavouriteNote();

	@Operation(summary = "Copy Notes", tags = { "User" }, description = "USER Can Copy Notes BY Id")
	@GetMapping("/copy/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> copyNotes(@PathVariable("id") Integer id) throws ResourceNotFoundException;

}
