//package com.example.quicksells.domain.search.repository;
//
//import com.example.quicksells.domain.item.entity.Item;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
//import static com.example.quicksells.domain.item.entity.QItem.item;
//import static com.example.quicksells.domain.user.entity.QUser.user;
//
//public class SearchRepositoryImpl implements SearchCustomRepository {
//
//    private final JPAQueryFactory jpaQueryFactory;
//
//    public SearchRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
//        this.jpaQueryFactory = jpaQueryFactory;
//    }
//
//    @Override
//    public Page<Item> searchItems(String keyword, Pageable pageable) {
//
//        String safeKeyword = keyword == null ? "" : keyword.trim();
//
//        List<Item> content = jpaQueryFactory
//                .selectFrom(item) //item 찾기
//                .leftJoin(item.user, user).fetchJoin()
//                .where(
//                        item.isDeleted.eq(false), //삭제된 데이터 제외
//                        item.name.containsIgnoreCase(safeKeyword) // 상품 이름 입력 시 대소문자 상관없음
//                )
//                .offset(pageable.getOffset()) //페이지 시작 위치
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        Long total = jpaQueryFactory
//                .select(item.count()) // 카운트 전체 조회
//                .from(item)
//                .where(
//                        item.isDeleted.eq(false),
//                        item.name.containsIgnoreCase(safeKeyword)
//                )
//                .fetchOne(); // 단일값 조회
//
//        return new PageImpl<>(content, pageable, total == null ? 0 : total //데이터 없을 경우 null 방지
//        );
//    }
//}
