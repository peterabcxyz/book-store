package com.interswitch.bookstore.mappers;

import com.interswitch.bookstore.domains.Cart;
import com.interswitch.bookstore.domains.CartItem;
import com.interswitch.bookstore.responses.CartDTO;
import com.interswitch.bookstore.responses.CartItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target="createdAt", source = "domain.createdAt",
            dateFormat = "dd-MM-yyyy HH:mm:ss")
    CartDTO toDTO(Cart domain);

    @Mapping(target="createdAt", source = "domain.createdAt",
            dateFormat = "dd-MM-yyyy HH:mm:ss")
    CartItemDTO toDTO(CartItem domain);
}
