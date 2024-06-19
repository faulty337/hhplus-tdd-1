package io.hhplus.tdd.point;

import io.hhplus.tdd.point.repository.UserPointRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserPointTestRepository implements UserPointRepository {

    private final Map<Long, UserPoint> table = new HashMap<>();

    @Override
    public Optional<UserPoint> findById(long id) {
        return Optional.of(table.getOrDefault(id, UserPoint.empty(id)));
    }

    @Override
    public Optional<UserPoint> save(UserPoint userPoint) {
        table.put(userPoint.id(), userPoint);
        return Optional.of(userPoint);
    }

    @Override
    public UserPoint update(long userId, long amount) {
        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());
        table.put(userPoint.id(), userPoint);
        return userPoint;
    }

    public boolean clear(){
        try{
            table.clear();
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
