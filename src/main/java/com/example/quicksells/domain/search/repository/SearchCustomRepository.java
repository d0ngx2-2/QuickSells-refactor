package com.example.quicksells.domain.search.repository;

import com.example.quicksells.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchCustomRepository {
    //검색전용
    Page<Item> searchItems(String keyword, Pageable pageable);
}
