package com.example.quicksells.domain.item.service;

import com.example.quicksells.common.aws.service.S3Service;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.model.request.ItemCreatedRequest;
import com.example.quicksells.domain.item.model.response.ItemCreatedResponse;
import com.example.quicksells.domain.item.model.response.ItemGetDetailResponse;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.example.quicksells.common.enums.UserRole.USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks //테스트 대상
    private ItemService itemService;

    @Mock //서비스가 의존하는 것들
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private MultipartFile multipartFile;

    private AuthUser authUser;

    @BeforeEach
// 공통데이터 초기화
    void setUp() {
        authUser = new AuthUser(1L, "test@test.com", USER, "name");
    }

    @Test
    @DisplayName("상품 생성 성공")
    void itemCreated() {

        //given
        AuthUser authUser = new AuthUser(1L, "test@test.com", USER, "name");
        ItemCreatedRequest request = new ItemCreatedRequest("상품1", 1L, "상품설명");
        User seller = mock(User.class);

        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(s3Service.uploadImage(image)).thenReturn("img-url");

        Item saved = new Item(seller, "상품1", 1L, "상품설명", "img_url");
        when(itemRepository.save(any(Item.class))).thenReturn(saved);

        //when
        ItemCreatedResponse response = itemService.itemCreated(authUser, request, image);

        //then
        assertThat(response).isNotNull();
        verify(s3Service).uploadImage(image);
        verify(s3Service, never()).deleteImage(anyString());
    }

    @Test
    @DisplayName("나의 등록 상품 상세 조회 성공")
    void itemMyDetailGettest() {
        //given
        Long id = 10L;
        AuthUser authUser = new AuthUser(1L, "test@test.com", USER, "name");

        Item item = mock(Item.class);
        User seller = mock(User.class);

        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(item.getSeller()).thenReturn(seller);
        when(seller.getId()).thenReturn(1L);

        //when
        ItemGetDetailResponse response = itemService.getMyDetail(id, authUser);

        //then
        assertThat(response).isNotNull();
    }
}
