package com.example.quicksells.domain.deal.repository;

import com.example.quicksells.domain.deal.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long>, DealCustomRepository {

    Optional<Deal> findByAppraiseId(Long appraiseId);
}
