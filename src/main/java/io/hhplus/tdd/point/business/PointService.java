package io.hhplus.tdd.point.business;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.UserPointRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PointService {

    private final UserPointRepository userPointRepository;
    private final PointTransactionHandler pointTransactionHandler;
    private final PointHistoryHandler pointHistoryHandler;

    private final Map<Long, Lock> userLock = new ConcurrentHashMap<>();

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
        return handleTransactionWithLock(id, pointToCharge, TransactionType.CHARGE);
    }

    public UserPoint usePoint(long id, long pointToUse) {
        return handleTransactionWithLock(id, pointToUse, TransactionType.USE);
    }

    private UserPoint handleTransactionWithLock(long userId, long point, TransactionType transactionType) {
        Lock lock = userLock.computeIfAbsent(userId, k -> new ReentrantLock());
        lock.lock();

        try {
            return pointTransactionHandler.handleTransaction(userId, point, transactionType);
        } finally {
            lock.unlock();
        }
    }
}
