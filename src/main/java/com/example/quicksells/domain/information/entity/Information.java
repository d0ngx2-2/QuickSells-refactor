package com.example.quicksells.domain.information.entity;

import com.example.quicksells.common.entity.BaseEntity;
import com.example.quicksells.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "informations")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Information extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id")
    private User user;

    @Column(nullable = false, unique = true, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false, length = 10)
    private boolean isDeleted;

    public Information(User user, String title, String description, String imageUrl) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isDeleted = false;
    }

    public void update(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public void delete() {this.isDeleted = true;}

    public void updateTitle(String title) {this.title = title;}

    public void updateDescription(String description) {this.description = description;}

    public void updateImage(String imageUrl) {this.imageUrl = imageUrl;}

    public void removeImage() {this.imageUrl = null;}
}
