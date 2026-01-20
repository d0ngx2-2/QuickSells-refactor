package com.example.quicksells.domain.information.service;

import com.example.quicksells.common.aws.service.S3Service;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    /**
     * 공지사항 생성 기능
     *
     * @param request 공지사항 생성 요청 정보
     * @return 생성된 공지사항
     * @throws CustomException 관리자 체크, 공지사항 제목 존재 여부
     */
    @Transactional
    public InformationCreateResponse create(AuthUser authUser, InformationCreateRequest request, MultipartFile image) {

        // 관리자 체크
        User admin = findAdminOrException(authUser);

        // 공지사항 제목 체크
        boolean exitsTitle = informationRepository.existsByTitle(request.getTitle());

        if (exitsTitle) throw new CustomException(ExceptionCode.EXISTS_INFORMATION_TITLE);

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            imageUrl = s3Service.uploadImage(image);
        }

        Information information = new Information(admin, request.getTitle(), request.getDescription(), imageUrl);

        informationRepository.save(information);

        return InformationCreateResponse.from(information);
    }

    /**
     * 공지사항 단건 조회 기능
     *
     * @return 단건 조회한 공지사항
     * @throws CustomException 공지사항 존재 여부
     */
    @Transactional(readOnly = true)
    public InformationGetResponse getOne(Long informationId) {

        // 공지사항 체크
        Information information = findInformationOrException(informationId);

        return InformationGetResponse.from(information);
    }

    /**
     * 공지사항 전체 조회 기능
     *
     * @return 전체 조회한 공지사항
     */
    @Transactional(readOnly = true)
    public Page<InformationGetAllResponse> getAll(Pageable pageable) {

        return informationRepository.findAll(pageable)
                .map(InformationGetAllResponse::from);
    }

    /**
     * 공지사항 수정 기능
     *
     * @param request 공지사항 수정 요청 정보
     * @return 수정된 공지사항
     * @throws CustomException 관리자 체크, 공지사항 체크
     */
    @Transactional
    public InformationUpdateResponse update(AuthUser authUser, Long informationId, InformationUpdateRequest request, MultipartFile image) {

        // request 값이 비었을 경우 예외
        if (request.isAllFieldEmpty() && (image == null || image.isEmpty())) {
            throw new CustomException(ExceptionCode.NO_UPDATE_FIELD);
        }

        // 관리자 체크
        findAdminOrException(authUser);

        // 공지사항 체크
        Information information = findInformationOrException(informationId);

        // 제목 변경
        if (request.getTitle() != null && !request.getTitle().equals(information.getTitle())) {

            if (informationRepository.existsByTitle(request.getTitle())) {
                throw new CustomException(ExceptionCode.EXISTS_INFORMATION_TITLE);
            }
            information.updateTitle(request.getTitle());
        }

        // 내용 변경
        if (request.getDescription() != null) {
            information.updateDescription(request.getDescription());
        }

        // 사진 변경
        handleImageUpdate(information, image, request.isDeleteImage());

        return InformationUpdateResponse.from(information);
    }

    /**
     * 공지사항 삭제 기능
     *
     * @throws CustomException 관리자 체크, 공지사항 체크
     */
    @Transactional
    public void delete(AuthUser authUser, Long informationId) {

        // 관리자 체크
        findAdminOrException(authUser);

        // 공지사항 체크
        Information information = findInformationOrException(informationId);

        information.delete();
    }

    private Information findInformationOrException(Long informationId) {

        return informationRepository.findById(informationId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_INFORMATION));
    }

    private User findAdminOrException(AuthUser authUser) {

        return userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ADMIN));
    }

    // 이미지 변경 메서드
    private void handleImageUpdate(Information information, MultipartFile image, boolean deleteImage) {

        // 이미지 삭제 요청
        if (deleteImage) {
            if (information.getImageUrl() != null) {
                s3Service.deleteImage(information.getImageUrl());
                information.removeImage();
            }
            return;
        }

        // 이미지 변경 요청
        if (image != null && !image.isEmpty()) {
            if (information.getImageUrl() != null) {
                s3Service.deleteImage(information.getImageUrl());
            }

            String imageUrl = s3Service.uploadImage(image);
            information.updateImage(imageUrl);
        }
    }
}
