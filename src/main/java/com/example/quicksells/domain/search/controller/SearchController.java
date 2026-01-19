package com.example.quicksells.domain.search.controller;

import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.item.model.response.ItemGetListResponse;

import com.example.quicksells.domain.item.service.ItemService;

import com.example.quicksells.domain.search.model.response.SearchGetResponse;
import com.example.quicksells.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    //search 조회
    @GetMapping("/item/searchs")
    public ResponseEntity<PageResponse> keywordGet(@RequestParam String keyword, @PageableDefault(page = 0, size = 10) Pageable pageable)
    {
        Page<SearchGetResponse> responsesDto = searchService.search(keyword, pageable);

        PageResponse responses = PageResponse.success("상품 검색 결과입니다.", responsesDto);

        return ResponseEntity.ok(responses);

    }
}
