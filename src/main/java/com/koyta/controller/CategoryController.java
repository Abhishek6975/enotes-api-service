package com.koyta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.CategoryDto;
import com.koyta.dto.CategoryResponse;
import com.koyta.endpoint.CategoryEndpoint;
import com.koyta.service.Impl.CategoryServiceImpl;
import com.koyta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CategoryController implements CategoryEndpoint {

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	@Override
	public ResponseEntity<?> saveCategory(CategoryDto categoryDto) {

		Boolean saveCategory = categoryServiceImpl.saveCategory(categoryDto);

		if (saveCategory) {

			return CommonUtil.createBuildResponseMessage("Saved Success", HttpStatus.CREATED);
			// return new ResponseEntity<>("Save success", HttpStatus.CREATED);
		} else {

			return CommonUtil.createErrorResponseMessage("Category Not Saved", HttpStatus.INTERNAL_SERVER_ERROR);
			// return new ResponseEntity<>("Not Save success", HttpStatus.INTERNAL_SERVER_ERROR);
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
			// return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}

	}

	@Override
	public ResponseEntity<?> getActiveCategory() {

		List<CategoryResponse> allCategory = categoryServiceImpl.getActiveCategory();

		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
			// return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}

	}

	@Override
	public ResponseEntity<?> getCategoryDetailsById(Integer id) throws Exception {

		CategoryDto categoryDto = categoryServiceImpl.getCategoryById(id);

		if (ObjectUtils.isEmpty(categoryDto)) {

			return CommonUtil.createErrorResponseMessage("Internal Server Error", HttpStatus.NOT_FOUND);
			// return new ResponseEntity<>("Internal Server Error", HttpStatus.NOT_FOUND);
		}

		return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
		// return new ResponseEntity<>(categoryDto, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> deleteCategoryById(Integer id) {

		Boolean deleted = categoryServiceImpl.deleteCategoryById(id);

		if (deleted) {

			return CommonUtil.createBuildResponseMessage("Category Deleted Successfully", HttpStatus.OK);

			// return new ResponseEntity<>("Category Deleted Successfully", HttpStatus.OK);
		}

		return CommonUtil.createErrorResponseMessage("Category Not Deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		// return new ResponseEntity<>("Category Not Deleted ", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
