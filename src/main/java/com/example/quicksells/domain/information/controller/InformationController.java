package com.example.quicksells.domain.information.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.information.model.request.InformationCreateRequest;
import com.example.quicksells.domain.information.model.request.InformationUpdateRequest;
import com.example.quicksells.domain.information.model.response.InformationCreateResponse;
import com.example.quicksells.domain.information.model.response.InformationGetAllResponse;
import com.example.quicksells.domain.information.model.response.InformationGetResponse;
import com.example.quicksells.domain.information.model.response.InformationUpdateResponse;
import com.example.quicksells.domain.information.service.InformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "공지사항(information) 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InformationController {

    private final InformationService informationService;

    /**
     * 공지사항 생성 API
     * hasRole(ADMIN)
     *
     * @param request 공지사항 생성 요청 정보
     */
    @Operation(summary = "공지사항 생성(관리자)")
    @PostMapping(value = "/admin/informations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> create(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestPart("request") InformationCreateRequest request, @RequestPart(value = "image", required = false) MultipartFile image) {

        InformationCreateResponse response = informationService.create(authUser, request, image);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("공지사항 생성 성공하셨습니다.", response));
    }

    /**
     * 공지사항 단건 조회 API
     *
     */
    @Operation(summary = "공지사항 단건 조회")
    @GetMapping("/informations/{id}")
    public ResponseEntity<CommonResponse> getOne(@PathVariable Long id) {

        InformationGetResponse response = informationService.getOne(id);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("공지사항 단건 조회 성공하셨습니다.", response));
    }

    /**
     * 공지사항 전제 조회 API
     *
     */
    @Operation(summary = "공지사항 전체 조회")
    @GetMapping("/informations")
    public ResponseEntity<PageResponse> getAll(Pageable pageable){

        Page<InformationGetAllResponse> responses = informationService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(PageResponse.success("공지사항 전체 조회 성공하셨습니다.", responses));
    }

    /**
     * 공지사항 수정 API
     * hasRole(ADMIN)
     *
     * @param request 공지사항 수정 요청 정보
     */
    @Operation(summary = "공지사항 수정(관리자)")
    @PatchMapping(value = "/admin/informations/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> update(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long id, @Valid @RequestPart(value = "request", required = false) InformationUpdateRequest request, @RequestPart(value = "image", required = false) MultipartFile image) {

        InformationUpdateResponse response = informationService.update(authUser, id, request, image);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("공지사항 수정 성공하셨습니다.", response));
    }

    /**
     * 공지사항 삭제 API
     * hasRole(ADMIN)
     *
     */
    @Operation(summary = "공지사항 삭제(관리자)")
    @DeleteMapping("/admin/informations/{id}")
    public ResponseEntity<CommonResponse> delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long id) {

        informationService.delete(authUser, id);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("공지사항 삭제 성공하셨습니다."));
    }
}
