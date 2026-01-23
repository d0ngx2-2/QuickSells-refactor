package com.example.quicksells.domain.deal.repository;

import com.example.quicksells.common.enums.StatusType;
import com.example.quicksells.domain.deal.entity.Deal;
import com.example.quicksells.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long>, DealCustomRepository {
    // 특정 Item에 대한 Deal 조회 (Item-Deal 1:1 관계)
    Optional<Deal> findByItem(Item item);

    // 이 아이템의 현재 판매/경매 중 Deal 찾기
    Optional<Deal> findTopByItemAndStatusOrderByCreatedAtDesc(Item item, StatusType status);
}
