package com.assignment.dto;

public class UserRegisterDTO {
	
	private String username;
	private String name;
	private String password;
	private boolean isAdmin;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public UserRegisterDTO(String username, String name, String password, boolean isAdmin) {
		super();
		this.username = username;
		this.name = name;
		this.password = password;
		this.isAdmin = isAdmin;
	}
	public UserRegisterDTO() {
		super();
	}

}
