package com.example.quicksells.domain.auction.entity;

import com.example.quicksells.common.enums.AuctionStatusType;
import com.example.quicksells.common.enums.StatusType;
import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.deal.entity.Deal;
import com.example.quicksells.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "auctions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 경매 ID

    @OneToOne(optional = false, cascade = CascadeType.PERSIST) // 거래x -> 경매 등록x
    @JoinColumn(name = "deal_id", nullable = false)
    private Deal deal; // 거래 ID

    @OneToOne(optional = false) // 감정x -> 경매 등록x
    @JoinColumn(name = "appraise_id", nullable = false)
    private Appraise appraise; // 감정 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User user; // 유저 ID

    @Column(name = "bid_price", nullable = false)
    private Integer bidPrice; // 입찰 확정 가격

    @Column(name = "status", nullable = false)
    private String status; // 경매 상태

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Auction(Deal deal, Appraise appraise, Integer bidPrice) {
        this.deal = deal;
        this.appraise = appraise;
        this.user = null;
        this.bidPrice = bidPrice;
        this.status = StatusType.ON_SALE.toString();
        this.isDeleted = false;
    }

    @PrePersist
    public void auctionCloseTime() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.endTime = createdAt.plusDays(7); // 경매 생성일 기준으로 일주일 뒤 종료
    }

    @PreUpdate
    public void auctionUpdateTime() {
        this.updatedAt = LocalDateTime.now(); // 수정일 적용 후 DB저장
    }

    public void update (User user, Integer bidPrice) {
        this.user = user;
        this.bidPrice = bidPrice;
    }

    public void auctionEndTimeCheck() {

        LocalDateTime nowDate = LocalDateTime.now(); // 현재 시간

        if (nowDate.isBefore(this.endTime)) {
            return; // 종료 시간 전일때 통과
        }

        this.isDeleted = true; // 종료 시간에 경매 삭제

        if (this.user == null) {
            this.status = AuctionStatusType.UNSUCCESSFUL_BID.toString(); // 유찰완료 상태 변경
        } else {
            this.status = AuctionStatusType.SUCCESSFUL_BID.toString();// 낙찰완료 상태 변경
        }
    }

    public void auctionDelete() {
        this.status = AuctionStatusType.CANCELED.toString(); // 경매취소 상태 변경
        this.isDeleted = true;
    }

}
