package com.example.quicksells.domain.auction.repository;

import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.auction.entity.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long>, AuctionCustomRepository {

    Page<Auction> findAll(Pageable pageable);

    boolean existsByAppraise(Appraise appraise);
}
