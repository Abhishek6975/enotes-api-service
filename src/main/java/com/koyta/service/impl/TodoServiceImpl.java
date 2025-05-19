package com.koyta.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.koyta.dto.TodoDto;
import com.koyta.dto.TodoDto.StatusDto;
import com.koyta.entity.Todo;
import com.koyta.enums.TodoStatus;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.repository.TodoRepository;
import com.koyta.service.TodoService;
import com.koyta.util.CommonUtil;
import com.koyta.util.Validation;

@Service
public class TodoServiceImpl implements TodoService {

	private TodoRepository todoRepository;

	private ModelMapper modelMapper;

	private Validation validation;

	public TodoServiceImpl(TodoRepository todoRepository, ModelMapper modelMapper, Validation validation) {
		super();
		this.todoRepository = todoRepository;
		this.modelMapper = modelMapper;
		this.validation = validation;
	}

	@Override
	public Boolean saveTodo(TodoDto todoDto) throws ResourceNotFoundException {

		validation.todoValidation(todoDto);

		Todo todo = modelMapper.map(todoDto, Todo.class);

		todo.setStatusId(todoDto.getStatus().getId());

		Todo save = todoRepository.save(todo);

		return !ObjectUtils.isEmpty(save);
	}

	@Override
	public TodoDto getTodoById(Integer id) throws ResourceNotFoundException {

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

		Integer userId = CommonUtil.getLoggedInUser().getId();

		List<Todo> todoList = todoRepository.findByCreatedBy(userId);

		return todoList.stream().map((Todo td) -> modelMapper.map(td, TodoDto.class)).toList();
	}

}
