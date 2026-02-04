package com.example.quicksells.domain.information.service;

import com.example.quicksells.common.aws.service.S3Service;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.information.entity.Information;
import com.example.quicksells.domain.information.model.request.InformationCreateRequest;
import com.example.quicksells.domain.information.model.response.InformationCreateResponse;
import com.example.quicksells.domain.information.model.response.InformationGetAllResponse;
import com.example.quicksells.domain.information.model.response.InformationGetResponse;
import com.example.quicksells.domain.information.repository.InformationRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.example.quicksells.common.enums.UserRole.ADMIN;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InformationServiceTest {

    @Mock
    private InformationRepository informationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private InformationService informationService;

    @Test
    @DisplayName("공지사항 생성 성공")
    void createInformation_success() {

        // given
        AuthUser authUser = new AuthUser(1L, "test@test.com", ADMIN, "홍길동");

        InformationCreateRequest request = new InformationCreateRequest("제목입니다","내용입니다");

        MultipartFile image = mock(MultipartFile.class);

        User admin = mock(User.class);

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(admin));
        when(informationRepository.existsByTitle(request.getTitle())).thenReturn(false);
        when(s3Service.uploadImage(image)).thenReturn("imageUrl");

        // when
        InformationCreateResponse response = informationService.create(authUser, request, image);

        // then
        assertNotNull(response);
        assertThat(response.getTitle()).isEqualTo("제목입니다");
        assertThat(response.getDescription()).isEqualTo("내용입니다");
        assertThat(response.getImageUrl()).isEqualTo("imageUrl");

        verify(informationRepository).save(any(Information.class));
        verify(s3Service, never()).deleteImage(any());
    }

    @Test
    @DisplayName("공지사항 생성 성공 - 이미지 없음")
    void createInformation_success_withoutImage() {

        // given
        AuthUser authUser = new AuthUser(1L, "test@test.com", ADMIN, "홍길동");

        InformationCreateRequest request = new InformationCreateRequest("제목입니다", "내용입니다");

        User admin = mock(User.class);

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(admin));
        when(informationRepository.existsByTitle(request.getTitle())).thenReturn(false);

        // when
        InformationCreateResponse response = informationService.create(authUser, request, null);

        // then
        assertThat(response.getImageUrl()).isNull();
        verify(s3Service, never()).uploadImage(any());
        verify(informationRepository).save(any(Information.class));
    }
    @Test
    @DisplayName("공지사항 생성 실패 - 이미 존재하는 제목")
    void createInformation_fail_existTitle() {

        // given
        AuthUser authUser = new AuthUser(1L, "test@test.com", ADMIN, "홍길동");

        InformationCreateRequest request = new InformationCreateRequest("존재하는 제목입니다","내용입니다");

        MultipartFile image = mock(MultipartFile.class);

        User admin = mock(User.class);

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(admin));
        when(informationRepository.existsByTitle(request.getTitle())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> informationService.create(authUser, request, image))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 존재하는 공지사항 제목입니다.");
    }

    @Test
    @DisplayName("공지사항 생성 실패 - 저장 중 예외 발생 시 이미지 삭제")
    void createInformation_fail_exception_deleteImage() {

        // given
        AuthUser authUser = new AuthUser(1L, "test@test.com", ADMIN, "홍길동");

        InformationCreateRequest request = new InformationCreateRequest("제목입니다","내용입니다");

        MultipartFile image = mock(MultipartFile.class);

        User admin = mock(User.class);

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(admin));
        when(informationRepository.existsByTitle(request.getTitle())).thenReturn(false);
        when(image.isEmpty()).thenReturn(false);
        when(s3Service.uploadImage(image)).thenReturn("imageUrl");

        doThrow(new RuntimeException("DB 오류"))
                .when(informationRepository)
                .save(any(Information.class));

        // when & then
        assertThatThrownBy(() ->
                informationService.create(authUser, request, image))
                .isInstanceOf(CustomException.class)
                .hasMessage("공지사항 생성에 실패했습니다.");

        verify(s3Service).uploadImage(image);
        verify(s3Service).deleteImage("imageUrl");
    }

    @Test
    @DisplayName("공지사항 생성 실패 - 이미지 없으면 삭제 로직 실행되지 않음")
    void createInformation_fail_exception_withoutImage_noDelete() {

        // given
        AuthUser authUser = new AuthUser(1L, "test@test.com", ADMIN, "홍길동");

        InformationCreateRequest request = new InformationCreateRequest("제목입니다", "내용입니다");

        MultipartFile image = mock(MultipartFile.class);

        User admin = mock(User.class);

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(admin));

        when(informationRepository.existsByTitle(request.getTitle())).thenReturn(false);

        when(image.isEmpty()).thenReturn(true);

        doThrow(new RuntimeException("DB 오류"))
                .when(informationRepository)
                .save(any(Information.class));

        // when & then
        assertThatThrownBy(() ->
                informationService.create(authUser, request, image))
                .isInstanceOf(CustomException.class)
                .hasMessage("공지사항 생성에 실패했습니다.");

        verify(s3Service, never()).deleteImage(any());
        verify(s3Service, never()).uploadImage(any());
    }

    @Test
    @DisplayName("공지사항 단건 조회 성공")
    void getOne_information_success() {

        // given
        Long informationId = 1L;
        User admin = mock(User.class);

        Information information = new Information(admin, "제목입니다", "내용입니다", "imageUrl");

        when(informationRepository.findById(informationId)).thenReturn(Optional.of(information));

        // when
        InformationGetResponse response = informationService.getOne(informationId);

        // then
        assertThat(response.getTitle()).isEqualTo("제목입니다");
        assertThat(response.getDescription()).isEqualTo("내용입니다");
        assertThat(response.getImageUrl()).isEqualTo("imageUrl");
    }

    @Test
    @DisplayName("공지사항 단건 조회 실패 - 게시물이 없음")
    void getOne_information_fail() {

        // given
        Long informationId = 1L;

        when(informationRepository.findById(informationId)).thenReturn(Optional.empty());


        // when & then
        assertThatThrownBy(() ->
                informationService.getOne(informationId))
                .isInstanceOf(CustomException.class)
                .hasMessage("공지사항을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("공지사항 전체 조회 성공")
    void getAll_information_success() {

        // given
        Pageable pageable = PageRequest.of(0,10);

        User admin = mock(User.class);

        Information information1 = new Information(admin, "제목입니다1", "내용입니다1", "imageUrl1");
        Information information2 = new Information(admin, "제목입니다2", "내용입니다2", "imageUrl2");

        Page<Information> page = new PageImpl<>(List.of(information1, information2), pageable, 2);

        when(informationRepository.findInformationPageSummary(pageable)).thenReturn(page);

        // when
        Page<InformationGetAllResponse> response = informationService.getAll(pageable);

        // then
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getTotalElements()).isEqualTo(2);

        assertThat(response.getContent().get(0).getTitle()).isEqualTo("제목입니다1");
        assertThat(response.getContent().get(1).getTitle()).isEqualTo("제목입니다2");
        assertThat(response.getContent().get(0).getDescription()).isEqualTo("내용입니다1");
        assertThat(response.getContent().get(1).getDescription()).isEqualTo("내용입니다2");
        assertThat(response.getContent().get(0).getImageUrl()).isEqualTo("imageUrl1");
        assertThat(response.getContent().get(1).getImageUrl()).isEqualTo("imageUrl2");

        verify(informationRepository).findInformationPageSummary(pageable);

    }
}