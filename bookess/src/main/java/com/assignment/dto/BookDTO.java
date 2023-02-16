package com.assignment.dto;

public class BookDTO {

	private String name;
	
	private String author;
	
	private float price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public BookDTO(String name, String author, float price) {
		super();
		this.name = name;
		this.author = author;
		this.price = price;
	}

	public BookDTO() {
		super();
	}
	
}
