package com.example.bookstore.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "cart_books",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "cart_books", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "book_id")
    @Column(name = "quantity", nullable = false)
    private Map<Book, Integer> bookQuantities = new HashMap<>();
}
