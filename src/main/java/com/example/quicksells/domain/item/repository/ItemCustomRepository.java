package com.example.quicksells.domain.item.repository;

import com.example.quicksells.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemCustomRepository {

    //상품 목록 조회
    Page<Item> findItemList(Pageable pageable);

    //상품 상세 조회
    Optional<Item> findItemDetail(Long id);

    //검색 기능 조회
    Page<Item> searchItems(String keyword, Pageable pageable);
}
