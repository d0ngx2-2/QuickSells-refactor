package com.example.quicksells.domain.search.service;


import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.item.entity.Item;

import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.search.entity.Search;
import com.example.quicksells.domain.search.model.response.SearchGetResponse;
import com.example.quicksells.domain.search.repository.SearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Page<SearchGetResponse> search(String keyword, Pageable
            pageable) {

        //keyword -> 공백 처리
        String searchKeyword = keyword == null ? "" : keyword.trim();
        if (searchKeyword.isEmpty()) {
            throw new RuntimeException("검색어 입력은 필수입니다.");
        }

        //상품 검색
        Page<Item> items = itemRepository.findByNameContaining(searchKeyword, pageable);

        //DB에 저장
        recordKeyword(searchKeyword);

        //DTO 변환
        return items.map(SearchGetResponse::from);
    }

    private void recordKeyword(String keyword) {
        Search search = searchRepository.findByKeyword(keyword)
                .orElseGet(() -> new Search(keyword));

        //카운트 +1 증가
        search.increase();
        searchRepository.save(search);
    }
}
