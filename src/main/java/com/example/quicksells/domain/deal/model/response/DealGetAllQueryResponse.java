package com.example.quicksells.domain.deal.model.response;

import com.example.quicksells.common.enums.DealType;
import com.example.quicksells.common.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DealGetAllQueryResponse {

    private Long dealId;
    private DealType type;
    private Integer dealPrice;
    private StatusType status;
    private LocalDateTime createdAt;

    // item
    private Long itemId;
    private String itemTitle;

    // seller
    private Long sellerId;
    private String sellerName;

    // buyer (nullable)
    private Long buyerId;
    private String buyerName;
}
