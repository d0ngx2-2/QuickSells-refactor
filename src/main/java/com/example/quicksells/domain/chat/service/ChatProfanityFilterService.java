package com.example.quicksells.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 비속어 필터링 서비스
 */
@Slf4j
@Service
public class ChatProfanityFilterService {

    // 비속어 목록 (HashSet으로 빠른 검색)
    private final Set<String> profanityWords;

    // 정규표현식 패턴들
    private final Pattern[] profanityPatterns;

    public ChatProfanityFilterService() {
        this.profanityWords = new HashSet<>();
        initializeProfanityWords();
        this.profanityPatterns = compileProfanityPatterns();
    }

    /**
     * 비속어 목록 초기화
     */
    private void initializeProfanityWords() {
        // 한국어 비속어 (예시, 실제로는 더 많이 추가)
        profanityWords.add("시발");
        profanityWords.add("씨발");
        profanityWords.add("ㅅㅂ");
        profanityWords.add("시팔");
        profanityWords.add("개새");
        profanityWords.add("개새끼");
        profanityWords.add("새끼");
        profanityWords.add("ㅅㄲ");
        profanityWords.add("병신");
        profanityWords.add("ㅂㅅ");
        profanityWords.add("지랄");
        profanityWords.add("ㅈㄹ");
        profanityWords.add("닥쳐");
        profanityWords.add("꺼져");
        profanityWords.add("죽어");
        profanityWords.add("좆");
        profanityWords.add("ㅈ");
        profanityWords.add("애미");
        profanityWords.add("에미");
        profanityWords.add("엄마없");
        profanityWords.add("느금마");
        profanityWords.add("느금");
        profanityWords.add("갈보");
        profanityWords.add("창녀");

        // 영어 비속어
        profanityWords.add("fuck");
        profanityWords.add("shit");
        profanityWords.add("bitch");
        profanityWords.add("damn");
        profanityWords.add("ass");

    }

    /**
     * 비속어 정규표현식 패턴 생성
     */
    private Pattern[] compileProfanityPatterns() {
        return new Pattern[] {
                // 자음/모음 분리 우회 방지: ㅅ ㅣ ㅂ ㅏ ㄹ → 시발
                Pattern.compile("[ㅅ시][ㅣ이]*[ㅂ비][ㅏ아]*[ㄹ르ㄹ]*", Pattern.CASE_INSENSITIVE),

                // 공백/특수문자 우회 방지: 시.발, 시 발, 시-발
                Pattern.compile("시[\\s\\W]*발", Pattern.CASE_INSENSITIVE),

                // 반복 문자 우회 방지: 시이이발
                Pattern.compile("시+.*발+", Pattern.CASE_INSENSITIVE),
        };
    }

    /**
     * 메인 메서드: 메시지 필터링
     *
     * @param message 원본 메시지
     * @return 필터링된 메시지
     */
    public String filterProfanity(String message) {
        if (message == null || message.trim().isEmpty()) {
            return message;
        }

        String filtered = message;

        // 1. 완전 일치 필터링
        for (String word : profanityWords) {
            if (filtered.toLowerCase().contains(word.toLowerCase())) {
                filtered = filtered.replaceAll("(?i)" + Pattern.quote(word), getMask(word.length()));
                log.warn("비속어 감지 및 필터링: '{}'", word);
            }
        }

        // 2. 정규표현식 패턴 필터링
        for (Pattern pattern : profanityPatterns) {
            if (pattern.matcher(filtered).find()) {
                filtered = pattern.matcher(filtered).replaceAll("***");
                log.warn("비속어 패턴 감지 및 필터링: {}", pattern.pattern());
            }
        }

        return filtered;
    }


    /**
     * 마스킹 문자열 생성 (비속어 길이만큼 *)
     */
    private String getMask(int length) {
        return "*".repeat(Math.max(1, length));
    }

    /**
     * 비속어 추가 (관리자 기능)
     */
    public void addProfanityWord(String word) {
        if (word != null && !word.trim().isEmpty()) {
            profanityWords.add(word.trim());
        }
    }

    /**
     * 비속어 제거 (관리자 기능)
     */
    public void removeProfanityWord(String word) {
        profanityWords.remove(word);
    }

    /**
     * 현재 등록된 비속어 개수
     */
    public int getProfanityWordCount() {
        return profanityWords.size();
    }
}
