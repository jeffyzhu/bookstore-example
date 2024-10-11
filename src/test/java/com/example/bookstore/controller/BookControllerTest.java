package com.example.bookstore.controller;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class) // WebMvcTest limits the test scope to web layer
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Author");
        book.setPrice(29.99);
        book.setCategory("Fiction");
    }

    @Test
    public void addBookTest() throws Exception {
        // Mocking the service layer
        given(bookService.addBook(Mockito.any(Book.class))).willReturn(book);

        String bookJson = "{\"title\":\"Test Book\", \"author\":\"Author\", \"price\":29.99, \"category\":\"Fiction\"}";

        mockMvc.perform(post("/books")
                        .content(bookJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"title\":\"Test Book\",\"author\":\"Author\",\"price\":29.99,\"category\":\"Fiction\"}"));
    }

    @Test
    public void getAllBooksTest() throws Exception {
        List<Book> books = Arrays.asList(book);

        // Mocking the service layer
        given(bookService.getAllBooks()).willReturn(books);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"title\":\"Test Book\",\"author\":\"Author\",\"price\":29.99,\"category\":\"Fiction\"}]"));
    }
}