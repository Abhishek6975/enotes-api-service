package com.koyta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.koyta.dto.TodoDto;
import com.koyta.endpoint.TodoEndpoint;
import com.koyta.service.TodoService;
import com.koyta.util.CommonUtil;

@RestController
public class TodoController implements TodoEndpoint {

	@Autowired
	private TodoService todoService;

	@Override
	public ResponseEntity<?> saveTodo(TodoDto todoDto) throws Exception {

		Boolean saveTodo = todoService.saveTodo(todoDto);

		if (saveTodo) {
			return CommonUtil.createBuildResponseMessage("Todo Saved Succes", HttpStatus.CREATED);
		}

		return CommonUtil.createErrorResponseMessage("Todo Not Saved", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<?> getTodoById(Integer todoId) throws Exception {

		TodoDto todo = todoService.getTodoById(todoId);

		if (ObjectUtils.isEmpty(todo)) {

			return CommonUtil.createErrorResponseMessage("Internal Server Error", HttpStatus.NOT_FOUND);
			// return new ResponseEntity<>("Internal Server Error", HttpStatus.NOT_FOUND);
		}

		return CommonUtil.createBuildResponse(todo, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getAllTodoByUser() {

		List<TodoDto> todoByUser = todoService.getTodoByUser();

		if (CollectionUtils.isEmpty(todoByUser)) {
			return ResponseEntity.noContent().build();
		} else {

			return CommonUtil.createBuildResponse(todoByUser, HttpStatus.OK);
			// return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}

	}

}
