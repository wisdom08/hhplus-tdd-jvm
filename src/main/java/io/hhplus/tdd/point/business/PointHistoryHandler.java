package io.hhplus.tdd.point.business;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.infrastructure.PointHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointHistoryHandler {

    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryHandler(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public List<PointHistory> getPointHistories(long userId) {
        List<PointHistory> pointHistories = pointHistoryRepository.selectAllByUserId(userId);
        PointHistory.isPointHistory(pointHistories);
        return pointHistories;
    }

    public void addPointHistory(long userId, long newPoint, TransactionType transactionType) {
        pointHistoryRepository.insert(userId, newPoint, transactionType, System.currentTimeMillis());
    }
}
