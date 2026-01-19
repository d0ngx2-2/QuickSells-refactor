package com.example.quicksells.domain.wishlist.repository;

import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.wishlist.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    // 동일한 유저ID 상품ID의 존재 여부
    boolean existsByUserAndItem(@Param("userId") User user, @Param("itemId") Item item);
}
