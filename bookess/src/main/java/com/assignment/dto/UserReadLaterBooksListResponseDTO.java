package com.assignment.dto;

import java.util.HashSet;
import java.util.Set;

import com.assignment.entity.Book;

public class UserReadLaterBooksListResponseDTO {
	
	private Long id;
	private String username;
	private Set<Book> readLaterBooks = new HashSet<>();

	public Long getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public Set<Book> getReadLaterBooks() {
		return readLaterBooks;
	}

	public UserReadLaterBooksListResponseDTO(Long id, String username, Set<Book> readLaterBooks) {
		super();
		this.id = id;
		this.username = username;
		this.readLaterBooks = readLaterBooks;
	}

}
