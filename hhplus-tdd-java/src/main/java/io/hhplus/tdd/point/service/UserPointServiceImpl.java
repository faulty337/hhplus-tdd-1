package io.hhplus.tdd.point.service;


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
        UserPoint userPoint = userPointRepository.findById(userId);
        return new UserPointResponse(userPoint.id(), userPoint.point());
    }
}
