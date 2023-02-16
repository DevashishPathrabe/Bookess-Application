package com.assignment.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.assignment.dto.BookDTO;
import com.assignment.dto.StatusDTO;
import com.assignment.entity.Book;
import com.assignment.repository.BookRepository;

@Service
public class BookService {
	
	@Autowired
	BookRepository bookRepository;
	
	public Book createBook(BookDTO bookDTO) {
		Book book = new Book(bookDTO.getName(), bookDTO.getAuthor(), bookDTO.getPrice());
		return bookRepository.save(book);
	}
	
	public List<Book> getAllBooks(String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name())
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		return bookRepository.findAll(sort);
	}
	
	public StatusDTO<Book> getBookById(Long id) {
		Optional<Book> optionalBook = bookRepository.findById(id);
		if (optionalBook.isEmpty()) {
			return new StatusDTO<>("Book with ID " + id + " does not exist.", false, null);
		}
		return new StatusDTO<>("", true, optionalBook.get());
	}
	
	public StatusDTO<Book> updateBook(BookDTO bookDTO, Long id) {
		Optional<Book> optionalBook = bookRepository.findById(id);
		if (optionalBook.isEmpty()) {
			return new StatusDTO<>("Book with ID " + id + " does not exist.", false, null);
		}
		Book book = optionalBook.get();
		book.setName(bookDTO.getName());
		book.setAuthor(bookDTO.getAuthor());
		book.setPrice(bookDTO.getPrice());
		return new StatusDTO<>("", true, bookRepository.save(book));
	}
	
	public StatusDTO<Book> updateBookProperties(Map<String, String> updateMap, Long id) {
		Optional<Book> optionalBook = bookRepository.findById(id);
		if (optionalBook.isEmpty()) {
			return new StatusDTO<>("Book with ID " + id + " does not exist.", false, null);
		}
		Book book = optionalBook.get();
		for (Entry<String, String> entry: updateMap.entrySet()) {
			switch(entry.getKey().toLowerCase()) {
			case "author": {
				book.setAuthor(entry.getValue());
				break;
			}
			case "name": {
				book.setName(entry.getValue());
				break;
			}
			default: {
				return new StatusDTO<>("Book property is not 'author' (string) or 'name' (string), but is '" + entry.getKey() + "' which is invalid.", false, null);
			}
			}
		}
		return new StatusDTO<>("", true, bookRepository.save(book));
	}
	
	public boolean deleteBookById(Long id) {
		Optional<Book> book = bookRepository.findById(id);
		if (book.isEmpty()) {
			return false;
		}
		bookRepository.deleteById(id);
		return true;
	}
	
	public void deleteAllBooks() {
		bookRepository.deleteAllInBatch();
	}
	
	

}
