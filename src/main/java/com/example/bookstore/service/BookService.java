package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BookService is a service class that provides methods to manage books in the bookstore.
 * It interacts with the BookRepository to perform CRUD operations on Book entities.
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Adds a new book to the bookstore's collection.
     *
     * @param book The Book entity to be added.
     * @return The saved Book entity.
     */
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Retrieves all books in the bookstore's collection.
     *
     * @return A list of all Book entities.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
