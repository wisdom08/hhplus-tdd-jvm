package io.hhplus.tdd.point.domain;


import io.hhplus.tdd.point.business.strategy.ChargeStrategy;
import io.hhplus.tdd.point.business.strategy.TransactionTypeStrategy;
import io.hhplus.tdd.point.business.strategy.UseStrategy;

/**
 * 포인트 트랜잭션 종류
 * - CHARGE : 충전
 * - USE : 사용
 */
public enum TransactionType {
    CHARGE {
        @Override
        public TransactionTypeStrategy getStrategyByType() {
            return new ChargeStrategy();
        }
    }, USE {
        @Override
        public TransactionTypeStrategy getStrategyByType() {
            return new UseStrategy();
        }
    };

    public abstract TransactionTypeStrategy getStrategyByType();
}