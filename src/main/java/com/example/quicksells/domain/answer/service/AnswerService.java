package com.example.quicksells.domain.answer.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.UserRole;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.answer.entity.Answer;
import com.example.quicksells.domain.answer.model.request.AnswerCreateRequest;
import com.example.quicksells.domain.answer.model.response.AnswerCreateResponse;
import com.example.quicksells.domain.answer.repository.AnswerRepository;
import com.example.quicksells.domain.ask.entity.Ask;
import com.example.quicksells.domain.ask.repository.AskRepository;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final AskRepository askRepository;
    private final UserRepository userRepository;

    /**
     * 답변 생성(관리자)
     */
    @Transactional
    public AnswerCreateResponse createAnswer(Long askId, AnswerCreateRequest request, AuthUser authUser) {

        // 관리자 검증
        if (authUser.getRole() != UserRole.ADMIN) {
            throw new CustomException(ExceptionCode.ACCESS_DENIED_ONLY_OWNER);
        }

        // 질문 찾기(Answer로 바꾸기 메세지)
        Ask ask = askRepository.findById(askId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

        // 답변 존재여부 (ANSWER로 바꾸기)
        if (answerRepository.existsByAsk(ask)) {
            throw new CustomException(ExceptionCode.CONFLICT_AUCTION);
        }

        // 관리자 유저 존재여부
        User admin = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        Answer asnwer = new Answer(ask, admin, request.getTitle(), request.getContent());

        // 답변 저장
        Answer savedAnswer = answerRepository.save(asnwer);

        return AnswerCreateResponse.from(savedAnswer);
    }
}
