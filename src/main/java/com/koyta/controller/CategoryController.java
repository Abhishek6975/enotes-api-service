package com.koyta.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.CategoryDto;
import com.koyta.dto.CategoryResponse;
import com.koyta.endpoint.CategoryEndpoint;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.service.impl.CategoryServiceImpl;
import com.koyta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CategoryController implements CategoryEndpoint {

	private CategoryServiceImpl categoryServiceImpl;

	public CategoryController(CategoryServiceImpl categoryServiceImpl) {
		super();
		this.categoryServiceImpl = categoryServiceImpl;
	}

	@Override
	public ResponseEntity<?> saveCategory(CategoryDto categoryDto) {

		boolean saveCategory = categoryServiceImpl.saveCategory(categoryDto);

		if (saveCategory) {
			return CommonUtil.createBuildResponseMessage("Saved Success", HttpStatus.CREATED);
		} else {
			return CommonUtil.createErrorResponseMessage("Category Not Saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllCategory() {

		List<CategoryDto> allCategory = categoryServiceImpl.getAllCategory();

		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> getActiveCategory() {

		List<CategoryResponse> allCategory = categoryServiceImpl.getActiveCategory();

		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> getCategoryDetailsById(Integer id) throws ResourceNotFoundException {

		CategoryDto categoryDto = categoryServiceImpl.getCategoryById(id);

		if (ObjectUtils.isEmpty(categoryDto)) {
			return CommonUtil.createErrorResponseMessage("Internal Server Error", HttpStatus.NOT_FOUND);
		}

		return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteCategoryById(Integer id) {

		boolean deleted = categoryServiceImpl.deleteCategoryById(id);

		if (deleted) {

			return CommonUtil.createBuildResponseMessage("Category Deleted Successfully", HttpStatus.OK);
		}

		return CommonUtil.createErrorResponseMessage("Category Not Deleted", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
