package io.hhplus.tdd.point.business;

import io.hhplus.tdd.point.business.strategy.TransactionTypeStrategy;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.UserPointRepository;
import org.springframework.stereotype.Service;

@Service
public class PointTransactionHandler {

    private final UserPointRepository userPointRepository;
    private final PointHistoryHandler pointHistoryHandler;

    public PointTransactionHandler(UserPointRepository userPointRepository, PointHistoryHandler pointHistoryHandler) {
        this.userPointRepository = userPointRepository;
        this.pointHistoryHandler = pointHistoryHandler;
    }

    public UserPoint handleTransaction(long userId, long point, TransactionType transactionType) {
        UserPoint currentPoint = userPointRepository.selectById(userId);
        TransactionTypeStrategy strategy = transactionType.getStrategyByType();
        long newPoint = strategy.act(currentPoint, point);

        pointHistoryHandler.addPointHistory(userId, newPoint, transactionType);

        return userPointRepository.insertOrUpdate(userId, newPoint);
    }
}
