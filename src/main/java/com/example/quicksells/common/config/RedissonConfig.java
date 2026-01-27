package com.example.quicksells.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.client.codec.StringCodec;

@Configuration
public class RedissonConfig {

    // 래디슨 클라이언트
    @Bean
    public RedissonClient redissonClient() {

        Config config = new Config();
        config.setCodec(new StringCodec()); // 문자열로 변환 (default = Kryo5Codec)
        config.setNettyThreads(Runtime.getRuntime().availableProcessors() * 2); // 서버가 실행되는 컴퓨터의 논리 코어 * 2
        config.setLockWatchdogTimeout(1500); // ttl 시간과 ttl시간/3 만큼 자동연장 (default = 30초)
        config.useClusterServers() // 클러스터 모드 설정
                /**
                 *  클러스터 노드 주소 설정
                 *   redis -> valkey
                 *   valkey는 redis보다 유지비용 절감과 높은 tps를 기록하는 측면에서 강세를 보임
                 *   최소 6개의 클러스터 (master : 3개 , slave : 3개)
                 */
                .addNodeAddress("valkey://127.0.0.1:6379")
                .addNodeAddress("valkey://127.0.0.1:6380")
                .addNodeAddress("valkey://127.0.0.1:6381")
                .addNodeAddress("valkey://127.0.0.1:6382")
                .addNodeAddress("valkey://127.0.0.1:6383")
                .addNodeAddress("valkey://127.0.0.1:6384")
                .setRetryAttempts(10)            // 연결 재시도 (default = 4)
                .setPingConnectionInterval(30000) // 시간마다 연결 유효성 체크 (default = 30초)
                .setTimeout(3000)                // 한번 요청할때 마다 기다리는 시간 (default = 3초)
                .setConnectTimeout(10000)      // 처음 연결할 때 기다리는 시간 (default = 10초)
                .setScanInterval(1000)         // 주기적으로 노드 상태 검증 (default = 1초)
                .setMasterConnectionPoolSize(64)  // 마스터 노드당 커넥션 연결 최대개수 (default = 64개)
                .setSlaveConnectionPoolSize(64)  // 슬레이브 노드당 커넥션 연결 최대개수 (default = 64개)
                .setMasterConnectionMinimumIdleSize(24) // 마스터 노드당 커넥션 연결 최소개수 (default = 24개)
                .setSlaveConnectionMinimumIdleSize(24) // 슬레이브 노드당 커넥션 연결 최소개수 (default = 24개)
                .setReadMode(ReadMode.SLAVE); // 슬레이브 노드는 읽기 전용 처리

        return Redisson.create(config); // redisson 설정
    }
}
