package io.hhplus.tdd.point;

import io.hhplus.tdd.point.repository.PointHistoryRepository;

import java.util.ArrayList;
import java.util.List;

public class PointHistoryTestRepository implements PointHistoryRepository {
    private final List<PointHistory> table = new ArrayList<>();
    private long cursor = 1;

    @Override
    public List<PointHistory> findAllByUserId(long userId) {
        return table.stream().filter(pointHistory -> pointHistory.userId() == userId).toList();
    }

    @Override
    public PointHistory save(long userId, long amount, TransactionType type) {
        PointHistory pointHistory = new PointHistory(cursor++, userId, amount, type, System.currentTimeMillis());
        table.add(pointHistory);
        return pointHistory;
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
