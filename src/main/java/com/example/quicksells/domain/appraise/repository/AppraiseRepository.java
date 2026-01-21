package com.example.quicksells.domain.appraise.repository;

import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AppraiseRepository extends JpaRepository<Appraise, Long> {

    // 특정 상품에 대한 모든 감정 조회 (페이징, 삭제되지 않은 것만)
    @Query("SELECT a FROM Appraise a WHERE a.item.id = :itemId ORDER BY a.createdAt DESC")
    Page<Appraise> findByItemIdWithPaging(@Param("itemId") Long itemId, Pageable pageable);

    // 특정 상품에 이미 선택된 감정이 있는지 확인
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appraise a WHERE a.item.id = :itemId AND a.isSelected = true")
    boolean existsByItemIdAndIsSelectedTrue(@Param("itemId") Long itemId);

    // 특정 상품에 대해 특정 감정사가 이미 감정을 등록했는지 확인
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appraise a WHERE a.item.id = :itemId AND a.admin.id = :adminId")
    boolean existsByItemIdAndUserId(@Param("itemId") Long itemId, @Param("adminId") Long adminId);

    // 특정 상품에 대한 특정 감정 조회 (삭제되지 않은 것만)
    @Query("SELECT a FROM Appraise a WHERE a.id = :appraiseId AND a.item.id = :itemId")
    Optional<Appraise> findByIdAndItemId(@Param("appraiseId") Long appraiseId, @Param("itemId") Long itemId);

    // 감정사가 작성한 감정 목록 (삭제되지 않은 것만)
    @Query("SELECT a FROM Appraise a WHERE a.admin.id = :adminId")
    List<Appraise> findByAdminId(@Param("adminId") Long adminId);

    // 특정 상품에 대한 특정 감정사의 감정 조회
    // @SQLRestriction으로 isDeleted = false 자동 필터링
    @Query("SELECT a FROM Appraise a WHERE a.item = :item AND a.admin.id = :userId")
    Optional<Appraise> findByItemAndUserId(@Param("item") Item item, @Param("userId") Long userId);
}
