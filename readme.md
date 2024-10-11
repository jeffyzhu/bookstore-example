# Bookstore API

A simple Spring Boot-based API for managing books and shopping carts in a bookstore.

## Table of Contents
- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Setup](#setup)
- [API Endpoints](#api-endpoints)
    - [Books](#books)
    - [Cart](#cart)
- [Running Tests](#running-tests)
- [Documentation](#documentation)
- [Authors](#authors)
- [License](#license)

## Introduction

This project demonstrates a simple bookstore API, which includes functionalities for:
- Managing books
- Managing shopping carts and their contents
- Calculating total prices for carts

## Technologies Used

- Java 17
- Spring Boot 3
- H2 Database
- JPA (Java Persistence API)
- Maven
- Swagger (Springdoc OpenAPI)

## Getting Started

### Prerequisites

- Java 17
- Maven

### Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/example/bookstore-api.git
   cd bookstore-api
   ```
2. Build and run the application:
   ```sh
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
3. Access the in-memory H2 database console:
   Open your browser and go to: `http://localhost:8080/h2-console`
   Use the following credentials:
    - **JDBC URL:** `jdbc:h2:mem:testdb`
    - **Username:** `sa`
    - **Password:** `password`

## API Endpoints

### Books

- **Add a Book**

  `POST /books`

  Request Body:
  ```json
  {
    "title": "Book Title",
    "author": "Author Name",
    "price": 29.99,
    "category": "Category"
  }
  ```

  Response:
  ```json
  {
    "id": 1,
    "title": "Book Title",
    "author": "Author Name",
    "price": 29.99,
    "category": "Category"
  }
  ```

- **Get All Books**

  `GET /books`

  Response:
  ```json
  [
    {
      "id": 1,
      "title": "Book Title",
      "author": "Author Name",
      "price": 29.99,
      "category": "Category"
    },
    ...
  ]
  ```

### Cart

- **Add Book to Cart**

  `POST /cart`

  Request Parameters:
    - `userId`: Long
    - `bookId`: Long
    - `quantity`: Integer

  Response:
  ```text
  Book added to cart
  ```

- **Get Cart Contents**

  `GET /cart/{userId}`

  Response:
  ```json
  [
    {
      "id": 1,
      "title": "Book Title",
      "author": "Author Name",
      "price": 29.99,
      "category": "Category"
    },
    ...
  ]
  ```

- **Checkout Cart and Get Total Price**

  `GET /cart/checkout/{userId}`

  Response:
  ```json
  {
    "totalPrice": 89.99
  }
  ```

## Running Tests

To run the tests, use the following command:
```sh
./mvnw test
```

## Documentation

To access the API documentation, navigate to the following URL after running the application:
`http://localhost:8080/swagger-ui.html`

## Authors
- [Author Name](https://github.com/example)

## License
[MIT](LICENSE)