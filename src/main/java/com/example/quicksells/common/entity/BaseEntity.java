package com.example.quicksells.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass   //매핑정보만 상속받는 SuperClass
@EntityListeners(AuditingEntityListener.class)  // jpa 엔티티의 특정 이벤트가 발생했을 때, 이를 자동으로 감지하여 수행하도록 도와줌
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;   //생성일

    @LastModifiedDate
    private LocalDateTime updatedAt;   //수정일
}
