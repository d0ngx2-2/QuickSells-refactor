package com.example.quicksells.domain.appraise.model.response;

import com.example.quicksells.common.enums.AppraiseStatus;
import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AppraiseAdminGetAllResponse {

    // 판매자 정보
    private final Long sellerId;
    private final String sellerName;

    // 상품 정보
    private final Long itemId;
    private final String itemName;

    // 감정 정보
    private final Long appraiseId;
    private final Integer bidPrice;
    private final AppraiseStatus status;
    private final Boolean isSelected;
    private final LocalDateTime createdAt;


    public static AppraiseAdminGetAllResponse from(Appraise appraise) {
        Item item = appraise.getItem();
        User seller = item.getSeller();

        return new AppraiseAdminGetAllResponse(
                seller.getId(),
                seller.getName(),
                item.getId(),
                item.getName(),
                appraise.getId(),
                appraise.getBidPrice(),
                appraise.getAppraiseStatus(),
                appraise.isSelected(),
                appraise.getCreatedAt()
        );

    }
}
