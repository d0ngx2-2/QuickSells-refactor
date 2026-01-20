package com.example.quicksells.domain.user.entity;

import com.example.quicksells.common.entity.BaseEntity;
import com.example.quicksells.common.enums.UserRole;
import com.example.quicksells.domain.user.model.request.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50 ,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 25)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 25)
    private String birth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserRole role;

    @Column(nullable = false)
    private boolean isDeleted;

    public User(String email, String password, String name, String phone, String address, String birth){
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.birth = birth;
        this.role = UserRole.USER;
        this.isDeleted = false;
    }
    public void updatePassword(String encodedPassword) {this.password = encodedPassword;}

    public void updatePhone(String phone) {this.phone = phone;}

    public void updateAddress(String address) {this.address = address;}

    public void updateRole(String role) {this.role = UserRole.of(role);}

    public void delete() {this.isDeleted = true;}
}
