package com.interswitch.bookstore.mappers;

import com.interswitch.bookstore.domains.Book;
import com.interswitch.bookstore.requests.BookAddDTO;
import com.interswitch.bookstore.responses.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target="createdAt", source = "domain.createdAt",
            dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target="price", source = "domain.price")
    BookDTO toDTO(Book domain);

    Book toEntity(BookAddDTO dto);
}
