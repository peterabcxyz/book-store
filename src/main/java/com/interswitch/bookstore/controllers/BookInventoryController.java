package com.interswitch.bookstore.controllers;

import com.interswitch.bookstore.requests.BookAddDTO;
import com.interswitch.bookstore.requests.BookUpdateDTO;
import com.interswitch.bookstore.responses.ApiResponse;
import com.interswitch.bookstore.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Book Store Inventory Service APIs", description = "API for management book inventory")
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
@RestController
public class BookInventoryController {
    private final BookService bookService;

    /**
     * Searches for books in the inventory based on a search term with pagination and sorting options.
     *
     * @param searchTerm    The term to search for in book attributes (optional)
     * @param page          The page number to retrieve (default: 0)
     * @param size          The number of items per page (default: 10)
     * @param sortBy        The field to sort by (default: "createdAt")
     * @param sortDirection The sort direction ("asc" or "desc", default: "desc")
     * @return ResponseEntity containing ApiResponse with search results
     */
    @Operation(summary = "Search books by search term")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchBooks(@RequestParam(required = false) String searchTerm,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                                      @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                      @RequestParam(value = "sort-direction", defaultValue = "desc") String sortDirection) {
        var direction =
                "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, JpaSort.unsafe(direction, sortBy));
        var books = bookService.searchBooks(searchTerm, pageable);
        var apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Books retrieved successfully.", books);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Retrieves a specific book from the inventory by its ID.
     *
     * @param id The ID of the book to retrieve
     * @return ResponseEntity containing ApiResponse with the requested book
     */
    @Operation(summary = "Get book from inventory by book id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getBookById(@PathVariable Long id) {
        var book = bookService.findBookById(id);
        var apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Book retrieved successfully.", book);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Adds a new book to the inventory.
     *
     * @param request The BookAddDTO containing book details to be added
     * @return ResponseEntity containing ApiResponse with the created book
     */
    @Operation(summary = "Add book to inventory")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addBook(@Valid @RequestBody BookAddDTO request) {
        var createdBook = bookService.addBook(request);
        var apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Book created successfully.", createdBook);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    /**
     * Updates an existing book in the inventory.
     *
     * @param id      The ID of the book to update
     * @param request The BookUpdateDTO containing updated book details
     * @return ResponseEntity containing ApiResponse with the updated book
     */
    @Operation(summary = "Update book in inventory")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateBook(@PathVariable Long id, @Valid @RequestBody BookUpdateDTO request) {
        var updatedBook = bookService.updateBook(id, request);
        var apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Book retrieved successfully.", updatedBook);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}