package com.example.quicksells.domain.information.repository;

import com.example.quicksells.domain.information.entity.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InformationCustomRepository {

    Page<Information> findInformationPageSummary(Pageable pageable);
}
