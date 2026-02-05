package com.example.quicksells.domain.wishlist.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import com.example.quicksells.domain.wishlist.entity.WishList;
import com.example.quicksells.domain.wishlist.model.request.OneWishListDeleteRequest;
import com.example.quicksells.domain.wishlist.model.request.WishListCreateRequest;
import com.example.quicksells.domain.wishlist.model.response.MyWishListGetAllResponse;
import com.example.quicksells.domain.wishlist.model.response.WishListCreateResponse;
import com.example.quicksells.domain.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

        // 구매자 조회
        User foundBuyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        // 상품 조회
        Item foundItem = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

        // 판매자 검증
        validateSeller(foundBuyer, foundItem);

        // 중복 검증
        deduplicationWishList(foundBuyer, foundItem);

        // 관심 목록 생성
        WishList newWishList = new WishList(foundBuyer, foundItem);

        // 관심 목록 저장
        WishList saveWishList = wishListRepository.save(newWishList);

        return WishListCreateResponse.from(saveWishList);
    }

    @Transactional(readOnly = true)
    public Slice<MyWishListGetAllResponse> getAllMyWishList(AuthUser authUser, Long buyerId, Pageable pageable) {

        // 구매자 검증
        validateUser(authUser, buyerId);

        // 구매자의 관심 목록 조회 (생성일 기준 내림차순)
        Slice<WishList> myWishListSlice = wishListRepository.myWishListSearch(buyerId, pageable);

        return myWishListSlice.map(MyWishListGetAllResponse::from);
    }

    @Transactional
    public void deleteMyWishList(AuthUser authUser, OneWishListDeleteRequest request, Pageable pageable) {

        // 구매자 검증
        validateUser(authUser, request.getBuyerId());

        // 구매자의 관심 목록 조회
        Slice<WishList> myWishListSlice = wishListRepository.myWishListSearch(request.getBuyerId(), pageable);

        // 내 관심 목록 인덱스 번호
        int myWishListIndex = request.getIndex() - 1;

        // 내 관심 목록의 인덱스 번호 검증
        validateWishListIndex(myWishListSlice, myWishListIndex);

        // 삭제할 관심 목록 인덱스로 가져오기
        WishList oneWishList = myWishListSlice.getContent().get(myWishListIndex);

        // 가져온 관심목록 물리 삭제
        wishListRepository.delete(oneWishList);
    }


    /**
     * 검증 메서드
     */

    private void deduplicationWishList(User foundUser, Item foundItem) {

        boolean duplicatedUserAndItem = wishListRepository.existsByBuyerAndItem(foundUser, foundItem);

        if (duplicatedUserAndItem) {
            throw new CustomException(ExceptionCode.CONFLICT_WISHLIST); // 중복된 관심 목록 에외
        }
    }

    private void validateSeller(User foundUser, Item foundItem) {

        Long userId = foundUser.getId();
        Long sellerId = foundItem.getSeller().getId();

        // 유저가 상품 판매자일때 예외
        if (userId.equals(sellerId)) {
            throw new CustomException(ExceptionCode.SELF_WISH_NOT_ALLOWED);
        }
    }


    private void validateUser(AuthUser authUser, Long buyerId) {

        Long authUserId = authUser.getId(); // 인증유저 아이디

        // 인증유저와 구매자가 일치하지 않을때 예외
        if (!authUserId.equals(buyerId)) {
            throw new CustomException(ExceptionCode.ACCESS_DENIED_EXCEPTION_WISHLIST);
        }
    }

    private void validateWishListIndex(Slice<WishList> myWishList, int index) {

        // 내 관심목록 개수 <= 요청한 관심목록 인덱스 - 1
        if (myWishList.getContent().size() <= index) {
            throw new CustomException(ExceptionCode.NOT_EXIST_ONE_WISHLIST);
        }
    }

}
