package com.example.quicksells.domain.appraise.repository;

import com.example.quicksells.common.enums.AppraiseStatus;
import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static com.example.quicksells.domain.appraise.entity.QAppraise.appraise;
import static com.example.quicksells.domain.item.entity.QItem.item;

@RequiredArgsConstructor
public class AppraiseRepositoryImpl implements AppraiseCustomRepository{

    private final JPAQueryFactory queryFactory;

    // admin (관리자 및 감정사)
    // seller (판매자) 정보 불러오기
    QUser admin = QUser.user;
    QUser seller = QUser.user;

    // 특정 상품에 대한 모든 감정 조회 (페이징, 삭제되지 않은 것만)
    @Override
    public Page<Appraise> findByItemIdWithPaging(Long itemId, Pageable pageable) {

        // 데이터 조회
        List<Appraise> contents = queryFactory
                .selectFrom(appraise)
                .join(appraise.admin, admin).fetchJoin()    // admin 조인
                .join(appraise.item, item).fetchJoin()      // item 조인
                .where(appraise.item.id.eq(itemId))
                .orderBy(appraise.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리 최적화 - Join 없이 단순 카운트
        Long total = queryFactory
                .select(appraise.count())
                .from(appraise)
                .where(appraise.item.id.eq(itemId))
                .fetchOne();

        // Objects.requireNonNullElse : null일 경우 기본 값 리턴
        return new PageImpl<>(contents, pageable, Objects.requireNonNullElse(total, 0L));
    }

    // 특정 상품에 대한 특정 감정 조회 (삭제되지 않은 것만)
    @Override
    public Optional<Appraise> findByIdAndItemId(Long appraiseId, Long itemId) {

        Appraise result = queryFactory
                .selectFrom(appraise)
                .join(appraise.admin, admin).fetchJoin()
                .join(appraise.item, item).fetchJoin()
                .where(
                        appraise.id.eq(appraiseId),
                        appraise.item.id.eq(itemId)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    // 관리자가 감정한 상품 목록 전체 조회
    @Override
    public Page<Appraise> findByAppraiserIdWithItemAndSeller(Long appraiserId, AppraiseStatus status, Pageable pageable) {

        // 동적 쿼리 - status 필터링 (감정진행중/즉시판매/경매)
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(appraise.admin.id.eq(appraiserId));

        if (status != null) {
            builder.and(appraise.appraiseStatus.eq(status));
        }

        // 조회 쿼리
        List<Appraise> contents = queryFactory
                .selectFrom(appraise)
                .join(appraise.item, item).fetchJoin()
                .join(item.seller, seller).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(appraise.createdAt.desc())
                .fetch();

        // 카운트 쿼리
        Long total = queryFactory
                .select(appraise.count())
                .from(appraise)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(contents, pageable, Objects.requireNonNullElse(total, 0L));
    }

    // 관리자가 감정한 감정 상세 조회
    @Override
    public Optional<Appraise> findByIdWithItemAndSeller(Long appraiseId) {

        Appraise result = queryFactory
                .selectFrom(appraise)
                .join(appraise.item, item).fetchJoin()
                .join(item.seller, seller).fetchJoin()
                .where(appraise.id.eq(appraiseId))
                .fetchOne();

        return Optional.ofNullable(result);
    }


}
