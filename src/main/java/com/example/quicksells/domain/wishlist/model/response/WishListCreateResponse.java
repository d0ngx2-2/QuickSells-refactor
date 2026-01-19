package com.example.quicksells.domain.wishlist.model.response;

import com.example.quicksells.domain.wishlist.entity.WishList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WishListCreateResponse {

    private final Long id;
    private final Long userId;
    private final Long itemId;

    public static WishListCreateResponse from(WishList wishList) {
        return new WishListCreateResponse(
                wishList.getId(),
                wishList.getUser().getId(),
                wishList.getItem().getId()
        );
    }
}
