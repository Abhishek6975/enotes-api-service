package com.koyta.dto;

import com.koyta.dto.NotesDto.CategoryDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotesRequest {
	
	private String title;
	
	private String description;
	
	private CategoryDto category;

}
