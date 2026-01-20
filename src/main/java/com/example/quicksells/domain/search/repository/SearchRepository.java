package com.example.quicksells.domain.search.repository;

import com.example.quicksells.domain.search.entity.Search;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long> {
    Optional<Search> findByKeyword(String keyword);
}
