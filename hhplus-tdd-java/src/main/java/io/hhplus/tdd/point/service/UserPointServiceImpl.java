package io.hhplus.tdd.point.service;


import io.hhplus.tdd.CustomException;
import io.hhplus.tdd.ErrorCode;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.repository.UserPointRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPointServiceImpl implements UserPointService{

    private final UserPointRepository userPointRepository;

    public UserPointServiceImpl(UserPointRepository userPointRepository) {
        this.userPointRepository = userPointRepository;
    }


    @Override
    public UserPointResponse getUserPoint(long userId) {
        //UserPointTable 내부 selectById()함수에서 getOrDefault()를 사용하기에 절대 없는 유저에 대한 상황이 나올 수 없음.
        UserPoint userPoint = userPointRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        return new UserPointResponse(userPoint.id(), userPoint.point());
    }
}
