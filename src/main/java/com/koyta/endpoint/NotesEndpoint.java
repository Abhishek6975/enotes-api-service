package com.koyta.endpoint;

import static com.koyta.util.AppConstants.ROLE_ADMIN;
import static com.koyta.util.AppConstants.ROLE_USER;
import static com.koyta.util.AppConstants.ROLE_USER_ADMIN;
import static com.koyta.util.AppConstants.DEFAULT_PAGE_NO;
import static com.koyta.util.AppConstants.DEFAULT_PAGE_SIZE;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/notes")
public interface NotesEndpoint {

	@PostMapping("/")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception;

	@GetMapping("/download/{id}")
	@PreAuthorize(ROLE_USER_ADMIN)
	public ResponseEntity<?> downloadFile(@PathVariable("id") Integer id) throws Exception;
	
	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllNotes();
	
	@GetMapping("/user-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);	

	@GetMapping("/search")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> searchNotes(@RequestParam(name = "key", defaultValue = "") String key,
			@RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);
	
	@GetMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> deleteNotes(@PathVariable("id") Integer id) throws Exception;
		
	@GetMapping("/restore/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> restoreNotes(@PathVariable("id") Integer id) throws Exception;
	
	@GetMapping("/recycle-bin")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> hardDeleteNotes(@PathVariable("id") Integer id) throws Exception;
	
	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyUserRecycleBin() throws Exception;
	
	@GetMapping("/fav/{notesId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> favouriteNote(@PathVariable("notesId") Integer notesId) throws Exception;
	
	@DeleteMapping("/un-fav/{favNotesId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> unFavouriteNote(@PathVariable("favNotesId") Integer favNotesId) throws Exception;
	
	@GetMapping("/fav-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserfavouriteNote();
	
	@GetMapping("/copy/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> copyNotes(@PathVariable("id") Integer id) throws Exception;

}
