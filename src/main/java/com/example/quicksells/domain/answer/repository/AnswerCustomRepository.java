package com.example.quicksells.domain.answer.repository;

import com.example.quicksells.domain.answer.model.response.AnswerGetAllResponse;
import com.example.quicksells.domain.answer.model.response.AnswerGetResponse;

import java.util.List;
import java.util.Optional;

public interface AnswerCustomRepository {

    List<AnswerGetAllResponse> findAllByAdmin();

    List<AnswerGetAllResponse> findAllByUser(Long userId);

    Optional<AnswerGetResponse> findByAskId(Long askId);
}
