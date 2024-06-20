package io.hhplus.tdd.point.service;

import io.hhplus.tdd.CustomException;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TestConfig;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.PointHistoryResponse;
import io.hhplus.tdd.point.dto.UserPointChargeResponse;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.dto.UserPointUseResponse;
import io.hhplus.tdd.point.repository.PointHistoryRepositoryImpl;
import io.hhplus.tdd.point.repository.UserPointRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@Import(TestConfig.class)
class PointServiceTest {
    @Mock
    private UserPointRepositoryImpl userPointRepository;

    @Mock
    private PointHistoryRepositoryImpl pointHistoryRepository;

    @InjectMocks
    private PointServiceImpl pointService;

    private static long userId;
    private static long point;
    private static long amount;
    private static final List<PointHistory> pointHistoryList = new ArrayList<>();
    private static UserPoint userPoint;
    private static PointHistory chargePointHistory;

    @BeforeAll
    static void setUp() {
        userId = 1L;
        point = 100L;
        amount = 20L;
        userPoint = new UserPoint(userId, point);
        int i = 0;
        for(; i < 5; i++){
            pointHistoryList.add(new PointHistory(i, userId, amount, TransactionType.CHARGE, System.currentTimeMillis()));
        }

        chargePointHistory = new PointHistory(i, userId, amount, TransactionType.CHARGE, System.currentTimeMillis());
    }

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    //기본 Service의 작동 테스트
    @Test
    @DisplayName("포인트 조회 - 서비스 로직 작동 테스트")
    void getUserPointSuccessPointTest() {
        UserPoint userPoint = new UserPoint(userId, point, System.currentTimeMillis());

        // given
        given(userPointRepository.findById(userId)).willReturn(Optional.of(userPoint));

        // when
        UserPointResponse response = pointService.getUserPoint(userId);

        // then
        assertNotNull(response);
        assertEquals(userId, response.userid());
        assertEquals(point, response.userPoint());
        then(userPointRepository).should(times(1)).findById(userId);
    }

    //없는 userId에 대한 예외 테스트
    @Test
    @DisplayName("포인트 조회 - Not Found UserId Exception 테스트")
    void getUserPointNotFoundUserIdTest(){
        // given
        given(userPointRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> {
            pointService.getUserPoint(userId);
        });
    }

    //기본 포인트 로그 조회 Service 테스트
    @Test
    @DisplayName("포인트 로그 조회 - 서비스 로직 작동 테스트")
    void getPointHistorySuccessTest() {
        // given
        given(pointHistoryRepository.findAllByUserId(userId)).willReturn(pointHistoryList);

        // when
        List<PointHistoryResponse> response = pointService.getPointHistory(userId);

        // then
        assertNotNull(response);
        assertEquals(pointHistoryList.size(), response.size());
        assertEquals(pointHistoryList.get(0).userId(), response.get(0).userId());
        then(pointHistoryRepository).should(times(1)).findAllByUserId(userId);
    }

    //기본 포인트 충전 작동 테스트
    @Test
    @DisplayName("포인트 충전 - 서비스 로직 작동 테스트")
    void chargePointSuccessTest() {
        UserPoint afterUserPoint = new UserPoint(userId, amount + point, System.currentTimeMillis());

        // given
        given(userPointRepository.findById(userId)).willReturn(Optional.of(userPoint));
        given(userPointRepository.update(userId, point + amount)).willReturn(afterUserPoint);
        given(pointHistoryRepository.save(userId, amount, TransactionType.CHARGE)).willReturn(chargePointHistory);

        // when
        UserPointChargeResponse response = pointService.chargePoint(userId, amount);

        // then
        assertNotNull(response);
        assertEquals(response.prPoint(), point);
        assertEquals(response.userPoint(), point + amount);
        assertEquals(response.amount(),  amount);
        then(userPointRepository).should(times(1)).findById(userId);
        then(userPointRepository).should(times(1)).update(userId, point + amount);
        then(pointHistoryRepository).should(times(1)).save(userId, amount, TransactionType.CHARGE);
    }

    //0 혹은 음수에 대한 충전 시도 시 특정 예외를 터트리는 지에 대한 테스트
    @Test
    @DisplayName("포인트 충전 - 양의 정수 이외 예외 테스트")
    void chargePointParameterExceptionTest() {
        long minus = -100L;
        long zero = 0L;

        // when & then
        assertThrows(CustomException.class, () -> {
            pointService.chargePoint(userId, minus);
        });

        assertThrows(CustomException.class, () -> {
            pointService.chargePoint(userId, zero);
        });
    }

    //포인트 충전 시도시 없는 유저에 대해 시도시 예외를 터트리는 지에 대한 테스트
    @Test
    @DisplayName("포인트 충전 - Not Found UserId Exception 테스트")
    void chargePointNotFoundUserExceptionTest() {
        // given
        given(userPointRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> {
            pointService.chargePoint(userId, amount);
        });
    }

    //기본 포인트 사용 테스트
    @Test
    @DisplayName("포인트 사용 - 서비스 로직 작동 테스트")
    void usePointSuccessTest() {
        UserPoint afterUserPoint = new UserPoint(userId, point - amount, System.currentTimeMillis());

        // given
        given(userPointRepository.findById(userId)).willReturn(Optional.of(userPoint));
        given(userPointRepository.update(userId, point - amount)).willReturn(afterUserPoint);
        given(pointHistoryRepository.save(userId, amount, TransactionType.USE)).willReturn(chargePointHistory);

        // when
        UserPointUseResponse response = pointService.usePoint(userId, amount);

        // then
        assertNotNull(response);
        assertEquals(response.point(), point - amount);
        assertEquals(response.userId(), userId);
        assertEquals(response.amount(), amount);
        then(userPointRepository).should(times(1)).findById(userId);
        then(userPointRepository).should(times(1)).update(userId, point - amount);
        then(pointHistoryRepository).should(times(1)).save(userId, amount, TransactionType.USE);
    }

    //보유 포인트보다 초과되는 사용 요청에 대한 예외 테스트
    @Test
    @DisplayName("포인트 사용 - 초과 사용 예외 테스트")
    void usePointOveruseTest() {
        long overAmount = userId + 1;

        // when & then
        assertThrows(CustomException.class, () -> {
            pointService.usePoint(userId, overAmount);
        });
    }

    // 유저를 못찾았을 때에 대한 예외 테스트
    @Test
    @DisplayName("포인트 사용 - Not Found UserId Exception 테스트")
    void usePointNotFoundUserExceptionTest() {
        // when & then
        assertThrows(CustomException.class, () -> {
            pointService.usePoint(userId, amount);
        });
    }

    @Test
    @DisplayName("포인트 사용 - 양의 정수 이외 예외 테스트")
    void usePointParameterExceptionTest() {
        long minus = -100L;
        long zero = 0L;

        // when & then
        assertThrows(CustomException.class, () -> {
            pointService.usePoint(userId, minus);
        });

        assertThrows(CustomException.class, () -> {
            pointService.usePoint(userId, zero);
        });
    }
}
