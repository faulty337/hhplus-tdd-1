package io.hhplus.tdd.point.service;


import io.hhplus.tdd.CustomException;
import io.hhplus.tdd.ErrorCode;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.PointHistoryResponse;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointServiceImpl implements PointService {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public PointServiceImpl(UserPointRepository userPointRepository, PointHistoryRepository pointHistoryRepository) {
        this.userPointRepository = userPointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }


    @Override
    public UserPointResponse getUserPoint(long userId) {
        //UserPointTable 내부 selectById()함수에서 getOrDefault()를 사용하기에 절대 없는 유저에 대한 상황이 나올 수 없음.
        UserPoint userPoint = userPointRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        return new UserPointResponse(userPoint.id(), userPoint.point());
    }

    @Override
    public List<PointHistoryResponse> getPointHistory(long userId) {
        //User ≠ UserPoint
        return pointHistoryRepository.findAllByUserId(userId).stream().map(
                pointHistory -> new PointHistoryResponse(
                        pointHistory.userId(),
                        pointHistory.amount(),
                        pointHistory.type(),
                        pointHistory.updateMillis())
        ).toList();
    }
}
