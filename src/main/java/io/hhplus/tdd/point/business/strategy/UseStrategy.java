package io.hhplus.tdd.point.business.strategy;

import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;

public class UseStrategy implements TransactionTypeStrategy {
    @Override
    public long act(UserPoint currentUserPoint, long pointToAct) {
        return currentUserPoint.usePoint(pointToAct);
    }

    @Override
    public boolean isTransactionTypeSupported(TransactionType transactionType) {
        return transactionType == TransactionType.USE;
    }
}
