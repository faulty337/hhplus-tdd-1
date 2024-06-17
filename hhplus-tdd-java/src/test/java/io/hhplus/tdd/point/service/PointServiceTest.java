package io.hhplus.tdd.point.service;

import io.hhplus.tdd.CustomException;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.repository.UserPointRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointServiceTest {
    @Mock
    private UserPointRepositoryImpl userPointRepository;

    @InjectMocks
    private UserPointServiceImpl userPointService;
    private long userId;
    private long point;
    @BeforeEach
    void setUp() {
        userId = 1L;
        point = 1L;
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
        UserPoint userPoint = new UserPoint(userId, point, System.currentTimeMillis());

        when(userPointRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () ->{
            userPointService.getUserPoint(userId);
        });
    }

}
