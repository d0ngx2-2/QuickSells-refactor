package com.example.quicksells.domain.wishlist.repository;

import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.wishlist.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    // 동일한 유저ID 상품ID의 존재 여부
    boolean existsByUserAndItem(@Param("userId") User user, @Param("itemId") Item item);

    // 특정 구매자의 생성일 기준으로 관심 목록 조회
    Optional<List<WishList>> findAllByUserIdOrderByCreatedAtDesc(@Param("buyerId") Long buyerId);
}
