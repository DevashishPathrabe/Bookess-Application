package com.assignment.dto;

public class UserUpdateDTO {
	
	private String name;
	private boolean isAdmin;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public UserUpdateDTO(String name, boolean isAdmin) {
		super();
		this.name = name;
		this.isAdmin = isAdmin;
	}
	public UserUpdateDTO() {
		super();
	}

}
