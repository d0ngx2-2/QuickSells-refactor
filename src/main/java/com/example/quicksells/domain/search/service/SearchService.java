package com.example.quicksells.domain.search.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.search.model.response.SearchGetResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SearchService {

    private final KeywordService keywordService;
    private final ItemRepository itemRepository;


    @Transactional
    public Page<SearchGetResponse> search(String keyword, Pageable
            pageable) {

        String searchKeyword = safeKeyword(keyword);

        //상품 검색
        Page<Item> items = itemRepository.findByNameContaining(searchKeyword, pageable);

        //DB에 저장
        keywordService.recordKeyword(searchKeyword);

        //DTO 변환
        return items.map(SearchGetResponse::from);
    }

    private String safeKeyword (String keyword){

        //공백제거(삼항 연산자)
        //keyword가 null이면 -> 문자열로 반환, keyword가 있을 시 앞뒤 공백 제거
        String searchKeyword = keyword== null?"":keyword.trim();

        //공백 시 에러 발생
        if (searchKeyword.isEmpty()) {
            //비어 있으면 예외 터짐
            throw new CustomException(ExceptionCode.INVALID_SEARCH_KEYWORD);
        }
        //공백 제거된 검색어 반환
        return searchKeyword;
    }
}
