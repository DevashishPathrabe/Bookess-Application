package com.assignment.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "book")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(max = 512)
	private String name;
	
	@NotNull
	@Size(max = 512)
	private String author;
	
	@NotNull
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

	public Long getId() {
		return id;
	}

	public Book(@NotNull @Size(max = 512) String name, @NotNull @Size(max = 512) String author, @NotNull float price) {
		super();
		this.name = name;
		this.author = author;
		this.price = price;
	}

	public Book() {
		super();
	}
	
}
