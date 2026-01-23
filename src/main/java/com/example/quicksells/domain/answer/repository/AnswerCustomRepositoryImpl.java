package com.example.quicksells.domain.answer.repository;

import com.example.quicksells.domain.answer.model.response.AnswerGetAllResponse;
import com.example.quicksells.domain.answer.model.response.AnswerGetResponse;
import com.example.quicksells.domain.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import static com.example.quicksells.domain.answer.entity.QAnswer.answer;
import static com.example.quicksells.domain.ask.entity.QAsk.ask;

@RequiredArgsConstructor
public class AnswerCustomRepositoryImpl implements AnswerCustomRepository{

    private final JPAQueryFactory queryFactory;

    QUser admin = new QUser("admin");

    @Override
    public List<AnswerGetAllResponse> findAllByAdmin() {

        return queryFactory
                .select(Projections.constructor(
                        AnswerGetAllResponse.class,
                        answer.id,
                        ask.id,
                        answer.title,
                        admin.name,
                        answer.createdAt
                ))
                .from(answer)
                .join(answer.ask, ask)
                .join(answer.admin, admin)
                .orderBy(answer.createdAt.desc())
                .fetch();
    }

    @Override
    public List<AnswerGetAllResponse> findAllByUser(Long userId) {

        return queryFactory
                .select(Projections.constructor(
                        AnswerGetAllResponse.class,
                        answer.id,
                        ask.id,
                        answer.title,
                        admin.name,
                        answer.createdAt
                ))
                .from(answer)
                .join(answer.ask, ask)
                .join(answer.admin, admin)
                .where(ask.user.id.eq(userId))
                .orderBy(answer.createdAt.desc())
                .fetch();
    }

    @Override
    public Optional<AnswerGetResponse> findByAskId(Long askId) {

        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(
                                AnswerGetResponse.class,
                                answer.id,
                                ask.id,
                                answer.title,
                                answer.content,
                                admin.name,
                                answer.createdAt
                        ))
                        .from(answer)
                        .join(answer.ask, ask)
                        .join(answer.admin, admin)
                        .where(ask.id.eq(askId))
                        .fetchOne()
        );
    }
}
