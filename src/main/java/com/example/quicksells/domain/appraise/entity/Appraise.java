package com.example.quicksells.domain.appraise.entity;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.deal.entity.Deal;
import org.hibernate.annotations.SQLRestriction;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Table(name = "appraises")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Appraise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 감정 고유 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id")
    private User admin; // 감정사 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id")
    private Item item; // 상품 ID

    @Column(nullable = false)
    private Integer bidPrice; // 감정 가격

    @Column(nullable = false, length = 10)
    private boolean isSeleted; // 구매자 선택여부 (false > 경매 돌임, true > 즉시 매임)

    @Column(nullable = false, length = 10)
    private boolean isDeleted; // 삭제 여부

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Appraise(User admin, Item item, Integer bidPrice, boolean isSeleted) {
        this.admin = admin;
        this.item = item;
        this.bidPrice = bidPrice;
        this.isSeleted = isSeleted;
        this.isDeleted = false;
    }

    // 여러 감정중 판매자가 선택할때,
    public void updateSelected(boolean isSelected) {
        if (this.isSeleted) {
            throw new CustomException(ExceptionCode.ALREADY_SELECT_APPRAISE);
        }
        this.isSeleted = isSelected;
    }

    // 감정 삭제 처리
    public void delete() {
        this.isDeleted = true;
    }
}
