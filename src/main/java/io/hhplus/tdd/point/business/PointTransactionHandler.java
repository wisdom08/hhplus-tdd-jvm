package io.hhplus.tdd.point.business;

import io.hhplus.tdd.point.business.strategy.TransactionTypeStrategy;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.UserPointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointTransactionHandler {

    private final UserPointRepository userPointRepository;
    private final PointHistoryHandler pointHistoryHandler;
    private final List<TransactionTypeStrategy> transactionTypeOperator;

    public PointTransactionHandler(UserPointRepository userPointRepository, PointHistoryHandler pointHistoryHandler, List<TransactionTypeStrategy> transactionTypeOperator) {
        this.userPointRepository = userPointRepository;
        this.pointHistoryHandler = pointHistoryHandler;
        this.transactionTypeOperator = transactionTypeOperator;
    }

    public UserPoint handleTransaction(long userId, long point, TransactionType transactionType) {
        UserPoint currentPoint = userPointRepository.selectById(userId);

        TransactionTypeStrategy transactionTypeOperator = this.transactionTypeOperator.stream().filter(operator -> operator.isTransactionTypeSupported(transactionType))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("지원하지 않는 트랜잭션 타입: " + transactionType));

        long newPoint = transactionTypeOperator.act(currentPoint, point);
        pointHistoryHandler.addPointHistory(userId, newPoint, transactionType);
        return userPointRepository.insertOrUpdate(userId, newPoint);
    }
}
