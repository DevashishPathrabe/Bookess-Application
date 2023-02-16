package com.assignment.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.component.JwtTokenUtilityComponent;
import com.assignment.dto.JwtRequest;
import com.assignment.dto.JwtResponse;
import com.assignment.dto.StatusDTO;
import com.assignment.dto.UserRegisterDTO;
import com.assignment.dto.UserResponseDTO;
import com.assignment.entity.LogoutToken;
import com.assignment.service.LogoutTokenService;
import com.assignment.service.UserService;

@RestController
@CrossOrigin
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtilityComponent jwtTokenUtilityComponent;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LogoutTokenService logoutTokenService;
	
	@GetMapping("/signout")
	public ResponseEntity<String> logoutUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		if (authorizationHeader.startsWith("Bearer ")) {
			String jwtToken = authorizationHeader.substring(7);
			logoutTokenService.createToken(new LogoutToken(jwtToken));
			return ResponseEntity.ok("You have been successfully logged out.");
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header does not begin with Bearer string.");
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
		StatusDTO<UserResponseDTO> userStatus = userService.createUser(userRegisterDTO);
		if (!userStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userStatus.getStatusMessage());
		}
		UserResponseDTO userResponseDTO = userStatus.getObject();
		return ResponseEntity.ok(userResponseDTO.getUsername() + " has been registered successfully" + (userResponseDTO.getIsAdmin() ? " as an admin." : " as a user.") + " Proceed to login to get your authorization token.");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtilityComponent.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
}
