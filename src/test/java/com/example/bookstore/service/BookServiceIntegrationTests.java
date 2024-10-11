package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
@ActiveProfiles("test")
public class BookServiceIntegrationTests {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        // Initialize the books
        book1 = new Book();
        book1.setTitle("The Catcher in the Rye");
        book1.setAuthor("J.D. Salinger");
        book1.setPrice(9.99);
        bookRepository.save(book1);

        book2 = new Book();
        book2.setTitle("To Kill a Mockingbird");
        book2.setAuthor("Harper Lee");
        book2.setPrice(12.99);
        bookRepository.save(book2);
    }

    @Test
    void addBook_validBook_createsBook() {
        Book book = new Book();
        book.setTitle("Brave New World");
        book.setAuthor("Aldous Huxley");
        book.setPrice(15.99);

        Book createdBook = bookService.addBook(book);

        // Assert the book got saved
        assertNotNull(createdBook.getId());
        assertEquals("Brave New World", createdBook.getTitle());
        assertEquals("Aldous Huxley", createdBook.getAuthor());
        assertEquals(15.99, createdBook.getPrice());
    }

    @Test
    void getAllBooks_returnsAllBooks() {
        List<Book> books = bookService.getAllBooks();

        // Assert the number of books
        assertEquals(2, books.size());

        // Verify the details of the first book
        Book retrievedBook1 = books.stream().filter(b -> b.getId().equals(book1.getId())).findFirst().orElse(null);
        assertNotNull(retrievedBook1);
        assertEquals("The Catcher in the Rye", retrievedBook1.getTitle());
        assertEquals("J.D. Salinger", retrievedBook1.getAuthor());
        assertEquals(9.99, retrievedBook1.getPrice());

        // Verify the details of the second book
        Book retrievedBook2 = books.stream().filter(b -> b.getId().equals(book2.getId())).findFirst().orElse(null);
        assertNotNull(retrievedBook2);
        assertEquals("To Kill a Mockingbird", retrievedBook2.getTitle());
        assertEquals("Harper Lee", retrievedBook2.getAuthor());
        assertEquals(12.99, retrievedBook2.getPrice());
    }
}