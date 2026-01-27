package com.example.quicksells.domain.search.service;

import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchCacheService {

    private final ItemRepository itemRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String POPULAR_RANKING_KEY = "ranking:keyword";
    private final KeywordService keywordService;
    /**
     * 캐시에 적용된 검색 메소드
     *
     * @param keyword  검색어
     * @param pageable 패이징 정보
     * @return 입력한 검색어의 상품 목록
     */
    //DB에서 상품 검색
    @Cacheable(value = "searchResult") //CacheConfig에서 캐시이름
    @Transactional(readOnly = true) //검색어, 페이지 번호, 페이지 사이즈
    public Page<Item> cachedSearch(String keyword, Pageable pageable) {

        return itemRepository.searchItems(keyword, pageable);
    }

    //레디스 조회된 검색어 조회수 증가
    public void increaseViewCount(String keyword) {
        redisTemplate.opsForZSet().incrementScore(POPULAR_RANKING_KEY, keyword, 1);
    }

    //인기 검색어 조회
    public List<String> getPopularKeywordsList() {

        Set<Object> count = redisTemplate.opsForZSet().reverseRange(POPULAR_RANKING_KEY, 0, 9);

        // NPE 방어
        if (count == null || count.isEmpty()) {
            return List.of();
        }

        //Object를 String으로 반환
        return count.stream()
                .map(keyword ->String.valueOf(keyword.toString()))
                .toList();
    }

//    //캐시 및 DB 동시 삭제 메소드(관리자 전용)
//    public void clearRankingCache() {
//
//        //DB 키워드 삭제
//        keywordService.deleteAll();
//
//        // Redis 비우기
//        redisTemplate.delete(POPULAR_RANKING_KEY);
//
//        //삭제 멘트
//        log.info("삭제완료");
//    }
}