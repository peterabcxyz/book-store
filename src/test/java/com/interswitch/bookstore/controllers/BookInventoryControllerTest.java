package com.interswitch.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.bookstore.enums.Genre;
import com.interswitch.bookstore.exceptions.NotFoundException;
import com.interswitch.bookstore.requests.BookAddDTO;
import com.interswitch.bookstore.requests.BookUpdateDTO;
import com.interswitch.bookstore.responses.BookDTO;
import com.interswitch.bookstore.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookInventoryController.class)
class BookInventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setGenre(Genre.FICTION);
        bookDTO.setIsbn("123-456-789");
        bookDTO.setAuthor("Test Author");
        bookDTO.setPublicationYear(2020);
        bookDTO.setPrice("10.99");
        bookDTO.setQuantityInStock(5);
        bookDTO.setCreatedAt("01-01-2023 12:00:00");
    }

    @Test
    void searchBooks_Success() throws Exception {
        Page<BookDTO> bookPage = new PageImpl<>(Collections.singletonList(bookDTO));
        when(bookService.searchBooks(anyString(), any())).thenReturn(bookPage);

        mockMvc.perform(get("/api/inventories/search")
                        .param("searchTerm", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content[0].title").value("Test Book"));
    }

    @Test
    void getBookById_Success() throws Exception {
        when(bookService.findBookById(1L)).thenReturn(bookDTO);

        mockMvc.perform(get("/api/inventories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("Test Book"));
    }

    @Test
    void getBookById_NotFound() throws Exception {
        when(bookService.findBookById(1L)).thenThrow(new NotFoundException("Book not found"));

        mockMvc.perform(get("/api/inventories/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void addBook_Success() throws Exception {
        BookAddDTO request = new BookAddDTO();
        request.setTitle("Test Book");
        request.setGenre(Genre.FICTION);
        request.setIsbn("123-456-789");
        request.setAuthor("Test Author");
        request.setPublicationYear(2020);
        request.setQuantityInStock(5);
        request.setPrice(10.99);

        when(bookService.addBook(any(BookAddDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(post("/api/inventories/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("Test Book"));
    }

    @Test
    void addBook_BadRequest_InvalidData() throws Exception {
        BookAddDTO request = new BookAddDTO();
        request.setTitle("Test@Book"); // Invalid title
        request.setGenre(Genre.FICTION);
        request.setIsbn("123-456-789");
        request.setAuthor("Test Author");
        request.setPublicationYear(2020);
        request.setQuantityInStock(5);
        request.setPrice(10.99);

        mockMvc.perform(post("/api/inventories/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data.title").value("Title must contain only letters and numbers"));
    }

    @Test
    void updateBook_Success() throws Exception {
        BookUpdateDTO request = new BookUpdateDTO();
        request.setTitle("Updated Book");
        request.setGenre(Genre.FICTION);
        request.setIsbn("123-456-789");
        request.setAuthor("Test Author");
        request.setPublicationYear(2020);
        request.setPrice(10.99);

        when(bookService.updateBook(eq(1L), any(BookUpdateDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(put("/api/inventories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("Test Book"));
    }

    @Test
    void updateBook_NotFound() throws Exception {
        BookUpdateDTO request = new BookUpdateDTO();
        request.setTitle("Updated Book");
        request.setGenre(Genre.FICTION);
        request.setIsbn("123-456-789");
        request.setAuthor("Test Author");
        request.setPublicationYear(2020);
        request.setPrice(10.99);

        when(bookService.updateBook(eq(1L), any())).thenThrow(new NotFoundException("Book not found"));

        mockMvc.perform(put("/api/inventories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Book not found"));
    }
}