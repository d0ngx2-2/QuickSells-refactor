package com.example.quicksells.domain.ask.repository;

import com.example.quicksells.domain.ask.entity.Ask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AskRepository extends JpaRepository<Ask, Long> {
}
