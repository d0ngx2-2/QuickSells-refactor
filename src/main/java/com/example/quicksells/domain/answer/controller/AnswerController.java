package com.example.quicksells.domain.answer.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.answer.model.request.AnswerCreateRequest;
import com.example.quicksells.domain.answer.model.response.AnswerCreateResponse;
import com.example.quicksells.domain.answer.model.response.AnswerGetResponse;
import com.example.quicksells.domain.answer.service.AnswerService;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "문의 답변(answer)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 답변 생성 API (관리자)
     */
    @PostMapping("/answer/ask/{askId}")
    public ResponseEntity<CommonResponse> createAnswer(@PathVariable Long askId, @Valid AnswerCreateRequest request, @AuthenticationPrincipal AuthUser authUser) {

        AnswerCreateResponse response = answerService.createAnswer(askId, request, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("답변 생성을 완료하였습니다", response));
    }

    /**
     * 답변 상세 조회
     */
    @GetMapping("/answer/ask/{askId}")
    public ResponseEntity<CommonResponse> getAnswer(@PathVariable Long askId) {

        AnswerGetResponse response = answerService.getAnswer(askId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("답변 조회에 성공하였습니다.", response));
    }
}
