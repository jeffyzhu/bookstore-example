package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Cart;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


public class CartServiceTests {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addToCart_bookNotFound_throwsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            cartService.addToCart(1L, 1L, 2);
        });

        assertEquals("Book not found with id: 1", exception.getMessage());
    }

    @Test
    void addToCart_bookExists_addsBookToCart() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Cart cart = new Cart();
        cart.setUserId(1L);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        cartService.addToCart(1L, 1L, 2);

        assertEquals(1, cart.getBooks().size());
        assertEquals(1, cart.getBooks().iterator().next().getId());
        assertEquals(2, cart.getBookQuantities().get(book));
    }

    @Test
    void getCartContents_cartExists_returnsBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.getBooks().add(book);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        Set<Book> books = cartService.getCartContents(1L);

        assertEquals(1, books.size());
        assertEquals(1L, books.iterator().next().getId());
    }

    @Test
    void getCartContents_cartNotFound_throwsException() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            cartService.getCartContents(1L);
        });

        assertEquals("Cart not found for user: 1", exception.getMessage());
    }

    @Test
    void calculateTotalPrice_cartExists_returnsTotalPrice() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setPrice(10.0);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setPrice(20.0);

        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.getBooks().add(book1);
        cart.getBooks().add(book2);
        cart.getBookQuantities().put(book1, 2);
        cart.getBookQuantities().put(book2, 1);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        double totalPrice = cartService.calculateTotalPrice(1L);

        assertEquals(40.0, totalPrice);
    }

    @Test
    void calculateTotalPrice_cartNotFound_throwsException() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            cartService.calculateTotalPrice(1L);
        });

        assertEquals("Cart not found for user: 1", exception.getMessage());
    }
}
