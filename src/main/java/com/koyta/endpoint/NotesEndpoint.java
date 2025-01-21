package com.koyta.endpoint;

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
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception;

	@GetMapping("/download/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> downloadFile(@PathVariable("id") Integer id) throws Exception;
	
	@GetMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllNotes();
	
	@GetMapping("/user-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize);	

	@GetMapping("/search")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> searchNotes(@RequestParam(name = "key", defaultValue = "") String key,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize);
	
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> deleteNotes(@PathVariable("id") Integer id) throws Exception;
		
	@GetMapping("/restore/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> restoreNotes(@PathVariable("id") Integer id) throws Exception;
	
	@GetMapping("/recycle-bin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable("id") Integer id) throws Exception;
	
	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyUserRecycleBin() throws Exception;
	
	@GetMapping("/fav/{notesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> favouriteNote(@PathVariable("notesId") Integer notesId) throws Exception;
	
	@DeleteMapping("/un-fav/{favNotesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> unFavouriteNote(@PathVariable("favNotesId") Integer favNotesId) throws Exception;
	
	@GetMapping("/fav-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserfavouriteNote();
	
	@GetMapping("/copy/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> copyNotes(@PathVariable("id") Integer id) throws Exception;

}
