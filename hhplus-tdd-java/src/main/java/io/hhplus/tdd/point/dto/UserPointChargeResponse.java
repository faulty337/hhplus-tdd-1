package io.hhplus.tdd.point.dto;

public record UserPointChargeResponse (
        long userId,
        long userPoint,
        long prPoint,
        long amount

){
}
