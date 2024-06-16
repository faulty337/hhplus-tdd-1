package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.UserPoint;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserPointRepository {
    public Optional<UserPoint> findById(long id);
}
