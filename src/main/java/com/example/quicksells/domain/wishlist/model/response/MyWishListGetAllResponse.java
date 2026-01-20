package com.example.quicksells.domain.wishlist.model.response;

import com.example.quicksells.domain.wishlist.entity.WishList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyWishListGetAllResponse {

    private final Long id;
    private final Long buyerId;
    private final Long itemId;
    private final LocalDateTime createdAt;

    public static List<MyWishListGetAllResponse> from(List<WishList> wishList) {
        return wishList.stream()
                .map(w -> new MyWishListGetAllResponse(
                        w.getId(),
                        w.getUser().getId(),
                        w.getItem().getId(),
                        w.getCreatedAt()
                ))
                .toList();
    }
}
