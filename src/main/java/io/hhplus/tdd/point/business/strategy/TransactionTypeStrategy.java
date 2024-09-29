package io.hhplus.tdd.point.business.strategy;

import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;

public interface TransactionTypeStrategy {
    long act(UserPoint currentUserPoint, long pointToAct);

    boolean isTransactionTypeSupported(TransactionType transactionType);
}
