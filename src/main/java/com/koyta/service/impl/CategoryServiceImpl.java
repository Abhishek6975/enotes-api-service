package com.koyta.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.koyta.dto.CategoryDto;
import com.koyta.dto.CategoryResponse;
import com.koyta.entity.Category;
import com.koyta.exception.ExistDataException;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.repository.CategoryRepository;
import com.koyta.service.CategoryService;
import com.koyta.util.Validation;

@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;

	private ModelMapper modelMapper;

	private Validation validation;

	public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, Validation validation) {
		super();
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
		this.validation = validation;
	}

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {

		// Validation Checking
		validation.categoryValidation(categoryDto);

		// Check Category exist or Not

		boolean exist = categoryRepository.existsByName(categoryDto.getName().trim());

		if (exist) {

			// throw error

			throw new ExistDataException("Category already Exist");
		}

		Category category = modelMapper.map(categoryDto, Category.class);

		if (ObjectUtils.isEmpty(category.getId())) {

			category.setIsDeleted(false);
		} else {

			updateCategory(category);

		}

		Category save = categoryRepository.save(category);

		return !ObjectUtils.isEmpty(save);

	}

	private void updateCategory(Category category) {

		Optional<Category> findById = categoryRepository.findById(category.getId());

		if (findById.isPresent()) {

			Category existCategory = findById.get();
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setIsDeleted(existCategory.getIsDeleted());

		}

	}

	@Override
	public List<CategoryDto> getAllCategory() {

		List<Category> categories = categoryRepository.findByIsDeletedFalse();

		return categories.stream().map(cat -> modelMapper.map(cat, CategoryDto.class)).toList();

	}

	@Override
	public List<CategoryResponse> getActiveCategory() {

		List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();

		return categories.stream().map(cat -> modelMapper.map(cat, CategoryResponse.class)).toList();

	}

	@Override
	public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException {

		Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Found with Id " + id));

		if (!ObjectUtils.isEmpty(category)) {

			return modelMapper.map(category, CategoryDto.class);
		}

		return null;

	}

	@Override
	public Boolean deleteCategoryById(Integer id) {

		Optional<Category> findByCategory = categoryRepository.findById(id);

		if (findByCategory.isPresent()) {

			Category category = findByCategory.get();
			category.setIsDeleted(true);
			categoryRepository.save(category);
			return true;

		}

		return false;

	}

}
