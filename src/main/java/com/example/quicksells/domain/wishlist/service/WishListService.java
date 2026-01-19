package com.example.quicksells.domain.wishlist.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import com.example.quicksells.domain.wishlist.entity.WishList;
import com.example.quicksells.domain.wishlist.model.request.WishListCreateRequest;
import com.example.quicksells.domain.wishlist.model.response.WishListCreateResponse;
import com.example.quicksells.domain.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public WishListCreateResponse saveWishList(WishListCreateRequest request) {

        // 유저 조회
        User foundUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        // 상품 조회
        Item foundItem = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

        // 중복 검증
        deduplicationWishList(foundUser, foundItem);

        // 관심 목록 생성
        WishList newWishList = new WishList(foundUser, foundItem);

        // 관심 목록 저장
        WishList saveWishList = wishListRepository.save(newWishList);

        return WishListCreateResponse.from(saveWishList);
    }


    /**
     * 검증 메서드
     */

    private void deduplicationWishList(User foundUser, Item foundItem) {

        boolean duplicatedUserAndItem = wishListRepository.existsByUserAndItem(foundUser, foundItem);

        if (duplicatedUserAndItem) {
            throw new CustomException(ExceptionCode.CONFLICT_WISHLIST); // 중복된 관심 목록 에외
        }
    }

}
