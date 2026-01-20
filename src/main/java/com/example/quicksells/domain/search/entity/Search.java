package com.example.quicksells.domain.search.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "searches")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "search_id", nullable = false)
    private Long id;

    @Column(name = "keyword", length = 50, nullable = false)
    private String keyword;

    @Column(name = "count", nullable = false)
    private Long count;

    public Search(String keyword) {
        this.keyword = keyword;
        this.count = 0L; //0부터 카운트 시작
    }

    //검색어 증가 (+1)
    public void increase() {
        this.count++;
    }
}
