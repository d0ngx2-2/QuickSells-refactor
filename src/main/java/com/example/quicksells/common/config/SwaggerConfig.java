package com.example.quicksells.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Swagger 전역 설명 추가
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Quick Sell API")
                        .version("ver 1.0")
                        .description(
                                "<div style='color: black;'>" +
                                "<p>Quick Sell은 감정사 기반 중고 매입과 경매를 결합한 온라인 전당포 서비스로, " +
                                "중고 거래의 불확실성과 시간을 동시에 해결하는 플랫폼입니다.</p>" +

                                "<h5>공통 응답 형식</h5>" +
                                "<p>모든 API는 다음과 같은 공통 응답 형식을 사용합니다:</p>" +

                                "<pre style='background-color: #f5f5f5; padding: 15px; border-radius: 5px; color: black;'>" +
                                "{\n" +
                                "  \"success\": true,\n" +
                                "  \"message\": \"응답 메시지\",\n" +
                                "  \"data\": { ... },\n" +
                                "  \"timestamp\": \"2025-01-16 15:30:00\"\n" +
                                "}" +
                                "</pre>" +

                                "<ul style='color: black;'>" +
                                "<li><strong>success</strong>: 성공 여부 (true/false)</li>" +
                                "<li><strong>message</strong>: 응답 메시지</li>" +
                                "<li><strong>data</strong>: 실제 응답 데이터 (API마다 다름)</li>" +
                                "<li><strong>timestamp</strong>: 응답 시간</li>" +
                                "</ul>" +
                                "</div>"
                        )
                )
                // Swagger UI에서 JWT 토큰을 저장하고 자동으로 사용하는 기능 활성화
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        // JWT 토큰 입력창 제공
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
