package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.service.PointServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
@Import(TestConfig.class)
public class ConcurrencyTest {

    @Autowired
    private UserPointTestRepository userPointRepository;

    @Autowired
    private PointHistoryTestRepository pointHistoryRepository;

    @Autowired
    PointService pointService;

    private static long userId;
    private static long point;

    @BeforeAll
    public static void beforeAll() {
        userId = 1L;
        point = 1000L;
    }

    @BeforeEach
    void setTable() {
        userPointRepository.clear();
        pointHistoryRepository.clear();

        userPointRepository.save(new UserPoint(userId, point));
    }

    @Test
    void chargePointConcurrencyTest() {
        long amount1 = 100;
        long amount2 = 200;

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> pointService.chargePoint(userId, amount1)),
                CompletableFuture.runAsync(() -> pointService.chargePoint(userId, amount2))
        ).join();

        UserPoint userPoint = userPointRepository.findById(userId).orElseThrow();

        assertEquals(point + amount2 + amount1, userPoint.point());

    }

    @Test
    void usePointConcurrencyTest() {
        long amount1 = 100;
        long amount2 = 200;

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> pointService.usePoint(userId, amount1)),
                CompletableFuture.runAsync(() -> pointService.usePoint(userId, amount2))
        ).join();

        UserPoint userPoint = userPointRepository.findById(userId).orElseThrow();

        assertEquals(point - amount2 - amount1, userPoint.point());

    }
}
