package com.interswitch.bookstore.controllers;

import com.interswitch.bookstore.requests.BookAddDTO;
import com.interswitch.bookstore.requests.BookUpdateDTO;
import com.interswitch.bookstore.responses.ApiResponse;
import com.interswitch.bookstore.responses.BookDTO;
import com.interswitch.bookstore.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookInventoryControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookInventoryController controller;

    private BookDTO bookDTO;
    private BookAddDTO bookAddDTO;
    private BookUpdateDTO bookUpdateDTO;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO();
        bookDTO.setId(1L);

        bookAddDTO = new BookAddDTO();
        bookUpdateDTO = new BookUpdateDTO();
    }

    @Test
    void searchBooks_success() {
        Page<BookDTO> bookPage = new PageImpl<>(List.of(bookDTO));
        // Use eq() matcher for "test" and any() for Pageable
        when(bookService.searchBooks(eq("test"), any(Pageable.class))).thenReturn(bookPage);

        ResponseEntity<ApiResponse<?>> response = controller.searchBooks("test", 0, 10, "createdAt", "desc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(bookPage, response.getBody().data());
        verify(bookService).searchBooks(eq("test"), any(Pageable.class));
    }

    // Rest of the test class remains the same
    @Test
    void getBookById_success() {
        when(bookService.findBookById(1L)).thenReturn(bookDTO);

        ResponseEntity<ApiResponse<?>> response = controller.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(bookDTO, response.getBody().data());
        verify(bookService).findBookById(1L);
    }

    @Test
    void addBook_success() {
        when(bookService.addBook(bookAddDTO)).thenReturn(bookDTO);

        ResponseEntity<ApiResponse<?>> response = controller.addBook(bookAddDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(bookDTO, response.getBody().data());
        verify(bookService).addBook(bookAddDTO);
    }

    @Test
    void updateBook_success() {
        when(bookService.updateBook(1L, bookUpdateDTO)).thenReturn(bookDTO);

        ResponseEntity<ApiResponse<?>> response = controller.updateBook(1L, bookUpdateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(bookDTO, response.getBody().data());
        verify(bookService).updateBook(1L, bookUpdateDTO);
    }
}