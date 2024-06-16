package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.dto.UserPointResponse;

public interface UserPointService {
    UserPointResponse getUserPoint(long userId);
}
