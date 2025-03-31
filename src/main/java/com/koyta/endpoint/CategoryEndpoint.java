package com.koyta.endpoint;

import static com.koyta.util.AppConstants.ROLE_ADMIN;
import static com.koyta.util.AppConstants.ROLE_USER_ADMIN;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koyta.dto.CategoryDto;
import com.koyta.util.AppConstants;

@RequestMapping("/api/v1/category")
public interface CategoryEndpoint {

	@PostMapping("/save")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllCategory();

	@GetMapping("/active")
	@PreAuthorize(ROLE_USER_ADMIN)
	public ResponseEntity<?> getActiveCategory();

	@GetMapping("/{id}")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable("id") Integer id) throws Exception;

	@DeleteMapping("/{id}")
	@PreAuthorize(AppConstants.ROLE_ADMIN)
	public ResponseEntity<?> deleteCategoryById(@PathVariable("id") Integer id);

}
