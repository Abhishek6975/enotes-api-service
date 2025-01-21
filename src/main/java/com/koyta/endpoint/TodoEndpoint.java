package com.koyta.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koyta.dto.TodoDto;

@RequestMapping("api/v1/todo")
public interface TodoEndpoint {

	@PostMapping("/")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto) throws Exception;

	@GetMapping("/{todoId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getTodoById(@PathVariable("todoId") Integer todoId) throws Exception;

	@GetMapping("/list")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllTodoByUser();

}
