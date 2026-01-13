package com.example.quicksells.domain.appraise.repository;

import com.example.quicksells.domain.appraise.entity.Appraise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppraiseRepository extends JpaRepository<Appraise, Long> {
}
