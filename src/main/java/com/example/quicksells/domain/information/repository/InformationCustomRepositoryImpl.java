package com.example.quicksells.domain.information.repository;

import com.example.quicksells.domain.information.entity.Information;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.quicksells.domain.information.entity.QInformation.information;
import static com.example.quicksells.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class InformationCustomRepositoryImpl implements InformationCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Information> findInformationPageSummary(Pageable pageable) {

        List<Information> content = jpaQueryFactory
                .selectFrom(information)
                .leftJoin(information.user, user).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(information.createdAt.desc())
                .fetch();

        Long total = jpaQueryFactory
                .select(information.count())
                .from(information)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }
}
