package com.example.quicksells.domain.deal.repository;

import com.example.quicksells.domain.deal.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
