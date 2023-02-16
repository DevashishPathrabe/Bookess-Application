package com.assignment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.assignment.dto.StatusDTO;
import com.assignment.dto.UserLikedBooksListResponseDTO;
import com.assignment.dto.UserReadLaterBooksListResponseDTO;
import com.assignment.dto.UserRegisterDTO;
import com.assignment.dto.UserResponseDTO;
import com.assignment.dto.UserUpdateDTO;
import com.assignment.entity.Book;
import com.assignment.entity.User;
import com.assignment.repository.BookRepository;
import com.assignment.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		User user = optionalUser.get();
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}
	
	private UserResponseDTO convertUserToUserResponseDTO(User user) {
		return new UserResponseDTO(user.getId(), user.getUsername(), user.getName(), user.getIsAdmin());
	}
	
	private StatusDTO<UserResponseDTO> convertOptionalUserToStatusDTOUserResponseDTO(Optional<User> optionalUser, String statusMessage) {
		if (optionalUser.isEmpty()) {
			return new StatusDTO<UserResponseDTO>(statusMessage, false, null);
		}
		return new StatusDTO<UserResponseDTO>("", true, convertUserToUserResponseDTO(optionalUser.get()));
	}
	
	public StatusDTO<UserResponseDTO> getUserByUsername(String username) {
		return convertOptionalUserToStatusDTOUserResponseDTO(userRepository.findByUsername(username), "User with username " + username + " does not exist.");
	}
	
	public StatusDTO<UserResponseDTO> createUser(UserRegisterDTO userRegisterDTO) {
		if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
			return new StatusDTO<UserResponseDTO>("User with username " + userRegisterDTO.getUsername() + " already exists. Please create user with different username.", false, null);
		}
		User user = new User(userRegisterDTO.getUsername(), userRegisterDTO.getName(), bcryptEncoder.encode(userRegisterDTO.getPassword()), userRegisterDTO.getIsAdmin());
		return new StatusDTO<UserResponseDTO>("", true, convertUserToUserResponseDTO(userRepository.save(user)));
	}
	
	public List<UserResponseDTO> getAllUsers() {
		return userRepository.findAll().stream().map(user -> convertUserToUserResponseDTO(user)).collect(Collectors.toList());
	}
	
	public StatusDTO<UserResponseDTO> getUserById(Long id) {
		return convertOptionalUserToStatusDTOUserResponseDTO(userRepository.findById(id), "User with ID " + id + " does not exist.");
	}
	
	public StatusDTO<UserResponseDTO> updateUser(UserUpdateDTO userUpdateDTO, Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isEmpty()) {
			return new StatusDTO<UserResponseDTO>("User with ID " + id + " does not exist.", false, null);
		}
		User user = optionalUser.get();
		user.setIsAdmin(userUpdateDTO.getIsAdmin());
		user.setName(userUpdateDTO.getName());
		return new StatusDTO<UserResponseDTO>("", true, convertUserToUserResponseDTO(userRepository.save(user)));
	}
	
	public StatusDTO<UserResponseDTO> updateUserProperties(Map<String, String> updateMap, Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isEmpty()) {
			return new StatusDTO<UserResponseDTO>("User with ID " + id + " does not exist.", false, null);
		}
		User user = optionalUser.get();
		for (Entry<String, String> entry: updateMap.entrySet()) {
			switch(entry.getKey().toLowerCase()) {
			case "name": {
				user.setName(entry.getValue());
				break;
			}
			default: {
				return new StatusDTO<UserResponseDTO>("User property provided is not 'name' (string), but is '" + entry.getKey() + "' which is invalid.", false, null);
			}
			}
		}
		return new StatusDTO<UserResponseDTO>("", true, convertUserToUserResponseDTO(userRepository.save(user)));
	}
	
	public boolean deleteUserById(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isEmpty()) {
			return false;
		}
		userRepository.deleteById(id);
		return true;
	}
	
	public void deleteAllUsers() {
		userRepository.deleteAllInBatch();
	}
	
	private StatusDTO<User> addToUserBooksList(Long bookId, String username, String type) {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isEmpty()) {
			return new StatusDTO<User>("User with username " + username + " does not exist.", false, null);
		}
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		if (optionalBook.isEmpty()) {
			return new StatusDTO<User>("Book with ID " + bookId + " does not exist.", false, null);
		}
		User user = optionalUser.get();
		Book book = optionalBook.get();
		Set<Book> bookSet = null;
		if (type.equalsIgnoreCase("liked")) {
			bookSet = user.getLikedBooks();
		}
		else {
			bookSet = user.getReadLaterBooks();
		}
		Set<Long> bookIdSet = bookSet.stream().map(currentBook -> currentBook.getId()).collect(Collectors.toSet());
		if (bookIdSet.contains(book.getId())) {
			return new StatusDTO<User>("Book is already part of user's " + type + " books list.", false, null);
		}
		bookSet.add(book);
		if (type.equalsIgnoreCase("liked")) {
			user.setLikedBooks(bookSet);
		}
		else {
			user.setReadLaterBooks(bookSet);
		}
		return new StatusDTO<User>("", true, userRepository.save(user));
	}
	
	private UserLikedBooksListResponseDTO convertUserToUserLikedBooksListResponseDTO(User user) {
		return new UserLikedBooksListResponseDTO(user.getId(), user.getUsername(), user.getLikedBooks());
	}
	
	public StatusDTO<UserLikedBooksListResponseDTO> addToUserLikedBooksList(Long bookId, String username) {
		StatusDTO<User> userStatus = addToUserBooksList(bookId, username, "liked");
		if (!userStatus.isValid()) {
			return new StatusDTO<UserLikedBooksListResponseDTO>(userStatus.getStatusMessage(), userStatus.isValid(), null);
		}
		return new StatusDTO<UserLikedBooksListResponseDTO>("", true, convertUserToUserLikedBooksListResponseDTO(userStatus.getObject()));
	}
	
	public StatusDTO<UserLikedBooksListResponseDTO> getUserLikedBooksList(String username) {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isEmpty()) {
			return new StatusDTO<UserLikedBooksListResponseDTO>("User with username " + username + " does not exist.", false, null);
		}
		return new StatusDTO<UserLikedBooksListResponseDTO>("", true, convertUserToUserLikedBooksListResponseDTO(optionalUser.get()));
	}
	
	private UserReadLaterBooksListResponseDTO convertUserToUserReadLaterBooksListResponseDTO(User user) {
		return new UserReadLaterBooksListResponseDTO(user.getId(), user.getUsername(), user.getReadLaterBooks());
	}
	
	public StatusDTO<UserReadLaterBooksListResponseDTO> addToUserReadLaterBooksList(Long bookId, String username) {
		StatusDTO<User> userStatus = addToUserBooksList(bookId, username, "read-later");
		if (!userStatus.isValid()) {
			return new StatusDTO<UserReadLaterBooksListResponseDTO>(userStatus.getStatusMessage(), userStatus.isValid(), null);
		}
		return new StatusDTO<UserReadLaterBooksListResponseDTO>("", true, convertUserToUserReadLaterBooksListResponseDTO(userStatus.getObject()));
	}

	public StatusDTO<UserReadLaterBooksListResponseDTO> getUserReadLaterBooksList(String username) {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isEmpty()) {
			return new StatusDTO<UserReadLaterBooksListResponseDTO>("User with username " + username + " does not exist.", false, null);
		}
		return new StatusDTO<UserReadLaterBooksListResponseDTO>("", true, convertUserToUserReadLaterBooksListResponseDTO(optionalUser.get()));
	}

}
