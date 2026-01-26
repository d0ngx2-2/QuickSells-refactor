package com.example.quicksells.domain.appraise.repository;

import com.example.quicksells.common.enums.AppraiseStatus;
import com.example.quicksells.domain.appraise.entity.Appraise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface AppraiseCustomRepository {

    Page<Appraise> findByItemIdWithPaging(@Param("itemId") Long itemId, Pageable pageable);

    Optional<Appraise> findByIdAndItemId(@Param("appraiseId") Long appraiseId, @Param("itemId") Long itemId);

    Page<Appraise> findByAppraiserIdWithItemAndSeller(@Param("appraiseId") Long appraiserId, @Param("status") AppraiseStatus status, Pageable pageable);

    Optional<Appraise> findByIdWithItemAndSeller(@Param("appraiseId") Long appraiseId);
}
