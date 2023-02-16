package com.assignment.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotNull
	@Size(max = 512)
	@Column(unique = true)
	private String username;
	
	@NotNull
	@Size(max = 512)
	private String name;
	
	@NotNull
	@Size(max = 128)
	private String password;
	
	@NotNull
	private boolean isAdmin;
	
	@ManyToMany(cascade = {CascadeType.PERSIST})
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "user_liked_books", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "book_id") })
	private Set<Book> likedBooks = new HashSet<>();
	
	@ManyToMany(cascade = {CascadeType.PERSIST})
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "user_read_later_books", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "book_id") })
	private Set<Book> readLaterBooks = new HashSet<>();

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

	public Set<Book> getLikedBooks() {
		return likedBooks;
	}

	public void setLikedBooks(Set<Book> likedBooks) {
		this.likedBooks = likedBooks;
	}

	public Set<Book> getReadLaterBooks() {
		return readLaterBooks;
	}

	public void setReadLaterBooks(Set<Book> readLaterBooks) {
		this.readLaterBooks = readLaterBooks;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User(@NotNull @Size(max = 512) String username, @NotNull @Size(max = 512) String name,
			@NotNull @Size(max = 128) String password, @NotNull boolean isAdmin) {
		super();
		this.username = username;
		this.name = name;
		this.password = password;
		this.isAdmin = isAdmin;
		this.setLikedBooks(new HashSet<Book>());
		this.setReadLaterBooks(new HashSet<Book>());
	}

	public User() {
		super();
	}
	
}
