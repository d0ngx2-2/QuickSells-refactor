package com.example.quicksells.domain.wishlist.repository;

import com.example.quicksells.domain.wishlist.entity.WishList;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import java.util.List;
import static com.example.quicksells.domain.wishlist.entity.QWishList.wishList;

@RequiredArgsConstructor
public class WishListCustomRepositoryImpl implements WishListCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<WishList> myWishListSearch(Long buyerId, Pageable pageable) {

        List<WishList> contents = jpaQueryFactory
                .selectFrom(wishList)
                .where(isOwner(buyerId))
                .orderBy(wishList.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 마지막 슬라이스 조회후 다음 페이지 첫 슬라이스 까지 조회
                .fetch();

        boolean hasNext = contents.size() > pageable.getPageSize(); // 다음 페이지 첫 슬라이스의 존재 유무

        if (hasNext) {
            contents.remove(contents.size() - 1); // 다음 페이지 첫 슬라이스는 삭제
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    public BooleanExpression isOwner(Long buyerId) {

        return buyerId != null ? wishList.buyer.id.eq(buyerId) : wishList.buyer.id.isNull();
    }
}
