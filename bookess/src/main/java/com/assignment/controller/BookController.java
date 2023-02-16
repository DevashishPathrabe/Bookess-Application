package com.assignment.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.BookDTO;
import com.assignment.dto.StatusDTO;
import com.assignment.entity.Book;
import com.assignment.service.BookService;
import com.assignment.utils.BookConstants;

@RestController
@CrossOrigin
public class BookController {
	
	@Autowired
	BookService bookService;
	
	@GetMapping("/books")
	public ResponseEntity<List<Book>> getAllBooks(
		@RequestParam(value = "sortBy", defaultValue = BookConstants.DEFAULT_SORT_BY, required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = BookConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
	) {
		return ResponseEntity.ok(bookService.getAllBooks(sortBy, sortDir));
	}
	
	@GetMapping("/books/{id}")
	public ResponseEntity<?> getBookById(@PathVariable @Min(1) Long id) {
		StatusDTO<Book> bookStatus = bookService.getBookById(id);
		if (!bookStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookStatus.getStatusMessage());
		}
		return ResponseEntity.ok(bookStatus.getObject());
	}
	
	@PostMapping("/books")
	public ResponseEntity<?> createBook(@Valid @RequestBody BookDTO bookDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDTO));
	}
	
	@PutMapping("/books/{id}")
	public ResponseEntity<?> updateBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable Long id) {
		StatusDTO<Book> bookStatus = bookService.updateBook(bookDTO, id);
		if (!bookStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookStatus.getStatusMessage());
		}
		return ResponseEntity.ok(bookStatus.getObject());
	}
	
	@PatchMapping("/books/{id}")
	public ResponseEntity<?> updateBookProperties(@Valid @RequestBody Map<String, String> updateMap, @PathVariable Long id) {
		StatusDTO<Book> bookStatus = bookService.updateBookProperties(updateMap, id);
		if (!bookStatus.isValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookStatus.getStatusMessage());
		}
		return ResponseEntity.ok(bookStatus.getObject());
	}
	
	@DeleteMapping("/books/{id}")
	public ResponseEntity<?> deleteBookById(@PathVariable @Min(1) Long id) {
		boolean deleted = bookService.deleteBookById(id);
		if (deleted) {
			return ResponseEntity.ok("Book deleted successfully.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with ID " + id + " does not exist.");
	}
	
	@DeleteMapping("/books")
	public ResponseEntity<?> deleteAllBooks() {
		bookService.deleteAllBooks();
		return ResponseEntity.ok("All books deleted successfully.");
	}

}
