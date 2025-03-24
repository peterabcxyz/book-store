package com.interswitch.bookstore.services;

import com.interswitch.bookstore.domains.Book;
import com.interswitch.bookstore.exceptions.NotFoundException;
import com.interswitch.bookstore.mappers.BookMapper;
import com.interswitch.bookstore.repositories.BookRepository;
import com.interswitch.bookstore.requests.BookAddDTO;
import com.interswitch.bookstore.requests.BookUpdateDTO;
import com.interswitch.bookstore.responses.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDTO bookDTO;
    private BookAddDTO bookAddDTO;
    private BookUpdateDTO bookUpdateDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setPrice(BigDecimal.valueOf(10.0));

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");

        bookAddDTO = new BookAddDTO();
        bookAddDTO.setTitle("Test Book");
        bookAddDTO.setPrice(10.0);

        bookUpdateDTO = new BookUpdateDTO();
        bookUpdateDTO.setTitle("Updated Book");
        bookUpdateDTO.setPrice(15.0);
    }

    @Test
    void searchBooks_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.searchBooks("test", pageable)).thenReturn(bookPage);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        Page<BookDTO> result = bookService.searchBooks("test", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository).searchBooks("test", pageable);
        verify(bookMapper).toDTO(book);
    }

    @Test
    void findBookById_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.findBookById(1L);

        assertNotNull(result);
        assertEquals(bookDTO, result);
        verify(bookRepository).findById(1L);
        verify(bookMapper).toDTO(book);
    }

    @Test
    void findBookById_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.findBookById(1L));
        verify(bookRepository).findById(1L);
        verifyNoInteractions(bookMapper);
    }

    @Test
    void addBook_success() {
        when(bookMapper.toEntity(bookAddDTO)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.addBook(bookAddDTO);

        assertNotNull(result);
        assertEquals(bookDTO, result);
        verify(bookMapper).toEntity(bookAddDTO);
        verify(bookRepository).save(book);
        verify(bookMapper).toDTO(book);
    }

    @Test
    void updateBook_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.updateBook(1L, bookUpdateDTO);

        assertNotNull(result);
        assertEquals(bookDTO, result);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(book);
        verify(bookMapper).toDTO(book);
    }

    @Test
    void updateBook_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.updateBook(1L, bookUpdateDTO));
        verify(bookRepository).findById(1L);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}