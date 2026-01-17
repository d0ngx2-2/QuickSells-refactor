package com.example.quicksells.domain.information.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.information.model.request.InformationCreateRequest;
import com.example.quicksells.domain.information.model.response.InformationCreateResponse;
import com.example.quicksells.domain.information.service.InformationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InformationController {

    private final InformationService informationService;


    @PostMapping("/admin/informations")
    public ResponseEntity<CommonResponse> create(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody InformationCreateRequest request) {

        InformationCreateResponse response = informationService.create(authUser, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("게시글 생성 성공하셨습니다.", response));
    }


}
