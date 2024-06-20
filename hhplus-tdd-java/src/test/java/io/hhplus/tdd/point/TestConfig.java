package io.hhplus.tdd.point;

import io.hhplus.tdd.ConcurrentManager;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.service.PointServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    public UserPointRepository userPointRepository() {
        return new UserPointTestRepository();
    }

    @Bean
    public PointHistoryRepository pointHistoryRepository() {
        return new PointHistoryTestRepository();
    }

    @Bean
    public ConcurrentManager concurrentManager(){
        return new ConcurrentManager();
    }

    @Bean
    @Primary
    public PointService pointService(UserPointRepository userPointRepository,
                                     PointHistoryRepository pointHistoryRepository,
                                     ConcurrentManager concurrentManager
    ) {
        return new PointServiceImpl(userPointRepository, pointHistoryRepository, concurrentManager);
    }
}