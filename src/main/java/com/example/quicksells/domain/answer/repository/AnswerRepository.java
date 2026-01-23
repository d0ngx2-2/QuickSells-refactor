package com.example.quicksells.domain.answer.repository;

import com.example.quicksells.domain.answer.entity.Answer;
import com.example.quicksells.domain.ask.entity.Ask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerCustomRepository {

    boolean existsByAsk(Ask ask);

//    Optional<Answer> findByAskId(Long askId);

    List<Answer> findAllByOrderByCreatedAtDesc();
}
