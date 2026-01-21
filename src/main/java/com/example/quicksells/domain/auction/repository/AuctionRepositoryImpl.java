package com.example.quicksells.domain.auction.repository;

import com.example.quicksells.domain.auction.entity.Auction;
import com.example.quicksells.domain.auction.model.request.AuctionSearchFilterRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import java.util.List;
import static com.example.quicksells.domain.appraise.entity.QAppraise.appraise;
import static com.example.quicksells.domain.auction.entity.QAuction.auction;
import static com.example.quicksells.domain.item.entity.QItem.item;

@Slf4j
@RequiredArgsConstructor
public class AuctionRepositoryImpl implements AuctionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<Auction> auctionSearch(Pageable pageable, AuctionSearchFilterRequest request) {

        BooleanBuilder builder = new BooleanBuilder(); // 동적 쿼리를 적용하기 위한 빌더 객체

        // 검색 필터 객체가 널이 아닐 때
        if (request != null) {
            // 감정 아이템이름이 null이거나 공백이 아닐 떄
            if (StringUtils.hasText(request.getAppraiseItemName())) {
                // 요청한 아이템 이름과 경매의 감정된 아이템의 겹치는
                builder.and(auction.appraise.item.name.contains(request.getAppraiseItemName()));
            }

            // 경매 사이 조건 입찰 가격이 두 개 다 null 아닐 때
            if (request.getMinBidPrice() != null && request.getMaxBidPrice() != null) {
                // 최소 입찰 가격과 최대 입찰 가격의 사이
                builder.and(auction.bidPrice.between(request.getMinBidPrice(), request.getMaxBidPrice()));
            }
        }

        List<Auction> content = jpaQueryFactory
                .selectFrom(auction)
                .leftJoin(auction.appraise, appraise).fetchJoin() // fetchJoin -> n+1 방지
                .leftJoin(appraise.item, item).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(auction.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(auction.count())
                .from(auction);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
