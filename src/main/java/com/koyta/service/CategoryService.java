package com.koyta.service;

import java.util.List;

import com.koyta.entity.Category;

public interface CategoryService {
	
	public Boolean saveCategory(Category category);
	
	public List<Category> getAllCategory();

}
