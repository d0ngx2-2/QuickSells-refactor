package com.example.quicksells.domain.burkdata.service;

import com.example.quicksells.common.enums.AuctionStatusType;
import com.example.quicksells.common.enums.StatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import static com.example.quicksells.common.enums.AuctionStatusType.*;
import static com.example.quicksells.common.enums.StatusType.ON_SALE;
import static com.example.quicksells.common.enums.StatusType.SOLD;

@Slf4j
@Service
@RequiredArgsConstructor
public class BulkDataInsertService {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    private static final int BATCH_SIZE = 1000;
    private static final Random RANDOM = new Random();

    /**
     * 진행 상황을 로깅하며 대용량 사용자 데이터 삽입
     */
    @Transactional
    public void insertBulkUsers(int count) {

        log.info("{}명의 사용자 데이터 삽입 시작", count);

        long startTime = System.currentTimeMillis();

        String sql = """
            INSERT INTO users (email, password, name, phone, address, birth, role, 
                               is_deleted, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        // 배치 단위로 나눠서 처리
        int totalBatches = (int) Math.ceil((double) count / BATCH_SIZE);

        for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
            final int currentBatch = batchIndex;
            final int startIdx = batchIndex * BATCH_SIZE;
            final int endIdx = Math.min(startIdx + BATCH_SIZE, count);
            final int batchCount = endIdx - startIdx;

            long batchStartTime = System.currentTimeMillis();

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    int actualIndex = startIdx + i;
                    ps.setString(1, "user" + actualIndex + "@quicksell.com");
                    ps.setString(2, passwordEncoder.encode("password123!"));
                    ps.setString(3, "사용자" + actualIndex);
                    ps.setString(4, "010" + String.format("%08d", actualIndex % 100000000));
                    ps.setString(5, "서울시 " + getRandomDistrict() + " " + (actualIndex % 500 + 1) + "번지");
                    ps.setString(6, generateRandomBirth());
                    ps.setString(7, actualIndex % 10 == 0 ? "ADMIN" : "USER");
                    ps.setBoolean(8, false);
                    ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now().minusDays(actualIndex % 365)));
                    ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now().minusDays(actualIndex % 365)));
                }

                @Override
                public int getBatchSize() {
                    return batchCount;
                }
            });

            long batchEndTime = System.currentTimeMillis();
            long batchDuration = batchEndTime - batchStartTime;

            // 진행 상황 로깅
            int progress = (int) (((double) endIdx / count) * 100);
            log.info("[Users] 배치 {}/{} 완료 | 진행률: {}% | {}건 삽입 | 소요시간: {}ms",
                    currentBatch + 1, totalBatches, progress, batchCount, batchDuration);
        }

        long endTime = System.currentTimeMillis();
        long totalDuration = endTime - startTime;

        log.info("사용자 데이터 {} 건 삽입 완료 | 총 소요시간: {}초",
                count, totalDuration / 1000.0);
        log.info("평균 처리 속도: {}건/초", (count * 1000.0) / totalDuration);

    }

    /**
     * 진행 상황을 로깅하며 대용량 상품 데이터 삽입
     */
    @Transactional
    public void insertBulkItems(int count) {

        log.info("{}개의 상품 데이터 삽입 시작", count);


        long startTime = System.currentTimeMillis();

        String sql = """
            INSERT INTO items (seller_id, name, hope_price, description, image, 
                             status, is_deleted, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        List<Long> userIds = getUserIds();
        if (userIds.isEmpty()) {
            throw new IllegalStateException("사용자 데이터가 없습니다. 먼저 사용자를 생성해주세요.");
        }

        log.info("사용 가능한 사용자 수: {} 명", userIds.size());

        int totalBatches = (int) Math.ceil((double) count / BATCH_SIZE);

        for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
            final int currentBatch = batchIndex;
            final int startIdx = batchIndex * BATCH_SIZE;
            final int endIdx = Math.min(startIdx + BATCH_SIZE, count);
            final int batchCount = endIdx - startIdx;

            long batchStartTime = System.currentTimeMillis();

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    int actualIndex = startIdx + i;
                    ps.setLong(1, userIds.get(actualIndex % userIds.size()));
                    ps.setString(2, generateItemName(actualIndex));
                    ps.setLong(3, (long) ((Math.random() * 900000) + 100000));
                    ps.setString(4, generateItemDescription(actualIndex));
                    ps.setString(5, "item_image_" + actualIndex + ".jpg");
                    ps.setBoolean(6, Math.random() > 0.3);
                    ps.setBoolean(7, false);
                    ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now().minusDays(actualIndex % 90)));
                    ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now().minusDays(actualIndex % 90)));
                }

                @Override
                public int getBatchSize() {
                    return batchCount;
                }
            });

            long batchEndTime = System.currentTimeMillis();
            long batchDuration = batchEndTime - batchStartTime;

            int progress = (int) (((double) endIdx / count) * 100);
            log.info("[Items] 배치 {}/{} 완료 | 진행률: {}% | {}건 삽입 | 소요시간: {}ms",
                    currentBatch + 1, totalBatches, progress, batchCount, batchDuration);
        }

        long endTime = System.currentTimeMillis();
        long totalDuration = endTime - startTime;

        log.info("상품 데이터 {} 건 삽입 완료 | 총 소요시간: {}초",
                count, totalDuration / 1000.0);
        log.info("평균 처리 속도: {}건/초", (count * 1000.0) / totalDuration);

    }

    /**
     * 대용량 거래 데이터 삽입 (안전하게 수정)
     */
    @Transactional
    public void insertBulkDeals(int count) {

        log.info("{}개의 거래 데이터 삽입 시작", count);

        long startTime = System.currentTimeMillis();

        String sql = """
        INSERT INTO deals (buyer_id, seller_id, item_id, type, status, 
                         deal_price, created_at)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        List<Long> userIds = getUserIds();
        List<Long> itemIds = getItemIds();

        if (userIds.isEmpty() || itemIds.isEmpty()) {
            throw new IllegalStateException("사용자 또는 상품 데이터가 없습니다.");
        }

        log.info("사용 가능한 사용자 수: {} 명, 상품 수: {} 개", userIds.size(), itemIds.size());

        int totalBatches = (int) Math.ceil((double) count / BATCH_SIZE);

        for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
            final int currentBatch = batchIndex;
            final int startIdx = batchIndex * BATCH_SIZE;
            final int endIdx = Math.min(startIdx + BATCH_SIZE, count);
            final int batchCount = endIdx - startIdx;

            long batchStartTime = System.currentTimeMillis();

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    int actualIndex = startIdx + i;

                    // 안전한 인덱스 계산
                    int buyerIndex = actualIndex % userIds.size();
                    int sellerIndex = (actualIndex + 1) % userIds.size();
                    int itemIndex = actualIndex % itemIds.size();

                    ps.setLong(1, userIds.get(buyerIndex));        // buyer_id
                    ps.setLong(2, userIds.get(sellerIndex));       // seller_id
                    ps.setLong(3, itemIds.get(itemIndex));         // item_id

                    // 안전한 타입 선택
                    ps.setString(4, actualIndex % 3 == 0 ? "AUCTION" : "IMMEDIATE_SELL");

                    // 안전한 상태 생성
                    ps.setString(5, generateDealStatus().toString());

                    // deal_price는 항상 값 설정
                    ps.setInt(6, randomInt(100000, 1000000)); // 10만원 ~ 100만원

                    // 안전한 날짜 생성
                    ps.setTimestamp(7, Timestamp.valueOf(
                            LocalDateTime.now().minusDays(actualIndex % 60)
                    ));
                }

                @Override
                public int getBatchSize() {
                    return batchCount;
                }
            });

            long batchEndTime = System.currentTimeMillis();
            long batchDuration = batchEndTime - batchStartTime;

            int progress = (int) (((double) endIdx / count) * 100);
            log.info("[Deals] 배치 {}/{} 완료 | 진행률: {}% | {}건 삽입 | 소요시간: {}ms",
                    currentBatch + 1, totalBatches, progress, batchCount, batchDuration);
        }

        long endTime = System.currentTimeMillis();
        long totalDuration = endTime - startTime;

        log.info("거래 데이터 {} 건 삽입 완료 | 총 소요시간: {}초",
                count, totalDuration / 1000.0);
        log.info("평균 처리 속도: {}건/초", (count * 1000.0) / totalDuration);

    }

    /**
     * 진행 상황을 로깅하며 대용량 감정 데이터 삽입
     */
    @Transactional
    public void insertBulkAppraises(int count) {

        log.info("{}개의 감정 데이터 삽입 시작", count);

        long startTime = System.currentTimeMillis();

        String sql = """
            INSERT INTO appraises (admin_id, item_id, deal_id, bid_price, 
                                  is_seleted, is_deleted, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        List<Long> adminIds = getAdminIds();
        List<Long> itemIds = getItemIds();
        List<Long> dealIds = getDealIds();

        if (adminIds.isEmpty()) {
            throw new IllegalStateException("관리자(감정사) 데이터가 없습니다.");
        }
        if (itemIds.isEmpty()) {
            throw new IllegalStateException("상품 데이터가 없습니다.");
        }

        log.info("사용 가능한 관리자 수: {} 명, 상품 수: {} 개, 거래 수: {} 개",
                adminIds.size(), itemIds.size(), dealIds.size());

        int totalBatches = (int) Math.ceil((double) count / BATCH_SIZE);

        for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
            final int currentBatch = batchIndex;
            final int startIdx = batchIndex * BATCH_SIZE;
            final int endIdx = Math.min(startIdx + BATCH_SIZE, count);
            final int batchCount = endIdx - startIdx;

            long batchStartTime = System.currentTimeMillis();

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    int actualIndex = startIdx + i;
                    ps.setLong(1, adminIds.get(actualIndex % adminIds.size()));
                    ps.setLong(2, itemIds.get(actualIndex % itemIds.size()));
                    ps.setLong(3, dealIds.get(actualIndex % dealIds.size()));
                    ps.setInt(4, (int) ((Math.random() * 800000) + 200000));
                    ps.setBoolean(5, Math.random() > 0.8);
                    ps.setBoolean(6, false);
                    ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now().minusDays(actualIndex % 45)));
                }

                @Override
                public int getBatchSize() {
                    return batchCount;
                }
            });

            long batchEndTime = System.currentTimeMillis();
            long batchDuration = batchEndTime - batchStartTime;

            int progress = (int) (((double) endIdx / count) * 100);
            log.info("[Appraises] 배치 {}/{} 완료 | 진행률: {}% | {}건 삽입 | 소요시간: {}ms",
                    currentBatch + 1, totalBatches, progress, batchCount, batchDuration);
        }

        long endTime = System.currentTimeMillis();
        long totalDuration = endTime - startTime;

        log.info("감정 데이터 {} 건 삽입 완료 | 총 소요시간: {}초",
                count, totalDuration / 1000.0);
        log.info("평균 처리 속도: {}건/초", (count * 1000.0) / totalDuration);

    }

    /**
     * 진행 상황을 로깅하며 대용량 경매 데이터 삽입
     */
    @Transactional
    public void insertBulkAuctions(int count) {

        log.info("{}개의 경매 데이터 삽입 시작", count);

        long startTime = System.currentTimeMillis();

        String sql = """
            INSERT INTO auctions (appraise_id, deal_id, buyer_id, bid_price, status, 
                                end_time, is_deleted, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        List<Long> appraiseIds = getAppraiseIds();
        List<Long> dealIds = getDealIds();
        List<Long> userIds = getUserIds();

        if (appraiseIds.isEmpty() || dealIds.isEmpty() || userIds.isEmpty()) {
            throw new IllegalStateException("감정, 거래 또는 사용자 데이터가 없습니다.");
        }

        log.info("사용 가능한 감정 수: {} 개, 거래 수: {} 개, 사용자 수: {} 명",
                appraiseIds.size(), dealIds.size(), userIds.size());

        int totalBatches = (int) Math.ceil((double) count / BATCH_SIZE);

        for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
            final int currentBatch = batchIndex;
            final int startIdx = batchIndex * BATCH_SIZE;
            final int endIdx = Math.min(startIdx + BATCH_SIZE, count);
            final int batchCount = endIdx - startIdx;

            long batchStartTime = System.currentTimeMillis();

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    int actualIndex = startIdx + i;
                    ps.setLong(1, appraiseIds.get(actualIndex % appraiseIds.size()));
                    ps.setLong(2, dealIds.get(actualIndex % dealIds.size()));
                    ps.setLong(3, userIds.get(actualIndex % userIds.size()));
                    ps.setInt(4, (int) ((Math.random() * 500000) + 100000));
                    ps.setString(5, generateAuctionStatus().toString());
                    ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now().plusDays(actualIndex % 14)));
                    ps.setBoolean(7, false);
                    ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now().minusDays(actualIndex % 30)));
                    ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now().minusDays(actualIndex % 30)));
                }

                @Override
                public int getBatchSize() {
                    return batchCount;
                }
            });

            long batchEndTime = System.currentTimeMillis();
            long batchDuration = batchEndTime - batchStartTime;

            int progress = (int) (((double) endIdx / count) * 100);
            log.info("[Auctions] 배치 {}/{} 완료 | 진행률: {}% | {}건 삽입 | 소요시간: {}ms",
                    currentBatch + 1, totalBatches, progress, batchCount, batchDuration);
        }

        long endTime = System.currentTimeMillis();
        long totalDuration = endTime - startTime;

        log.info("경매 데이터 {} 건 삽입 완료 | 총 소요시간: {}초",
                count, totalDuration / 1000.0);
        log.info("평균 처리 속도: {}건/초", (count * 1000.0) / totalDuration);

    }

    // ==================== 헬퍼 메소드 ====================

    private List<Long> getUserIds() {
        long startTime = System.currentTimeMillis();
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM users WHERE is_deleted = false ORDER BY id LIMIT 50000",
                Long.class
        );
        long duration = System.currentTimeMillis() - startTime;
        log.debug("사용자 ID 조회 완료: {} 건, 소요시간: {}ms", ids.size(), duration);
        return ids;
    }

    private List<Long> getAdminIds() {
        long startTime = System.currentTimeMillis();
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM users WHERE role = 'ADMIN' AND is_deleted = false ORDER BY id LIMIT 10000",
                Long.class
        );
        long duration = System.currentTimeMillis() - startTime;
        log.debug("관리자 ID 조회 완료: {} 건, 소요시간: {}ms", ids.size(), duration);
        return ids;
    }

    private List<Long> getItemIds() {
        long startTime = System.currentTimeMillis();
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM items WHERE is_deleted = false ORDER BY id LIMIT 50000",
                Long.class
        );
        long duration = System.currentTimeMillis() - startTime;
        log.debug("상품 ID 조회 완료: {} 건, 소요시간: {}ms", ids.size(), duration);
        return ids;
    }

    private List<Long> getDealIds() {
        long startTime = System.currentTimeMillis();
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM deals ORDER BY id LIMIT 50000",
                Long.class
        );
        long duration = System.currentTimeMillis() - startTime;
        log.debug("거래 ID 조회 완료: {} 건, 소요시간: {}ms", ids.size(), duration);
        return ids;
    }

    private List<Long> getAppraiseIds() {
        long startTime = System.currentTimeMillis();
        List<Long> ids = jdbcTemplate.queryForList(
                "SELECT id FROM appraises WHERE is_deleted = false ORDER BY id LIMIT 50000",
                Long.class
        );
        long duration = System.currentTimeMillis() - startTime;
        log.debug("감정 ID 조회 완료: {} 건, 소요시간: {}ms", ids.size(), duration);
        return ids;
    }


    private <T> T getRandomElement(T[] array) {
        return array[RANDOM.nextInt(array.length)];
    }

    private String generateRandomBirth() {
        int year = 1970 + RANDOM.nextInt(40); // 1970~2009
        int month = 1 + RANDOM.nextInt(12);    // 1~12
        int day = 1 + RANDOM.nextInt(28);      // 1~28
        return String.format("%d-%02d-%02d", year, month, day);
    }

    private String getRandomDistrict() {
        String[] districts = {
                "강남구", "서초구", "송파구", "강동구", "마포구",
                "용산구", "성동구", "광진구", "종로구", "중구"
        };
        return getRandomElement(districts);
    }

    private String generateItemName(int i) {
        String[] categories = {
                "맥북 프로", "아이폰", "갤럭시", "에어팟", "아이패드",
                "다이슨 청소기", "LG 그램", "삼성 갤럭시 워치", "애플 워치", "소니 카메라",
                "명품 가방", "롤렉스 시계", "나이키 신발", "아디다스 운동화", "구찌 벨트"
        };
        String category = categories[i % categories.length];
        int modelNumber = (i / categories.length) + 1;
        return category + " " + modelNumber + "세대";
    }

    private String generateItemDescription(int i) {
        String[] conditions = {
                "새상품 미개봉", "거의 새것", "사용감 적음",
                "중고 양호", "일부 사용감 있음"
        };
        String condition = conditions[i % conditions.length];
        return condition + " 상태입니다. 직거래 환영합니다. 상세 사진 추가 가능합니다.";
    }

    private StatusType generateDealStatus() {
        StatusType[] statuses = {ON_SALE, SOLD};
        return getRandomElement(statuses); // 30%
    }

    private AuctionStatusType generateAuctionStatus() {
        AuctionStatusType[] statuses = { AUCTIONING, SUCCESSFUL_BID, UNSUCCESSFUL_BID, CANCELED};
        return getRandomElement(statuses);
    }

    private int randomInt(int min, int max) {
        return min + RANDOM.nextInt(max - min + 1);
    }
}
