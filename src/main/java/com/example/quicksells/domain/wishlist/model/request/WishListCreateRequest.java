package com.example.quicksells.domain.wishlist.model.request;

import lombok.Getter;

@Getter
public class WishListCreateRequest {

    private Long buyerId;
    private Long itemId;
}
