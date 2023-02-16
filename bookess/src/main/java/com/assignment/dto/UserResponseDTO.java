package com.assignment.dto;

public class UserResponseDTO {

	private Long id;
	private String username;
	private String name;
	private boolean isAdmin;
	public String getName() {
		return name;
	}
	public boolean getIsAdmin() {
		return isAdmin;
	}
	public Long getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public UserResponseDTO(Long id, String username, String name, boolean isAdmin) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.isAdmin = isAdmin;
	}
	
}
