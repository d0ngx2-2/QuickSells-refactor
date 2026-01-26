package com.example.quicksells.domain.wishlist.repository;

import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.wishlist.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long>, WishListCustomRepository {

    // 동일한 유저ID 상품ID의 존재 여부
    boolean existsByBuyerAndItem(User user, Item item);

    // 특정 구매자의 생성일 기준으로 관심 목록 조회

    List<WishList> findAllByBuyerIdOrderByCreatedAtDesc(Long buyerId);
}
