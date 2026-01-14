package com.example.quicksells.domain.item.repository;

import com.example.quicksells.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByIdAndIsDeletedFalse(Long itemId);
}
