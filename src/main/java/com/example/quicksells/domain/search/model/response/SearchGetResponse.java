package com.example.quicksells.domain.search.model.response;

import com.example.quicksells.domain.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchGetResponse {
    private Long id;
    private String name;

    public SearchGetResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static SearchGetResponse from(Item item){
        return new SearchGetResponse(
                item.getId(),
                item.getName()
        );
    }
}