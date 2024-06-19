package io.hhplus.tdd.point.dto;

public record UserPointUseResponse (
    long userId,
    long amount,
    long point
){
}
