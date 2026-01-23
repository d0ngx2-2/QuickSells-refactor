package com.example.quicksells.domain.item.repository;

import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.search.repository.SearchCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>,ItemCustomRepository, SearchCustomRepository {

    //생성 시 중복 검증에서 사용
    boolean existsBySellerIdAndName(Long id, String name);

    // 상세조회 검증 시 상용
    Optional<Item> findById(Long itemId);

    //상품 수정 중복 검증 시 사용
    boolean existsByNameAndIdNot(String name, Long id);
}
