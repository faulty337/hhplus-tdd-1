package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.dto.PointHistoryResponse;
import io.hhplus.tdd.point.dto.UserPointChargeResponse;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.dto.UserPointUseResponse;

import java.util.List;

public interface PointService {
    UserPointResponse getUserPoint(long userId);

    List<PointHistoryResponse> getPointHistory(long userId);

    UserPointChargeResponse chargePoint(long userId, long amount);

    UserPointUseResponse usePoint(long userId, long amount);
}
