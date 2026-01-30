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
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final SearchRepository searchRepository;

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

        // Null 방어
        if (count == null || count.isEmpty()) {
            return List.of();
        }

        //Object를 String으로 반환
        return count.stream()
                .map(keyword -> String.valueOf(keyword.toString()))
                .toList();
    }

    /**
     * Redis에 있는 캐시 정보를 10분마다 DB로 복사 및 백업
     */
    @Scheduled(fixedDelay = 600_000) //10분 마다
    @Transactional
    public void snapshotTop10toDB() {

        log.info("[snapshot] start");
        log.info("[snapshot] TICK {}", LocalDateTime.now());

        Long size = redisTemplate.opsForZSet().size(POPULAR_RANKING_KEY);
        log.info("[snapshot] redis zset size={}", size);

        Set<ZSetOperations.TypedTuple<Object>> top10 =
                redisTemplate.opsForZSet().reverseRangeWithScores(POPULAR_RANKING_KEY, 0, 9);

        log.info("[snapshot] {}", top10);

        // 캐시 데이터가 없는 경우
        if (top10 == null || top10.isEmpty()) {
            return;
        }

        //인기 검색어 top10 DB에 스냅샷 저장, (cacheDate = value, score)
        for (ZSetOperations.TypedTuple<Object> cacheData : top10) {

            //레디스 데이터에서 null, 스케줄러 에러 방지
            if (cacheData == null || cacheData.getValue() == null || cacheData.getScore() == null) {
                continue; //이상한 데이터 건너뜀
            }

            //레디스 캐시에서 가져온 값 파싱
            // value = object -> String 변환
            String keyword = cacheData.getValue().toString();

            //count = Double -> Long 변환
            Long count = Math.round(cacheData.getScore());
            log.info("[snapshot] upsert keyword={}, count={}", keyword, count);

            //DB 업데이트 로직 -> 존재하는 키워드 update, 키워드 없는 경우 insert
            keywordService.upsertSnapshot(keyword, count);
            log.info("[snapshot] done");
        }
    }

    /**
     * DB 데이터 삭제 (최신 내역 3일 보관)
     */
    // 3일 마다  DB 삭제
    @Scheduled(cron = "0 0 0 */3 * *", zone = "Asia/Seoul")
    @Transactional
    public void cleanDBSearchLogs() {

        //시간 지정 (3일 전 내역 삭제)
        LocalDateTime threeDay = LocalDateTime.now().minusDays(3);

        //데이터 삭제
        searchRepository.deletedOldLogs(threeDay);
    }
}