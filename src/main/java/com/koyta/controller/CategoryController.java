package com.koyta.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.entity.Category;
import com.koyta.service.Impl.CategoryServiceImpl;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody Category category) {

		Boolean saveCategory = categoryServiceImpl.saveCategory(category);

		if (saveCategory) {
			return new ResponseEntity<>("save success", HttpStatus.CREATED);
		} else {

			return new ResponseEntity<>("save success", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/categories")
	public ResponseEntity<?> getCategories(){
		
		List<Category> allCategory = categoryServiceImpl.getAllCategory();
		
		if(CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		}else {
			return new ResponseEntity<>(allCategory,HttpStatus.OK);
		}
		
	}
}
