package com.example.quicksells.domain.appraise.model.response;

import com.example.quicksells.domain.appraise.entity.Appraise;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppraiseAdminUpdateResponse {

    private Long appraiseId;
    private Integer bidPrice;

    public static AppraiseAdminUpdateResponse from(Appraise appraise) {
        return new AppraiseAdminUpdateResponse(
                appraise.getId(),
                appraise.getBidPrice()
        );
    }
}
