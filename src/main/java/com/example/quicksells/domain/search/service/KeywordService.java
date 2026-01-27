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

    /**
     * 검색어 공백 관리 및 카운트 증가
     *
     * @param keyword 사용자가 입력한 검색어
     */
    @Transactional
    public void recordKeyword(String keyword) {

        //keyword 공백, null방지
        String readKeyword = keyword == null ? "" : keyword.trim();

        //공백 제거해도 잘못된 요청일 경우 예외처리
        if (readKeyword.isEmpty()) {
            throw new RuntimeException("검색어 입력은 필수입니다.");
        }

        //DB에 요청한 키워드가 있는지 조회
        Search search = searchRepository.findByKeyword(readKeyword)
                .orElseGet(() -> new Search(readKeyword));

        //카운트 +1 증가
        search.increase();

        //저장
        searchRepository.save(search);
    }

//    //전체 검색 데이터 삭제
//    @Transactional
//    public void deleteAll() {
//        searchRepository.deleteAll();
//    }
}