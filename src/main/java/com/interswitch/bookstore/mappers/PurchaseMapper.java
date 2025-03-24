package com.interswitch.bookstore.mappers;

import com.interswitch.bookstore.domains.Book;
import com.interswitch.bookstore.domains.Purchase;
import com.interswitch.bookstore.domains.PurchaseItem;
import com.interswitch.bookstore.responses.BookDTO;
import com.interswitch.bookstore.responses.PurchaseDTO;
import com.interswitch.bookstore.responses.PurchaseItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    @Mapping(target="createdAt", source = "domain.createdAt",
            dateFormat = "dd-MM-yyyy HH:mm:ss")
    PurchaseDTO toDTO(Purchase domain);

    @Mapping(target="createdAt", source = "domain.createdAt",
            dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target="price", source = "domain.price")
    BookDTO toDTO(Book domain);

    @Mapping(target="createdAt", source = "domain.createdAt",
            dateFormat = "dd-MM-yyyy HH:mm:ss")
    PurchaseItemDTO toDTO(PurchaseItem domain);
}
