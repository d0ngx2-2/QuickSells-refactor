package com.example.quicksells.domain.information.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.information.entity.Information;
import com.example.quicksells.domain.information.model.request.InformationCreateRequest;
import com.example.quicksells.domain.information.model.request.InformationUpdateRequest;
import com.example.quicksells.domain.information.model.response.InformationCreateResponse;
import com.example.quicksells.domain.information.model.response.InformationGetAllResponse;
import com.example.quicksells.domain.information.model.response.InformationGetResponse;
import com.example.quicksells.domain.information.model.response.InformationUpdateResponse;
import com.example.quicksells.domain.information.repository.InformationRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;
    private final UserRepository userRepository;

    @Transactional
    public InformationCreateResponse create(AuthUser authUser, InformationCreateRequest request) {

        User admin = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ADMIN));

        boolean exitsTitle = informationRepository.existsByTitle(request.getTitle());

        if (exitsTitle) throw new CustomException(ExceptionCode.EXISTS_INFORMATION_TITLE);

        Information information = new Information(admin, request.getTitle(), request.getDescription(), request.getImageUrl());

        informationRepository.save(information);

        return InformationCreateResponse.from(information);
    }

    @Transactional(readOnly = true)
    public InformationGetResponse getOne(Long informationId) {

        Information information = informationRepository.findById(informationId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_INFORMATION));

        return InformationGetResponse.from(information);
    }

    @Transactional(readOnly = true)
    public Page<InformationGetAllResponse> getAll(Pageable pageable) {

        return informationRepository.findAll(pageable)
                .map(InformationGetAllResponse::from);
    }

    @Transactional
    public InformationUpdateResponse update(Long informationId, InformationUpdateRequest request) {

        Information information = informationRepository.findById(informationId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_INFORMATION));

        information.update(request.getTitle(), request.getDescription(), request.getImageUrl());

        return InformationUpdateResponse.from(information);
    }
}
