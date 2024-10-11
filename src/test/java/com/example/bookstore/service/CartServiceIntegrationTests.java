package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Cart;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
@ActiveProfiles("test")
public class CartServiceIntegrationTests {

    @Autowired
    private CartService cartService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartRepository cartRepository;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        book1 = new Book();
        book1.setTitle("The Great Gatsby");
        book1.setAuthor("F. Scott Fitzgerald");
        book1.setPrice(10.99);
        bookRepository.save(book1);

        book2 = new Book();
        book2.setTitle("1984");
        book2.setAuthor("George Orwell");
        book2.setPrice(8.99);
        bookRepository.save(book2);

        Cart cart = new Cart();
        cart.setUserId(1L);
        cartRepository.save(cart);
    }

    @Test
    void addToCart_bookNotFound_throwsException() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            cartService.addToCart(1L, 999L, 2);
        });

        assertEquals("Book not found with id: 999", exception.getMessage());
    }

    @Test
    void addToCart_bookExists_addsBookToCart() {
        cartService.addToCart(1L, book1.getId(), 2);

        Cart cart = cartRepository.findByUserId(1L).orElseThrow();

        assertEquals(1, cart.getBooks().size());
        assertEquals(2, cart.getBookQuantities().get(book1));
    }

    @Test
    void getCartContents_cartExists_returnsBooks() {
        cartService.addToCart(1L, book1.getId(), 2);

        Set<Book> books = cartService.getCartContents(1L);

        assertEquals(1, books.size());
        assertEquals(book1.getId(), books.iterator().next().getId());
    }

    @Test
    void getCartContents_cartNotFound_throwsException() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            cartService.getCartContents(999L);
        });

        assertEquals("Cart not found for user: 999", exception.getMessage());
    }

    @Test
    void calculateTotalPrice_cartExists_returnsTotalPrice() {
        cartService.addToCart(1L, book1.getId(), 2);
        cartService.addToCart(1L, book2.getId(), 1);

        double totalPrice = cartService.calculateTotalPrice(1L);

        assertEquals(30.97, totalPrice, 0.0001);
    }

    @Test
    void calculateTotalPrice_cartNotFound_throwsException() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            cartService.calculateTotalPrice(999L);
        });

        assertEquals("Cart not found for user: 999", exception.getMessage());
    }
}