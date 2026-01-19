package com.example.quicksells.domain.ask.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.ask.model.request.AskCreateRequest;
import com.example.quicksells.domain.ask.model.response.AskCreateResponse;
import com.example.quicksells.domain.ask.service.AskService;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "문의(ask) 관리")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AskController {

    private final AskService askService;

    /**
     * 문의 생성
     */
    @Operation(summary = "문의 생성")
    @PostMapping("/asks")
    public ResponseEntity<CommonResponse> createAsk(@Valid @RequestBody AskCreateRequest request, @AuthenticationPrincipal AuthUser authUser) {

        AskCreateResponse response = askService.createAsk(request, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("문의 생성에 성공했습니다.", response));
    }

}
