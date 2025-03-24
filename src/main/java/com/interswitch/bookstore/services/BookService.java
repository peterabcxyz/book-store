package com.interswitch.bookstore.services;

import com.interswitch.bookstore.exceptions.NotFoundException;
import com.interswitch.bookstore.mappers.BookMapper;
import com.interswitch.bookstore.repositories.BookRepository;
import com.interswitch.bookstore.requests.BookAddDTO;
import com.interswitch.bookstore.requests.BookUpdateDTO;
import com.interswitch.bookstore.responses.BookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    /**
     * Searches for books in the inventory based on a search term with pagination support.
     *
     * @param searchTerm The term to search for in book attributes (can be null)
     * @param pageable   Pagination and sorting information
     * @return A Page of BookDTO objects containing the search results
     */
    public Page<BookDTO> searchBooks(String searchTerm, Pageable pageable) {
        var books = bookRepository.searchBooks(searchTerm, pageable);
        return books.map(bookMapper::toDTO);
    }

    /**
     * Retrieves a specific book from the inventory by its ID.
     *
     * @param id The ID of the book to retrieve
     * @return BookDTO containing the book details
     * @throws NotFoundException if no book is found with the given ID
     */
    public BookDTO findBookById(Long id) {
        return bookMapper.toDTO(bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found")));
    }

    /**
     * Adds a new book to the inventory.
     *
     * @param request BookAddDTO containing the details of the book to add
     * @return BookDTO containing the details of the newly created book
     */
    public BookDTO addBook(BookAddDTO request) {
        var entity = bookMapper.toEntity(request);
        entity.setPrice(BigDecimal.valueOf(request.getPrice()));
        return bookMapper.toDTO(bookRepository.save(entity));
    }

    /**
     * Updates an existing book in the inventory.
     *
     * @param id      The ID of the book to update
     * @param request BookUpdateDTO containing the updated book details
     * @return BookDTO containing the details of the updated book
     * @throws NotFoundException if no book is found with the given ID
     */
    public BookDTO updateBook(Long id, BookUpdateDTO request) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setIsbn(request.getIsbn());
        book.setPublicationYear(request.getPublicationYear());
        book.setUpdatedAt(LocalDateTime.now());
        book.setPrice(BigDecimal.valueOf(request.getPrice()));
        return bookMapper.toDTO(bookRepository.save(book));
    }
}
