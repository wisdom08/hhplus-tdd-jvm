package io.hhplus.tdd.point.business.strategy;

import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;

public class ChargeStrategy implements TransactionTypeStrategy {

    @Override
    public long act(UserPoint currentUserPoint, long pointToAct) {
        return currentUserPoint.chargePoint(pointToAct);
    }

    @Override
    public boolean isTransactionTypeSupported(TransactionType transactionType) {
        return transactionType == TransactionType.CHARGE;
    }
}
