package com.example.quicksells.domain.ask.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.ask.entity.Ask;
import com.example.quicksells.domain.ask.model.request.AskCreateRequest;
import com.example.quicksells.domain.ask.model.response.AskCreateResponse;
import com.example.quicksells.domain.ask.repository.AskRepository;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AskService {

    private final AskRepository askRepository;
    private final UserRepository userRepository;

    /**
     * 문의 생성
     */
    @Transactional
    public AskCreateResponse createAsk(AskCreateRequest request, AuthUser authUser) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        Ask ask = new Ask(user, request.getAskType(), request.getTitle(), request.getContent());

        Ask savedAsk = askRepository.save(ask);

        return AskCreateResponse.from(savedAsk);
    }
}
