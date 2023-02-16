package com.assignment.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.component.JwtTokenUtilityComponent;
import com.assignment.dto.StatusDTO;
import com.assignment.dto.UserLikedBooksListResponseDTO;
import com.assignment.dto.UserReadLaterBooksListResponseDTO;
import com.assignment.dto.UserRegisterDTO;
import com.assignment.dto.UserResponseDTO;
import com.assignment.dto.UserUpdateDTO;
import com.assignment.service.UserService;

@RestController
@CrossOrigin
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	private JwtTokenUtilityComponent jwtTokenUtilityComponent;
	
	@GetMapping("/users")
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserById(@PathVariable @Min(1) Long id) {
		StatusDTO<UserResponseDTO> userStatus = userService.getUserById(id);
		if (!userStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.ok(userStatus.getObject());
	}
	
	@PostMapping("/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
		StatusDTO<UserResponseDTO> userStatus = userService.createUser(userRegisterDTO);
		if (!userStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(userStatus.getObject());
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO, @PathVariable Long id) {
		StatusDTO<UserResponseDTO> userStatus = userService.updateUser(userUpdateDTO, id);
		if (!userStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.ok(userStatus.getObject());
	}

	@PatchMapping("/users/{id}")
	public ResponseEntity<?> updateUserProperties(@Valid @RequestBody Map<String, String> updateMap, @PathVariable Long id) {
		StatusDTO<UserResponseDTO> userStatus = userService.updateUserProperties(updateMap, id);
		if (!userStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.ok(userStatus.getObject());
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable @Min(1) Long id) {
		boolean deleted = userService.deleteUserById(id);
		if (deleted) {
			return ResponseEntity.ok("User deleted successfully.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " does not exist.");
	}

	@DeleteMapping("/users")
	public ResponseEntity<?> deleteAllUser() {
		userService.deleteAllUsers();
		return ResponseEntity.ok("All users deleted successfully.");
	}
	
	private String getUsernameFromAuthorizationHeader(String authorizationHeader) {
		String jwtToken = authorizationHeader.substring(7);
		return jwtTokenUtilityComponent.getUsernameFromToken(jwtToken);
	}

	@GetMapping("/users/liked-books")
	public ResponseEntity<?> getUserLikedBooksList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		StatusDTO<UserLikedBooksListResponseDTO> userStatus = userService.getUserLikedBooksList(getUsernameFromAuthorizationHeader(authorizationHeader));
		if (!userStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.ok(userStatus.getObject());
	}
	
	@PostMapping("/users/liked-books")
	public ResponseEntity<?> addToUserLikedBooksList(@Valid @RequestBody Long bookId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		StatusDTO<UserLikedBooksListResponseDTO> userStatus = userService.addToUserLikedBooksList(bookId, getUsernameFromAuthorizationHeader(authorizationHeader));
		if (!userStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.ok(userStatus.getObject());
	}

	@GetMapping("/users/read-later-books")
	public ResponseEntity<?> getUserReadLaterBooksList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		StatusDTO<UserReadLaterBooksListResponseDTO> userStatus = userService.getUserReadLaterBooksList(getUsernameFromAuthorizationHeader(authorizationHeader));
		if (!userStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.ok(userStatus.getObject());
	}

	@PostMapping("/users/read-later-books")
	public ResponseEntity<?> addToUserReadLaterBooksList(@Valid @RequestBody Long bookId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		StatusDTO<UserReadLaterBooksListResponseDTO> userStatus = userService.addToUserReadLaterBooksList(bookId, getUsernameFromAuthorizationHeader(authorizationHeader));
		if (!userStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.ok(userStatus.getObject());
	}

}
