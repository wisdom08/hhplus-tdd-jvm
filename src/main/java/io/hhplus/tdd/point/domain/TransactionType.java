package io.hhplus.tdd.point.domain;


import io.hhplus.tdd.point.business.strategy.ChargeOperator;
import io.hhplus.tdd.point.business.strategy.TransactionTypeStrategy;
import io.hhplus.tdd.point.business.strategy.UseOperator;
import lombok.Getter;

/**
 * 포인트 트랜잭션 종류
 * - CHARGE : 충전
 * - USE : 사용
 */
@Getter
public enum TransactionType {
    CHARGE(new ChargeOperator()),
    USE(new UseOperator());

    private final TransactionTypeStrategy strategy;

    TransactionType(TransactionTypeStrategy strategy) {
        this.strategy = strategy;
    }
}