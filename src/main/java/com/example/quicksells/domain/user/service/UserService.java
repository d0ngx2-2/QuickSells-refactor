package com.example.quicksells.domain.user.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.model.response.UserGetResponse;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 내 정보 조회 기능
     * @return 내 페이지 정보
     * @throws CustomException 이메일 존재 여부
     */
    @Transactional(readOnly = true)
    public UserGetResponse getMyPage(AuthUser authUser) {

        // 이메일 체크
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(()->new CustomException(ExceptionCode.NOT_FOUND_USER));

        UserGetResponse response = UserGetResponse.from(user);

        return response;

    }
}
