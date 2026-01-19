package com.example.quicksells.domain.information.repository;

import com.example.quicksells.domain.information.entity.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationRepository extends JpaRepository<Information, Long> {

    boolean existsByTitle(String title);

    @EntityGraph(attributePaths = "user")
    Page<Information> findAll(Pageable pageable);
}
