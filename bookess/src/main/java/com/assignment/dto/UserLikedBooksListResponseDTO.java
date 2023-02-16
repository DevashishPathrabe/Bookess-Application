package com.assignment.dto;

import java.util.HashSet;
import java.util.Set;

import com.assignment.entity.Book;

public class UserLikedBooksListResponseDTO {
	
	private Long id;
	private String username;
	private Set<Book> likedBooks = new HashSet<>();
	
	public Long getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public Set<Book> getLikedBooks() {
		return likedBooks;
	}
	
	public UserLikedBooksListResponseDTO(Long id, String username, Set<Book> likedBooks) {
		super();
		this.id = id;
		this.username = username;
		this.likedBooks = likedBooks;
	}

}
