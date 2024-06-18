package io.hhplus.tdd.point.service;


import io.hhplus.tdd.CustomException;
import io.hhplus.tdd.ErrorCode;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.PointHistoryResponse;
import io.hhplus.tdd.point.dto.UserPointChargeResponse;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.dto.UserPointUseResponse;
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

    @Override
    public UserPointChargeResponse chargePoint(long userId, long amount) {
        if(amount <= 0){
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        //유효성 검증
        UserPoint userpoint = userPointRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        long prPoint = userpoint.point();

        try {
            userpoint = userPointRepository.update(userId, prPoint + amount);
            pointHistoryRepository.save(userId, amount, TransactionType.CHARGE);

            return new UserPointChargeResponse(userId, userpoint.point(), prPoint, amount);
        } catch (Exception e) {
            //Transaction이 없기에 임시 처리
            userPointRepository.update(userId, prPoint);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserPointUseResponse usePoint(long userId, long amount) {
        UserPoint userPoint = userPointRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        long prPoint = userPoint.point();
        if(prPoint < amount){
            throw new CustomException(ErrorCode.INSUFFICIENT_FUNDS);
        }

        try {
            userPoint = userPointRepository.update(userId, prPoint - amount);
            pointHistoryRepository.save(userId, amount, TransactionType.USE);
            return new UserPointUseResponse(userId, amount, userPoint.point());
        } catch (Exception e) {
            //Transaction이 없기에 임시 처리
            userPointRepository.update(userId, prPoint);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
}
