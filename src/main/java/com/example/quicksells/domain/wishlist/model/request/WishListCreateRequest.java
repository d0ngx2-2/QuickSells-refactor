package com.example.quicksells.domain.wishlist.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WishListCreateRequest {

    @NotNull(message = "구매자는 필수입니다.")
    private Long buyerId;
    @NotNull(message = "아이템은 필수입니다")
    private Long itemId;
}
