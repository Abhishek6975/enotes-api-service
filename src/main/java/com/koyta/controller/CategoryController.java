package com.koyta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.CategoryDto;
import com.koyta.dto.CategoryResponse;
import com.koyta.service.Impl.CategoryServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {

		Boolean saveCategory = categoryServiceImpl.saveCategory(categoryDto);

		if (saveCategory) {
			return new ResponseEntity<>("Save success", HttpStatus.CREATED);
		} else {

			return new ResponseEntity<>("Not Save success", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/category")
	public ResponseEntity<?> getAllCategory() {

		List<CategoryDto> allCategory = categoryServiceImpl.getAllCategory();

		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}

	}

	@GetMapping("/active")
	public ResponseEntity<?> getActiveCategory() {

		List<CategoryResponse> allCategory = categoryServiceImpl.getActiveCategory();

		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable("id") Integer id) throws Exception {

		CategoryDto categoryDto = categoryServiceImpl.getCategoryById(id);

		if (ObjectUtils.isEmpty(categoryDto)) {

			return new ResponseEntity<>("Internal Server Error", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(categoryDto, HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable("id") Integer id) {

		Boolean deleted = categoryServiceImpl.deleteCategoryById(id);

		if (deleted) {

			return new ResponseEntity<>("Category Deleted Successfully", HttpStatus.OK);
		}

		return new ResponseEntity<>("Category Not Deleted ", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
