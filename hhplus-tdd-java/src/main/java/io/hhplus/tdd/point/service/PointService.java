package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.dto.PointHistoryResponse;
import io.hhplus.tdd.point.dto.UserPointResponse;

import java.util.List;

public interface PointService {
    UserPointResponse getUserPoint(long userId);

    List<PointHistoryResponse> getPointHistory(long userId);
}
