package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository{
    private final PointHistoryTable pointHistoryTable;

    public PointHistoryRepositoryImpl(PointHistoryTable pointHistoryTable) {
        this.pointHistoryTable = pointHistoryTable;
    }

    @Override
    public List<PointHistory> findAllByUserId(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }

    @Override
    public PointHistory save(long userid, long amount, TransactionType type) {
        return pointHistoryTable.insert(userid, amount, type, System.currentTimeMillis());
    }


}
