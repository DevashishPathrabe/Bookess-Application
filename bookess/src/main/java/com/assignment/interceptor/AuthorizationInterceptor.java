package com.assignment.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import com.assignment.component.JwtTokenUtilityComponent;
import com.assignment.dto.StatusDTO;
import com.assignment.dto.UserResponseDTO;
import com.assignment.service.UserService;

public class AuthorizationInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtilityComponent jwtTokenUtilityComponent;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String currentMethod = request.getMethod();
        final String currentURI = request.getRequestURI().toString();
        if (currentURI.equalsIgnoreCase("/books") && HttpMethod.GET.matches(currentMethod)) {
        	return true;
        }
        else if (currentURI.equalsIgnoreCase("/register") || currentURI.equalsIgnoreCase("/login")) {
        	if (HttpMethod.POST.matches(currentMethod)) {
        		return true;
        	}
        	response.sendError(HttpStatus.FORBIDDEN.value());
			return false;
        }
        final String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtilityComponent.getUsernameFromToken(jwtToken);
			} catch (Exception e) {
				System.out.println("Exception " + e.toString());
			}
		}
		if (username != null) {
			StatusDTO<UserResponseDTO> userStatus = userService.getUserByUsername(username);
			if (!userStatus.isValid()) {
				response.sendError(HttpStatus.FORBIDDEN.value());
				return false;
			}
			UserResponseDTO userResponseDTO = userStatus.getObject();
			boolean isUserAdmin = userResponseDTO.getIsAdmin();
			if (currentURI.equalsIgnoreCase("/signout") && HttpMethod.GET.matches(currentMethod)) {
				return true;
			}
			else if (
				currentURI.toLowerCase().startsWith("/books") &&
				(HttpMethod.GET.matches(currentMethod) || HttpMethod.POST.matches(currentMethod) || HttpMethod.PUT.matches(currentMethod) || HttpMethod.PATCH.matches(currentMethod) || HttpMethod.DELETE.matches(currentMethod))
			) {
				if (isUserAdmin) {
					return true;
				}
				response.sendError(HttpStatus.FORBIDDEN.value());
				return false;
			}
			else if (
				(currentURI.equalsIgnoreCase("/users/liked-books") || currentURI.equalsIgnoreCase("/users/read-later-books")) &&
				(HttpMethod.GET.matches(currentMethod) || HttpMethod.POST.matches(currentMethod))
			) {
				if (!isUserAdmin) {
					return true;
				}
				response.sendError(HttpStatus.FORBIDDEN.value());
				return false;
			}
			else if (
				currentURI.toLowerCase().startsWith("/users") &&
				(HttpMethod.GET.matches(currentMethod) || HttpMethod.POST.matches(currentMethod) || HttpMethod.PUT.matches(currentMethod) || HttpMethod.PATCH.matches(currentMethod) || HttpMethod.DELETE.matches(currentMethod))
			) {
				if (isUserAdmin) {
					return true;
				}
				response.sendError(HttpStatus.FORBIDDEN.value());
				return false;
			}
			else {
				response.sendError(HttpStatus.FORBIDDEN.value());
				return false;
			}
		}
		response.sendError(HttpStatus.FORBIDDEN.value());
		return false;
    }
	
}
