package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
