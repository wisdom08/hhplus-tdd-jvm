package io.hhplus.tdd.point.domain;

import lombok.Builder;

@Builder
public record UserPoint(
        long id,
        long point,
        long updateMillis
) {
    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public long chargePoint(long pointToCharge) {
        validPointToCharge(pointToCharge);
        return this.point + pointToCharge;
    }

    public long usePoint(long pointToUse) {
        validPointToUse(pointToUse);
        return this.point - pointToUse;
    }

    private void validPointToCharge(Long pointToCharge) {
        if (pointToCharge <= 0) {
            throw new IllegalArgumentException("충전하려는 포인트는 0보다 커야 합니다");
        }
        if (pointToCharge > 1000000) {
            throw new IllegalArgumentException("최대 충전 포인트는 백만원입니다");
        }
    }

    private void validPointToUse(Long pointToUse) {
        if (this.point < pointToUse) {
            throw new IllegalArgumentException("잔고가 부족합니다");
        }
        if(pointToUse <= 0) {
            throw new IllegalArgumentException("사용하려는 포인트는 0보다 커야 합니다");
        }
    }
}
