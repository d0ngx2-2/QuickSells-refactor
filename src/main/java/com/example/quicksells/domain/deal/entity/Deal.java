package com.example.quicksells.domain.deal.entity;

import com.example.quicksells.common.enums.DealType;
import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.StatusType;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "deals")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id")
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DealType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusType status;

    @Column(nullable = false)
    private Integer dealPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 생성 시간
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Deal(User buyer, User seller, Item item, DealType type, StatusType status, Integer dealPrice) {
        this.buyer = buyer;
        this.seller = seller;
        this.item = item;
        this.type = type;
        this.status = status;
        this.dealPrice = dealPrice;
    }


    // 비즈니스 메서드

    /**
     * 감정 선택 시 거래 시작
     * - buyer 설정
     * - type 설정 (IMMEDIATE_SELL)
     * - status: BEFORE -> ON_SALE
     */
    public void startDeal(User buyer, DealType dealType) {
        if (this.status != StatusType.BEFORE) {
            throw new CustomException(ExceptionCode.NOT_DEAL_BEFORE);
        }
        this.buyer = buyer;
        this.type = dealType;
        this.status = StatusType.ON_SALE;
    }

    /**
     * 거래 완료 처리
     * - status: ON_SALE -> SOLD
     * - 추후 결제 API 붙일때, 활용 하면 좋습니다.
     */
    public void complete() {
        if (this.status != StatusType.SOLD) {
            throw new CustomException(ExceptionCode.NOT_DEAL_ON_SALE);
        }
        this.status = StatusType.SOLD;
    }

    // 경매로 전환
    public void convertToAuction() {
        this.type = DealType.AUCTION;
    }

    // 즉시판매로 전환
    public void convertToImmdiateSell() {
        this.type = DealType.IMMEDIATE_SELL;
    }
}
