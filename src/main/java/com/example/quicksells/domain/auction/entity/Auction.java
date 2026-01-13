package com.example.quicksells.domain.auction.entity;

import com.example.quicksells.common.entity.BaseEntity;
import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "aucitons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Auction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 경매 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appraise_id", nullable = false)
    private Appraise appraise; // 감정 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User user; // 유저 ID

    @Column(name = "bid_price", nullable = false)
    private Long bidPrice; // 입찰 확정 가격

    @Column(name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    public Auction(Appraise appraise, User user, Long bidPrice) {
        this.appraise = appraise;
        this.user = user;
        this.bidPrice = bidPrice;
        this.isDeleted = false;
    }

}
