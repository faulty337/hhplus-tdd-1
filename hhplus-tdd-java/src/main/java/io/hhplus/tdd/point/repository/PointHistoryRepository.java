package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;

import java.util.List;
import java.util.Optional;

public interface PointHistoryRepository {

    List<PointHistory> findAllByUserId(long userId);

    Optional<PointHistory> save(long userid, long amount, TransactionType type);

}
