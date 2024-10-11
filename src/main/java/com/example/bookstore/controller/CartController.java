package com.example.bookstore.controller;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    @Operation(summary = "Add book to cart", description = "Adds a specified quantity of a book to a user's cart")
    public ResponseEntity<String> addToCart(@RequestParam Long userId,
                                            @RequestParam Long bookId,
                                            @RequestParam Integer quantity) {
        cartService.addToCart(userId, bookId, quantity);
        return ResponseEntity.ok("Book added to cart");
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get cart contents", description = "Retrieves all books present in the user's cart")
    public ResponseEntity<Set<Book>> getCartContents(@PathVariable Long userId) {
        Set<Book> cartContents = cartService.getCartContents(userId);
        return ResponseEntity.ok(cartContents);
    }

    @GetMapping("/checkout/{userId}")
    @Operation(summary = "Checkout cart", description = "Calculates the total price of the books in the user's cart for checkout")
    public ResponseEntity<Double> checkout(@PathVariable Long userId) {
        double totalPrice = cartService.calculateTotalPrice(userId);
        return ResponseEntity.ok(totalPrice);
    }
}
