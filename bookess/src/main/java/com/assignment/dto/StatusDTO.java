package com.assignment.dto;

public class StatusDTO<T> {
	
	private String statusMessage;
	private boolean isValid;
	private T object;

	public String getStatusMessage() {
		return statusMessage;
	}
	public boolean isValid() {
		return isValid;
	}
	public T getObject() {
		return object;
	}

	public StatusDTO(String statusMessage, boolean isValid, T object) {
		super();
		this.statusMessage = statusMessage;
		this.isValid = isValid;
		this.object = object;
	}

}
