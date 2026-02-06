package com.example.quicksells.domain.search.repository;

import com.example.quicksells.common.enums.AppraiseStatus;
import com.example.quicksells.common.enums.AuctionStatusType;
import com.example.quicksells.domain.appraise.entity.QAppraise;
import com.example.quicksells.domain.auction.entity.QAuction;
import com.example.quicksells.domain.item.entity.QItem;
import com.example.quicksells.domain.search.model.response.SearchGetResponse;
import com.querydsl.core.BooleanBuilder;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class SearchCustomRepositoryImpl implements SearchCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SearchGetResponse> searchItems(String keyword, List<AppraiseStatus> appraiseStatuses, List<AuctionStatusType> auctionStatus, Long viewrId, boolean isAdmin, Pageable pageable) {
        QItem item = QItem.item;
        QAppraise appraise = QAppraise.appraise;
        QAuction auction = QAuction.auction;


        BooleanBuilder where = new BooleanBuilder(); //BooleanBuilder: 동적으로 where 조건 추가

        //상품 이름으로 검색
        if (keyword != null && !keyword.isBlank()) {
            where.and(item.name.like("%" + keyword.trim() + "%"));
        }

        // 감정 상태 값: 즉시 판매, 경매 진행중
        List<AppraiseStatus> safeAppraiseStatues =
                (appraiseStatuses == null || appraiseStatuses.isEmpty())
                        ? List.of(AppraiseStatus.IMMEDIATE_SELL, AppraiseStatus.AUCTION) : appraiseStatuses;

        //경매 상태 값: 진행 중인 경매
        List<AuctionStatusType> safeAuction =
                (auctionStatus == null || auctionStatus.isEmpty())
                        ? List.of(AuctionStatusType.AUCTIONING)
                        : auctionStatus;

        //관리자 등록부터 전부 조회
        if (!isAdmin) {
            List<AppraiseStatus> safeAppraise =
                    (appraiseStatuses == null || appraiseStatuses.isEmpty())
                            ? List.of(
                            AppraiseStatus.PENDING,
                            // 감정중 enum 있으면 추가: AppraiseStatus.APPRAISING or IN_PROGRESS
                            AppraiseStatus.IMMEDIATE_SELL,
                            AppraiseStatus.AUCTION
                    )
                            : appraiseStatuses;

            // 일반 사용자용 필터
            List<AuctionStatusType> safeAuction1 =
                    (auctionStatus == null || auctionStatus.isEmpty())
                            ? List.of(
                            AuctionStatusType.AUCTIONING,
                            AuctionStatusType.SUCCESSFUL_BID,
                            AuctionStatusType.UNSUCCESSFUL_BID,
                            AuctionStatusType.CANCELED
                    )
                            : auctionStatus;

            //공개 상품 조건 만들기
            BooleanExpression getAppraise =
                    appraise.id.isNotNull()
                            .and(appraise.appraiseStatus.in(safeAppraise));

            //경매 상태 체크
            //감정 상태가 AUCTION이 즉시판매
            // - 감정 상태가 AUCTION이면 → 경매가 존재하고 허용된 경매 상태여야 함
            BooleanExpression getAuction =
                    appraise.appraiseStatus.ne(AppraiseStatus.AUCTION)
                            .or(
                                    appraise.appraiseStatus.eq(AppraiseStatus.AUCTION)
                                            .and(
                                                    auction.id.isNull()
                                                            .or(auction.status.in(safeAuction1))
                                            )
                            );
            BooleanExpression openItem = getAppraise.and(getAuction);

            BooleanExpression myItem = item.seller.id.eq(viewrId);

            // 최종 조건: 공개 상품  or 내 상품
            where.and(openItem.or(myItem));

        }

        // 응답 DTO 필드 설정
        //appraiseStatus 필드 설정
//        Expression<String> appraiseStatusOut = new CaseBuilder()
//                .when(appraise.appraiseStatus.eq(AppraiseStatus.AUCTION))
//                .then(Expressions.nullExpression(String.class))
//                .when(appraise.appraiseStatus.isNotNull())
//                .then(appraise.appraiseStatus.stringValue())  // ← null 체크 추가
//                .otherwise(Expressions.nullExpression(String.class));
//
//        //auctionStatus 필드 설정
//        Expression<String> auctionStatusOut = new CaseBuilder()
//                .when(appraise.appraiseStatus.eq(AppraiseStatus.AUCTION)
//                        .and(auction.id.isNotNull()))
//                .then(auction.status.stringValue())
//                .when(appraise.appraiseStatus.eq(AppraiseStatus.AUCTION)
//                        .and(auction.id.isNull()))
//                        .then(Expressions.constant("AUCTIONING"))
//                        .otherwise(Expressions.nullExpression(String.class));

        // 감정 상태
        Expression<String> appraiseStatusOut = new CaseBuilder()
                .when(auction.id.isNotNull())
                .then(Expressions.nullExpression(String.class))
                .when(appraise.appraiseStatus.isNotNull())
                .then(appraise.appraiseStatus.stringValue())
                .otherwise(Expressions.nullExpression(String.class));

// 경매 상태
        Expression<String> auctionStatusOut = new CaseBuilder()
                .when(auction.id.isNotNull())
                .then(auction.status.stringValue())
                .otherwise(Expressions.nullExpression(String.class));


        // 쿼리 실행
        List<SearchGetResponse> itemList = queryFactory
                // 중복 제거 -> selectDistinct
                .selectDistinct(Projections.constructor(
                        SearchGetResponse.class,
                        item.id,
                        item.name,
                        appraiseStatusOut, //감정 상태
                        auctionStatusOut   //경매 상태
                ))
                .from(item)
                .leftJoin(appraise).on(appraise.item.eq(item))
                .leftJoin(auction).on(auction.appraise.eq(appraise))
                .where(where)
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //전체 개수 조회
        JPAQuery<Long> countQuery = queryFactory
                .select(item.count())
                .from(item)
                .leftJoin(appraise).on(appraise.item.eq(item))
                .leftJoin(auction).on(auction.appraise.eq(appraise))
                .where(where);

        return PageableExecutionUtils.getPage(itemList, pageable, countQuery::fetchOne);
    }
}
//ERD 감정 경매 이미지 캡처
// 연관관계 더미 데이터 팬딩 즉시판매 = 경매 n
// 경매 상태인경우 팬딩, 낙찰, 유찰 경매 상태가 나와야한다.
//상품 검색 시 감정이나 경매된 상품 검색 하려는데 상태 반영해서 더미 데이터 만들어줘 -> insert문 DB 쿼리 문으로 넣어줘
// 유저 상품
// 파라미터 넣을 경우 커스텀