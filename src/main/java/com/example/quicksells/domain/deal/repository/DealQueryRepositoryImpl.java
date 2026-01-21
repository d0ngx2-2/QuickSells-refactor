package com.example.quicksells.domain.deal.repository;

import com.example.quicksells.domain.deal.model.response.DealGetAllQueryResponse;
import com.example.quicksells.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static com.example.quicksells.domain.deal.entity.QDeal.deal;
import static com.example.quicksells.domain.item.entity.QItem.item;

@RequiredArgsConstructor
public class DealQueryRepositoryImpl implements DealQueryRepository{

    private JPAQueryFactory queryFactory;

    @Override
    public Page<DealGetAllQueryResponse> findPurchaseDeals(Long buyerId, Pageable pageable) {
        return fetchDeals(deal.buyer.id.eq(buyerId), pageable);
    }

    @Override
    public Page<DealGetAllQueryResponse> findSaleDeals(Long sellerId, Pageable pageable) {
        return fetchDeals(deal.seller.id.eq(sellerId), pageable);
    }

    @Override
    public Page<DealGetAllQueryResponse> findAllDeals(Pageable pageable) {
        return fetchDeals(null, pageable);
    }

    private Page<DealGetAllQueryResponse> fetchDeals(
            BooleanExpression condition,
            Pageable pageable
    ) {

        // 🔽 static import 기반 별칭
        QUser seller = new QUser("seller");
        QUser buyer = new QUser("buyer");

        List<DealGetAllQueryResponse> content = queryFactory
                .select(Projections.constructor(
                        DealGetAllQueryResponse.class,
                        deal.id,
                        deal.type,
                        deal.dealPrice,
                        deal.status,
                        deal.createdAt,

                        item.id,
                        item.name,

                        seller.id,
                        seller.name,

                        buyer.id,
                        buyer.name
                ))
                .from(deal)
                .join(deal.item, item)
                .join(deal.seller, seller)
                .leftJoin(deal.buyer, buyer)
                .where(condition)
                .orderBy(deal.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(deal.count())
                .from(deal)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
