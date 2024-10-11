package com.example.bookstore.service;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Cart;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service class for managing user carts in a book store application.
 */
@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Adds a specified quantity of a book to the user's cart. If the book does not exist,
     * an {@code EntityNotFoundException} is thrown. If the cart for the user does not exist,
     * a new cart is created.
     *
     * @param userId   the ID of the user to add the book to the cart for
     * @param bookId   the ID of the book to add to the cart
     * @param quantity the quantity of the book to add to the cart
     * @throws EntityNotFoundException if the book with the specified ID is not found
     */
    public void addToCart(Long userId, Long bookId, Integer quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

        cart.getBooks().add(book);
        cart.getBookQuantities().merge(book, quantity, Integer::sum);

        cartRepository.save(cart);
    }

    /**
     * Retrieves the contents of a user's cart.
     *
     * @param userId the ID of the user whose cart contents are to be retrieved
     * @return a set of books present in the user's cart
     * @throws EntityNotFoundException if the cart for the specified user is not found
     */
    public Set<Book> getCartContents(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user: " + userId));
        return cart.getBooks();
    }

    /**
     * Calculates the total price of the books in the user's cart.
     *
     * @param userId the ID of the user whose cart total price is to be calculated
     * @return the total price of all books in the user's cart
     * @throws EntityNotFoundException if the cart for the specified user is not found
     */
    public double calculateTotalPrice(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user: " + userId));
        return cart.getBooks().stream()
                .mapToDouble(book -> book.getPrice() * cart.getBookQuantities().get(book))
                .sum();
    }
}
