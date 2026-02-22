package com.example.quicksells.domain.wishlist.repository;

import com.example.quicksells.domain.wishlist.entity.WishList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface WishListCustomRepository {

    Slice<WishList> myWishListSearch(Long buyerId, Pageable pageable);
}
