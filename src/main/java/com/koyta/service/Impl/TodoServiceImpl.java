package com.koyta.service.Impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.koyta.dto.TodoDto;
import com.koyta.dto.TodoDto.StatusDto;
import com.koyta.entity.Todo;
import com.koyta.enums.TodoStatus;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.repository.TodoRepository;
import com.koyta.service.TodoService;
import com.koyta.util.Validation;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private Validation validation;

	@Override
	public Boolean saveTodo(TodoDto todoDto) throws Exception {
		
		// validate todo status
		
		validation.todoValidation(todoDto);
		

		Todo todo = modelMapper.map(todoDto, Todo.class);
		
		todo.setStatusId(todoDto.getStatus().getId());

		Todo save = todoRepository.save(todo);

		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}

		return false;
	}

	@Override
	public TodoDto getTodoById(Integer id) throws Exception {

		Todo todo = todoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Todo Not Found! id Invalid"));

		TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
		
		setStatus(todoDto,todo);

		return todoDto;
	}

	private void setStatus(TodoDto todoDto, Todo todo) {
		
		for(TodoStatus st : TodoStatus.values()) {
			
			if(st.getId().equals(todo.getStatusId())) {
				
				StatusDto statusDto = StatusDto.builder()
						.id(st.getId())
						.name(st.getName())
						.build();
				todoDto.setStatus(statusDto);
			}
			
		}
	}

	@Override
	public List<TodoDto> getTodoByUser() {

		Integer userId = 1;

		List<Todo> todoList = todoRepository.findByCreatedBy(userId);

		return todoList.stream().map((Todo td) -> modelMapper.map(td, TodoDto.class)).toList();
	}

}
