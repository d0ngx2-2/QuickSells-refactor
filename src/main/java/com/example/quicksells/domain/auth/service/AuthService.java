package com.example.quicksells.domain.auth.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.common.util.PasswordEncoder;
import com.example.quicksells.domain.auth.model.request.UserCreateRequest;
import com.example.quicksells.domain.auth.model.response.UserCreateResponse;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 기능
     * @param request 회원가입 요청 정보
     * @return 회원가입 완료된 사용자 정보
     * @throws CustomException 이메일 또는 전화번호가 이미 존재하는 경우
     */
    @Transactional
    public UserCreateResponse createUser(UserCreateRequest request) {

        // 이메일, 전화번호 중복 데이터 여부 체크
        boolean exitsEmail = userRepository.existsByEmail(request.getEmail());

        if (exitsEmail) throw new CustomException(ExceptionCode.EXISTS_EMAIL);

        boolean existsPhone = userRepository.existsByPhone(request.getPhone());

        if (existsPhone) throw new CustomException(ExceptionCode.EXISTS_PHONE);

        // 회원가입 요청 저장
        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getName(), request.getPhone(), request.getAddress(), request.getBirth());

        User savedUser = userRepository.save(user);

        UserCreateResponse response = UserCreateResponse.from(savedUser);

        return response;
    }
}
