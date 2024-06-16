package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserPointRepositoryImpl implements UserPointRepository{

    private final UserPointTable userPointTable;


    public UserPointRepositoryImpl(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
    }

    @Override
    public Optional<UserPoint> findById(long id) {

        return Optional.of(userPointTable.selectById(id));
    }

    @Override
    public Optional<UserPoint> save(UserPoint userPoint) {
        return Optional.of(userPointTable.insertOrUpdate(userPoint.id(), userPoint.point()));
    }


}
