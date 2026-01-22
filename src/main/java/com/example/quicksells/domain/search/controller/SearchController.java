package com.example.quicksells.domain.search.controller;

import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.search.model.response.SearchGetResponse;
import com.example.quicksells.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * 상품검색 API
     *
     * @param keyword  검색어
     * @param pageable 페이징
     * @return 페이징된 상품 결과 검색
     */
    @GetMapping("/item/searchs")
    public ResponseEntity<PageResponse> keywordGet(@AuthenticationPrincipal AuthUser authUser, @RequestParam String keyword, @PageableDefault(page = 0, size = 10) Pageable pageable) {

        //비지니스 로직
        Page<SearchGetResponse> responsesDto = searchService.search(authUser, keyword, pageable);

        //응답 값
        return ResponseEntity.status(HttpStatus.OK).body(PageResponse.success("검색 결과입니다.",responsesDto));
    }
}
