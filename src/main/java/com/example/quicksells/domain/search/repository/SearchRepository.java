package com.example.quicksells.domain.search.repository;

import com.example.quicksells.domain.search.entity.Search;
import com.example.quicksells.domain.search.model.response.SearchGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long> {}
