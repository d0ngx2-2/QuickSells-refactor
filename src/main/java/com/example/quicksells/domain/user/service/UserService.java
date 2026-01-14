package com.example.quicksells.domain.user.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.model.request.UserUpdateRequest;
import com.example.quicksells.domain.user.model.response.UserGetResponse;
import com.example.quicksells.domain.user.model.response.UserUpdateResponse;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 내 정보 조회 기능
     *
     * @return 내 페이지 정보
     * @throws CustomException 사용자 체크
     */
    @Transactional(readOnly = true)
    public UserGetResponse getMyPage(AuthUser authUser) {

        // 사용자 체크
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        UserGetResponse response = UserGetResponse.from(user);

        return response;

    }

    /**
     * 내 정보 수정 기능
     *
     * @param request 내 정보 수정 요청 정보
     * @return 변경된 내 정봊
     * @throws CustomException 사용자 체크, 요청에 값이없다면 예외, 전화번호 중복 예외
     */
    @Transactional
    public UserUpdateResponse update(AuthUser authUser, UserUpdateRequest request) {

        // 사용자 체크
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        // request 값이 비었을 경우 예외
        if (request.getPassword() == null && request.getPhone() == null && request.getAddress() == null) {

            throw new CustomException(ExceptionCode.NO_UPDATE_FIELD);
        }

        // 비밀번호 변경
        if (request.getPassword() != null) {
            String encoded = passwordEncoder.encode(request.getPassword());
            user.updatePassword(encoded);
        }

        // 전화번호 변경
        if (request.getPhone() != null) {
            if (userRepository.existsByPhone(request.getPhone())) {
                throw new CustomException(ExceptionCode.EXISTS_PHONE);
            }
            user.updatePhone(request.getPhone());
        }

        // 주소 변경
        if (request.getAddress() != null) {
            user.updateAddress(request.getAddress());
        }

        UserUpdateResponse response = UserUpdateResponse.from(user);

        return response;
    }
}
