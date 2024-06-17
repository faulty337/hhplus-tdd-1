package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserPointRepository userPointRepository;

    @MockBean
    private PointHistoryRepository pointHistoryRepository;


    private long userId;

    private long point;

    private final List<PointHistory> pointHistoryList = new ArrayList<>();

    @BeforeEach
    public void setUP(){
        userId = 1L;
        point = 100L;

        for(int i = 0; i < 5; i++){
            pointHistoryList.add(new PointHistory(i, userId, 20, TransactionType.CHARGE, System.currentTimeMillis()));
        }

        UserPoint userPoint = new UserPoint(userId, point);
        when(userPointRepository.findById(userId)).thenReturn(Optional.of(userPoint));
        when(pointHistoryRepository.findAllByUserId(userId)).thenReturn(pointHistoryList);

    }


    //기본 API의 작동 테스트
    @Test
    @DisplayName("포인트 조회 - 작동 테스트")
    void pointAPISuccessTest() throws Exception {

        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userid").value(userId))
                .andExpect(jsonPath("$.userPoint").isNumber())
                .andExpect(jsonPath("$.userPoint").value(point));
    }

    //userId의 자료형(long)이외 값에 대한 예외 처리
    //원래는 CustomException을 Throw하고 싶었으나 시간상 예외에 대한 테스트만 구현
    @Test
    @DisplayName("포인트 조회 - 정수 이외 테스트")
    void pointAPIParameterExceptionTest() throws Exception {
        String userId = "테스트";
        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().is4xxClientError());
    }

    //없는 userId에 대한 예외 처리
    //다른 분들께 물어보니 보통 없는 userId가 들어오는 경우를 제외.. 했다고는 하지만 기본적으로 있어야 하는 예외라고 생각
    //Optional로 감싸 없는 경우를 강제로 생성(원래는 UserPointTable에 getOrDefault 함수로 인해 없는 경우 절대X)
    @Test
    @DisplayName("포인트 조회 - 없는 사용자 테스트")
    void pointAPINotFoundUserIdExceptionTest() throws Exception {
        long nonUserId = 999L;

        when(userPointRepository.findById(nonUserId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/point/{id}", nonUserId))
                .andExpect(status().is5xxServerError());
    }

    //기본 API 작동 테스트
    @Test
    @DisplayName("포인트 로그 조회 - 작동 테스트")
    void pointHistoryAPISuccessTest() throws Exception {

        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(pointHistoryList.size()))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].amount").value(20));
    }

    //userId의 자료형(long)이외 값에 대한 예외 처리
    //원래는 CustomException을 Throw하고 싶었으나 시간상 예외에 대한 테스트만 구현
    @Test
    @DisplayName("포인트 로그 조회 - 정수 이외 테스트")
    void pointHistoryAPIParameterExceptionTest() throws Exception {
        String userId = "테스트";
        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().is4xxClientError());
    }


}