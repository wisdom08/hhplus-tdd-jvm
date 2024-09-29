package io.hhplus.tdd.point.domain;


import io.hhplus.tdd.point.business.strategy.ChargeStrategy;
import io.hhplus.tdd.point.business.strategy.TransactionTypeStrategy;
import io.hhplus.tdd.point.business.strategy.UseStrategy;
import lombok.Getter;

/**
 * 포인트 트랜잭션 종류
 * - CHARGE : 충전
 * - USE : 사용
 */
@Getter
public enum TransactionType {
    CHARGE(new ChargeStrategy()),
    USE(new UseStrategy());

    private final TransactionTypeStrategy strategy;

    TransactionType(TransactionTypeStrategy strategy) {
        this.strategy = strategy;
    }
}