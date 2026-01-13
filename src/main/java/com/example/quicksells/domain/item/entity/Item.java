package com.example.quicksells.domain.item.entity;

import com.example.quicksells.common.entity.BaseEntity;
import com.example.quicksells.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false") //false(삭제인 경우 조회 X)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column (name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id")
    private User user;

    @Column (name = "name", nullable = false, length = 255)
    private String name;

    @Column (name = "hope_price", nullable = false)
    private String hopePrice;

    @Column (name = "description", nullable = false)
    private String description;

    @Column (name = "image", nullable = false, length = 255)
    private String image;

    @Column (name = "status", nullable = false, length = 10)
    private String status;

    @Column (name = "is_deleted", nullable = false, length = 10)
    private boolean isDeleted = false;

    public Item(User user, String name, String hopePrice, String description, String image, String status){
        this.user = user;
        this.name = name;
        this.hopePrice = hopePrice;
        this.description = description;
        this.image = image;
        this.status = status;
    }
}
