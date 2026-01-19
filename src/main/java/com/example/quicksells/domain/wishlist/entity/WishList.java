package com.example.quicksells.domain.wishlist.entity;

import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import java.time.Clock;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "wish_list", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "item_id"})) // 중복 저장 x
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public WishList(User user, Item item) {
        this.user = user;
        this.item = item;
        this.isDeleted = false;
    }

    @PrePersist
    public void CreatedAt() {
        Clock clock = Clock.systemDefaultZone();
        this.createdAt = LocalDateTime.now(clock);
    }
}
