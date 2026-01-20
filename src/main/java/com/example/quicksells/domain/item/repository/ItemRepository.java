package com.example.quicksells.domain.item.repository;

import com.example.quicksells.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    //생성 시 중복 검증에서 사용
    boolean existsByUserIdAndName(Long id, String name);

    // 상세조회 검증 시 상용
    Optional<Item> findById(Long itemId);

    //상품 목록 조회 검증 시 사용
    Page<Item> findAll(Pageable pageable);

    //상품 삭제 안된 상품 검색 시 사용
    Page<Item> findByNameContaining(String keyword, Pageable pageable);
    //상품 수정 중복 검증 시 사용
    boolean existsByNameAndIdNot(String name, Long id);

}
