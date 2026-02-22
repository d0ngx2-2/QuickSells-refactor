package com.example.quicksells.domain.auction.repository;

import com.example.quicksells.domain.auction.entity.Auction;
import com.example.quicksells.domain.auction.model.request.AuctionSearchFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuctionCustomRepository {
    Page<Auction> auctionSearch(Pageable pageable, AuctionSearchFilterRequest request);
}
