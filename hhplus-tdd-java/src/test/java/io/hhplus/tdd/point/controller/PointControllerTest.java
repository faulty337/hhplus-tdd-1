package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.UserPointRepository;
import io.hhplus.tdd.point.repository.UserPointRepositoryImpl;
import io.hhplus.tdd.point.service.UserPointService;
import io.hhplus.tdd.point.service.UserPointServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

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


    private long userId;

    private long point;

    @BeforeEach
    public void setUP(){
        userId = 1L;
        point = 100L;

        UserPoint userPoint = new UserPoint(userId, point);
        when(userPointRepository.findById(userId)).thenReturn(Optional.of(userPoint));

    }

    @Test
    @DisplayName("포인트 조회 - 작동 테스트")
    void pointAPISuccessTest() throws Exception {

        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userid").value(userId))
                .andExpect(jsonPath("$.userPoint").isNumber())
                .andExpect(jsonPath("$.userPoint").value(point));
    }

    @Test
    @DisplayName("포인트 조회 - 정수 이외 테스트")
    void pointAPIParameterExceptionTest() throws Exception {
        String userId = "테스트";
        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("포인트 조회 - 없는 사용자 테스트")
    void pointAPINotFoundUserIdExceptionTest() throws Exception {
        long nonUserId = 999L;

        when(userPointRepository.findById(nonUserId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/point/{id}", nonUserId))
                .andExpect(status().is5xxServerError());
    }

}