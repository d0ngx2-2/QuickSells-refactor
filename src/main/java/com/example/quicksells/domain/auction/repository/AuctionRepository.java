package com.example.quicksells.domain.auction.repository;

import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.auction.entity.Auction;
import com.example.quicksells.domain.deal.entity.Deal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    Page<Auction> findAll(Pageable pageable);

    boolean existsByAppraise(@Param("appraiseId") Appraise appraise);

    boolean existsByDeal(@Param("dealId") Deal deal);
}
