package com.example.quicksells.domain.search.service;

import com.example.quicksells.common.enums.AppraiseStatus;
import com.example.quicksells.common.enums.AuctionStatusType;
import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.search.model.response.SearchGetResponse;
import com.example.quicksells.domain.search.repository.SearchCustomRepositoryImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchService {

    private final SearchRankingSnapshotService searchRankingSnapshotService;
    private final SearchCacheService searchCacheService;
    private final SearchCustomRepositoryImpl searchCustomRepositoryImpl;

    /**
     * 상품 검색을 처리하는 메인 서비스
     *
     * @param keyword  사용자가 입력한 검색어
     * @param pageable 페이징 정보
     * @return 상품 목록 검색 결과
     */
    @Transactional(readOnly = true)
    public Page<SearchGetResponse> search(AuthUser authUser, String keyword, List<AppraiseStatus> appraiseStatus, List<AuctionStatusType>auctionStatus, Pageable pageable) {

        //로그인 예외처리
        if (authUser == null) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_SEARCH);
        }

        //검색어 공백, null 방지
        String searchKeyword = safeKeyword(keyword);

        // 검색어 중복 방지
        searchCacheService.notDoubleClick(String.valueOf(authUser.getId()),searchKeyword);

        //관리자 체크
        boolean isAdmin = authUser.getRole().name().equals("ADMIN");

        //판매자 or 구매자(유저)
        Long viewerId = authUser.getId();

        return searchCustomRepositoryImpl.searchItems(searchKeyword,appraiseStatus, auctionStatus, viewerId, isAdmin, pageable);
    }

    /**
     * 검색어 공백 제거
     *
     * @param keyword 사용자가 입력한 키워드
     * @return 공백 제거된 검색어
     */
    private String safeKeyword(String keyword) {

        //공백제거
        //keyword가 null이면 -> 문자열로 반환, keyword가 있을 시 앞뒤 공백 제거
        String searchKeyword = keyword == null ? "" : keyword.trim();

        //공백 시 에러 발생
        if (searchKeyword.isEmpty()) {
            //비어 있으면 예외 터짐
            throw new CustomException(ExceptionCode.INVALID_SEARCH_KEYWORD);
        }
        //공백 제거된 검색어 반환
        return searchKeyword;
    }
}

