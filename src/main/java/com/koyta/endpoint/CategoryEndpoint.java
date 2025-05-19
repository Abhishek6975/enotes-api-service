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
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.util.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Category",description = "All the Category operation API's")
@RequestMapping("/api/v1/category")
public interface CategoryEndpoint {

	@Operation(summary = "Save Category",tags = {"Category"},description = "ROLE_ADMIN Save Category")
	@PostMapping("/save")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

	@Operation(summary = "Get All Category",tags = {"Category"},description = "ROLE_ADMIN Get All Category")
	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllCategory();

	@Operation(summary = "Get Active Category",tags = {"Category"},description = "ROLE_USER_ADMIN Get Active Category")
	@GetMapping("/active")
	@PreAuthorize(ROLE_USER_ADMIN)
	public ResponseEntity<?> getActiveCategory();

	@Operation(summary = "Get Category By Id",tags = {"Category"},description = "ROLE_ADMIN Get Category Details By Id")
	@GetMapping("/{id}")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable("id") Integer id) throws ResourceNotFoundException;

	@Operation(summary = "Delete Category By Id Endpoint",tags = {"Authentication"},description = "ROLE_ADMIN Delete Category Details By Id")
	@DeleteMapping("/{id}")
	@PreAuthorize(AppConstants.ROLE_ADMIN)
	public ResponseEntity<?> deleteCategoryById(@PathVariable("id") Integer id);

}
