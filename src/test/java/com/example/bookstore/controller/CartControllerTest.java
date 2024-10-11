package com.example.bookstore.controller;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Author");
        book.setPrice(29.99);
    }

    @Test
    public void addToCartTest() throws Exception {
        Long userId = 1L;
        Long bookId = 1L;
        int quantity = 3;

        // Mocking the service layer
        Mockito.doNothing().when(cartService).addToCart(userId, bookId, quantity);

        mockMvc.perform(post("/cart")
                        .param("userId", String.valueOf(userId))
                        .param("bookId", String.valueOf(bookId))
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book added to cart"));
    }

    @Test
    public void getCartContentsTest() throws Exception {
        Long userId = 1L;
        Set<Book> cartContents = new HashSet<>();
        cartContents.add(book);

        // Mocking the service layer
        given(cartService.getCartContents(userId)).willReturn(cartContents);

        mockMvc.perform(get("/cart/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'title':'Test Book','author':'Author','price':29.99}]"));
    }

    @Test
    public void checkoutTest() throws Exception {
        Long userId = 1L;
        double totalPrice = 59.98;

        // Mocking the service layer
        given(cartService.calculateTotalPrice(userId)).willReturn(totalPrice);

        mockMvc.perform(get("/cart/checkout/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("59.98"));
    }
}