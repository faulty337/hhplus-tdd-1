package io.hhplus.tdd.point.service;

import io.hhplus.tdd.CustomException;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.PointHistoryResponse;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.PointHistoryRepositoryImpl;
import io.hhplus.tdd.point.repository.UserPointRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointServiceTest {
    @Mock
    private UserPointRepositoryImpl userPointRepository;

    @Mock
    private PointHistoryRepositoryImpl pointHistoryRepository;

    @InjectMocks
    private PointServiceImpl userPointService;

    private long userId;
    private long point;
    private final List<PointHistory> pointHistoryList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userId = 1L;
        point = 100L;

        for(int i = 0; i < 5; i++){
            pointHistoryList.add(new PointHistory(i, userId, 20, TransactionType.CHARGE, System.currentTimeMillis()));
        }

        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("포인트 조회 - 서비스 로직 작동 테스트")
    void getUserPointSuccessPointTest() {

        UserPoint userPoint = new UserPoint(userId, point, System.currentTimeMillis());

        when(userPointRepository.findById(userId)).thenReturn(Optional.of(userPoint));

        UserPointResponse response = userPointService.getUserPoint(userId);
        assertNotNull(response);
        assertEquals(userId, response.userid());
        assertEquals(point, response.userPoint());
        verify(userPointRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("포인트 조회 - Not Found UserId Exception 테스트")
    void getUserPointNotFoundUserIdTest(){

        when(userPointRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () ->{
            userPointService.getUserPoint(userId);
        });
    }

    //기본 포인트 로그 조회 Service 테스트
    @Test
    @DisplayName("포인트 로그 조회 - 서비스 로직 작동 테스트")
    void getPointHistorySuccessPointTest() {

        when(pointHistoryRepository.findAllByUserId(userId)).thenReturn(pointHistoryList);

        List<PointHistoryResponse> response = userPointService.getPointHistory(userId);
        assertNotNull(response);
        assertEquals(pointHistoryList.size(), response.size());
        assertEquals(pointHistoryList.get(0).userId(), response.get(0).userId());
        verify(pointHistoryRepository, times(1)).findAllByUserId(userId);


    }



}
