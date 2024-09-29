package io.hhplus.tdd.point.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record PointHistory(
        long id,
        long userId,
        long amount,
        TransactionType type,
        long updateMillis
) {

    public static void isPointHistory(List<PointHistory> pointHistories) {
        if (pointHistories.isEmpty()) {
            throw new IllegalArgumentException("포인트 히스토리가 없습니다.");
        }
    }
}
