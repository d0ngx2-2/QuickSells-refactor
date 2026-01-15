package com.example.quicksells.domain.deal.repository;

import com.example.quicksells.domain.deal.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DealRepository extends JpaRepository<Deal, Long> {

    List<Deal> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);

    List<Deal> findBySellerIdOrderByCreatedAtDesc(Long sellerId);

    List<Deal> findAllByOrderByCreatedAtDesc();
}
