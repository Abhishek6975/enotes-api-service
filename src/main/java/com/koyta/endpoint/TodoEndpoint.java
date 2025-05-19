package com.koyta.endpoint;

import static com.koyta.util.AppConstants.ROLE_USER;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koyta.dto.TodoDto;
import com.koyta.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Todo",description = "All the Todo Operation API's")
@RequestMapping("api/v1/todo")
public interface TodoEndpoint {

	@Operation(summary = "Save Todo",tags = {"Notes"},description = "Save Todo")
	@PostMapping("/")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto) throws ResourceNotFoundException;

	@Operation(summary = "Get Todo",tags = {"Notes"},description = "Get Todo By Id")
	@GetMapping("/{todoId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getTodoById(@PathVariable("todoId") Integer todoId) throws ResourceNotFoundException;

	@Operation(summary = "All Todo List",tags = {"Notes"},description = "Login User Todo List")
	@GetMapping("/list")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllTodoByUser();

}
