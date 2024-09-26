package io.hhplus.tdd.point.business;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.UserPointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {

    private final UserPointRepository userPointRepository;
    private final PointTransactionHandler pointTransactionHandler;
    private final PointHistoryHandler pointHistoryHandler;

    public PointService(UserPointRepository userPointRepository, PointTransactionHandler pointTransactionHandler, PointHistoryHandler pointHistoryHandler) {
        this.userPointRepository = userPointRepository;
        this.pointTransactionHandler = pointTransactionHandler;
        this.pointHistoryHandler = pointHistoryHandler;
    }

    public UserPoint getPointBy(long userId) {
        return userPointRepository.selectById(userId);
    }

    public List<PointHistory> getPointHistoryBy(long userId) {
        return pointHistoryHandler.getPointHistories(userId);
    }

    public UserPoint chargePoint(long id, long pointToCharge) {
        return pointTransactionHandler.handleTransaction(id, pointToCharge, TransactionType.CHARGE);
    }

    public UserPoint usePoint(long id, long pointToUse) {
        return pointTransactionHandler.handleTransaction(id, pointToUse, TransactionType.USE);
    }
}
