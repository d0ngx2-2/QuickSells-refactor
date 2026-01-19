package com.example.quicksells.domain.information.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.information.model.request.InformationCreateRequest;
import com.example.quicksells.domain.information.model.response.InformationCreateResponse;
import com.example.quicksells.domain.information.model.response.InformationGetAllResponse;
import com.example.quicksells.domain.information.model.response.InformationGetResponse;
import com.example.quicksells.domain.information.service.InformationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InformationController {

    private final InformationService informationService;


    @PostMapping("/admin/informations")
    public ResponseEntity<CommonResponse> create(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody InformationCreateRequest request) {

        InformationCreateResponse response = informationService.create(authUser, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("공지사항 생성 성공하셨습니다.", response));
    }

    @GetMapping("/informations/{id}")
    public ResponseEntity<CommonResponse> getOne(@PathVariable Long id) {

        InformationGetResponse response = informationService.getOne(id);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("공지사항 단건 조회 성공하셨습니다.", response));
    }

    @GetMapping("/informations")
    public ResponseEntity<PageResponse> getAll(Pageable pageable){

        Page<InformationGetAllResponse> responses = informationService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(PageResponse.success("공지사항 전체 조회 성공하셨습니다.", responses));
    }
}
