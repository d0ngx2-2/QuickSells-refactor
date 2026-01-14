package com.example.quicksells.domain.item.service;

import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.appraise.repository.AppraiseRepository;
import com.example.quicksells.domain.item.dto.dto.ItemDto;
import com.example.quicksells.domain.item.dto.request.ItemCreatedRequest;
import com.example.quicksells.domain.item.dto.response.ItemCreatedResponse;
import com.example.quicksells.domain.item.entity.Item;
import com.example.quicksells.domain.item.repository.ItemRepository;
import com.example.quicksells.domain.user.entity.User;
import com.example.quicksells.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final AppraiseRepository appraiseRepository;

    @Transactional
    public ItemCreatedResponse itemCreated(Long userId, ItemCreatedRequest request) {

        //유저 조회
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 조회할 수 없습니다."));

        // 엔티티 생성
        Item item = new Item(seller, request.getName(), request.getHopePrice(), request.getDescription(), request.getImage());

        // Item 엔티티 DB에 저장
        Item saved = itemRepository.save(item);

        // 저장된 엔티티 -> ItemDto로 변환
        ItemDto itemDto = ItemDto.from(saved);

        //응답 전용 객체
        return ItemCreatedResponse.from(itemDto);
    }

}
