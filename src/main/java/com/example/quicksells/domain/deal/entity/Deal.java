package com.example.quicksells.domain.deal.entity;

import com.example.quicksells.common.enums.DealType;
import com.example.quicksells.common.enums.StatusType;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DealType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
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

    public Deal(Item item, User user, DealType type, StatusType status, Integer dealPrice) {
        this.item = item;
        this.user = user;
        this.type = type;
        this.status = status;
        this.dealPrice = dealPrice;
    }
}
