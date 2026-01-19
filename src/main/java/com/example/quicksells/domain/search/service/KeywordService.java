package com.example.quicksells.domain.search.service;

import com.example.quicksells.domain.search.repository.SearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.quicksells.domain.search.entity.Search;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor

public class KeywordService {

    private final SearchRepository searchRepository;

    // 공백 관리, 키워드 카운트 증가
    @Transactional
    public void recordKeyword(String keyword) {

        //keyword -> 공백 처리
        String readKeyword = keyword == null ? "" : keyword.trim();
        if (readKeyword.isEmpty()) {
            throw new RuntimeException("검색어 입력은 필수입니다.");
        }

        Search search = searchRepository.findByKeyword(keyword)
                .orElseGet(() -> new Search(keyword));

        //카운트 +1 증가
        search.increase();
        searchRepository.save(search);
    }


}
