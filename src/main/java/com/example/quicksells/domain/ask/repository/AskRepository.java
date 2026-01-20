package com.example.quicksells.domain.ask.repository;

import com.example.quicksells.domain.ask.entity.Ask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AskRepository extends JpaRepository<Ask, Long> {

    // 문의 전체 조회 (페이징)
    @Query("SELECT a FROM Ask a ORDER BY a.createdAt DESC")
    Page<Ask> findAllAsks(Pageable pageable);

    // 특정 유저의 문의 목록 조회 (페이징)
    @Query("SELECT a FROM Ask a WHERE a.user.id = :userId ORDER BY a.createdAt DESC")
    Page<Ask> findByUserId(Long userId, Pageable pageable);
}
