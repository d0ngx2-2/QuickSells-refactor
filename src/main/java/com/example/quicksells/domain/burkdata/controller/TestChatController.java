package com.example.quicksells.domain.burkdata.controller;

import com.example.quicksells.common.security.JwtAuthenticationToken;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * WebSocket 테스트용 Controller
 *
 * [중요] WebSocket에서는 @Payload와 @AuthenticationPrincipal을 함께 사용하면 Jackson 역직렬화 문제가 발생
 * Principal을 직접 사용하고 AuthUser는 메서드 내에서 추출
 */
@Slf4j
@Controller
public class TestChatController {

    /**
     *  @AuthenticationPrincipal 사용
     */
    @MessageMapping("/chat.test")
    @SendTo("/topic/test")
    public TestMessage testMessage(@Payload TestMessage message, Principal principal) {

        // Principal에서 AuthUser 추출
        AuthUser authUser = extractAuthUser(principal);

        System.out.println("=========================================");
        System.out.println("받은 메시지: " + message.getContent());
        System.out.println("발신자 ID: " + authUser.getId());
        System.out.println("발신자 Email: " + authUser.getEmail());
        System.out.println("발신자 이름: " + authUser.getName());
        System.out.println("발신자 권한: " + authUser.getRole());
        System.out.println("=========================================");

        // 응답에 발신자 정보 포함
        return new TestMessage(message.getContent(), authUser.getId(), authUser.getName());
    }

    /**
     * Principal에서 AuthUser 추출
     *
     * @param principal SecurityContext에 저장된 인증 정보
     * @return AuthUser 객체
     * @throws IllegalStateException 인증 정보가 없거나 타입이 맞지 않을 때
     */
    private AuthUser extractAuthUser(Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("인증 정보가 없습니다");
        }

        if (!(principal instanceof JwtAuthenticationToken)) {
            throw new IllegalStateException("잘못된 인증 타입입니다: " + principal.getClass());
        }

        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        Object principalObj = token.getPrincipal();

        if (!(principalObj instanceof AuthUser)) {
            throw new IllegalStateException("Principal이 AuthUser 타입이 아닙니다: " + principalObj.getClass());
        }

        return (AuthUser) principalObj;
    }

    /**
     * 테스트용 메시지 DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestMessage {
        private String content;
        private Long senderId;
        private String senderName;

        public TestMessage(String content) {
            this.content = content;
        }
    }
}
