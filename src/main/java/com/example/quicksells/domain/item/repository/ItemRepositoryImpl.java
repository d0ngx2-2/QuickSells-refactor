package com.example.quicksells.domain.item.repository;

import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.quicksells.domain.item.entity.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;


    private final QUser seller = QUser.user;


    @Override
    public Page<Item> findItemList(Pageable pageable) {

        List<Item> content = jpaQueryFactory
                .selectFrom(item) //item 찾기
                .join(item.seller,seller).fetchJoin() //판매자 정보 가져오기
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(item.id.desc()) //최신순 정렬 권장
                .fetch();

        Long total = jpaQueryFactory
                .select(item.count()) // 카운트 전체 조회
                .from(item)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total); //데이터 없을 경우 null 방지
    }

    @Override
    public Optional<Item> findItemDetail(Long itemId) {

        //DB에서 아이템을 찾는다
        Item foundItem = jpaQueryFactory
                .selectFrom(item)
                .join(item.seller, seller).fetchJoin()
                .where(item.id.eq(itemId))
                .fetchOne();

        //찾은 아이템을 Optional에 넣어준다
        return Optional.ofNullable(foundItem);
    }

    @Override
    public Page<Item> searchItems(String keyword, Pageable pageable) {

        String safeKeyword = keyword == null ? "" : keyword.trim();

        List<Item> content = jpaQueryFactory
                .selectFrom(item) //item 찾기
                .leftJoin(item.seller,seller).fetchJoin()
                .where(
                        item.isDeleted.eq(false), //삭제된 데이터 제외
                        item.name.containsIgnoreCase(safeKeyword) // 상품 이름 입력 시 대소문자 상관없음
                )
                .offset(pageable.getOffset()) //페이지 시작 위치
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(item.count()) // 카운트 전체 조회
                .from(item)
                .where(
                        item.isDeleted.eq(false),
                        item.name.containsIgnoreCase(safeKeyword)
                )
                .fetchOne(); // 단일값 조회

        return new PageImpl<>(content, pageable, total == null ? 0 : total //데이터 없을 경우 null 방지
        );
    }
}
