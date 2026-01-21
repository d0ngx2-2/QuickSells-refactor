package com.example.quicksells.domain.item.entity;

import com.example.quicksells.common.entity.BaseEntity;
import com.example.quicksells.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false") //false(삭제인 경우 조회 X)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "hope_price", nullable = false)
    private Long hopePrice;

    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "status", nullable = false)
    private boolean status = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public Item(User user, String name, Long hopePrice, String description, String image) {
        this.user = user;
        this.name = name;
        this.hopePrice = hopePrice;
        this.description = description;
        this.image = image;
    }

    public void update(String name, Long hopePrice, String description, String image, MultipartFile itemImage) {
        this.name = name;
        this.hopePrice = hopePrice;
        this.description = description;
        this.image = image;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    // 상품 판매 완료시 false > true 변경
    public void updateItemStatus(boolean status) {
        this.status = status;
    }
}
