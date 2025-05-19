package com.koyta.service;

import java.util.List;

import com.koyta.dto.TodoDto;
import com.koyta.exception.ResourceNotFoundException;

public interface TodoService {
	
	public Boolean saveTodo(TodoDto todo) throws ResourceNotFoundException;
	
	public TodoDto getTodoById(Integer id) throws ResourceNotFoundException;
	
	public List<TodoDto> getTodoByUser();

}
