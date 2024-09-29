package io.hhplus.tdd.point.business;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.UserPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointService service;

    @Mock
    private UserPointRepository repository;

    @Mock
    private PointTransactionHandler transactionHandler;

    @Mock
    private PointHistoryHandler historyHandler;

    private UserPoint userPoint;
    private List<PointHistory> pointHistories;
    private static final long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        userPoint = UserPoint.builder().id(USER_ID).point(0L).build();
        pointHistories = List.of(PointHistory.builder().id(USER_ID).build());
    }

    @DisplayName("포인트 조회 시 UserPoint가 정상 반환된다")
    @Test
    void shouldReturnUserPointWhenRequested() {
        // given
        when(repository.selectById(USER_ID)).thenReturn(userPoint);

        // when
        UserPoint res = service.getPointBy(USER_ID);

        // then
        assertThat(res).isEqualTo(userPoint);
    }

    @DisplayName("포인트 히스토리 조회 시 List<PointHistory>이 정상 반환된다")
    @Test
    void shouldReturnPointHistoriesWhenRequested() {
        // given
        when(historyHandler.getPointHistories(USER_ID)).thenReturn(pointHistories);

        // when
        List<PointHistory> result = service.getPointHistoryBy(USER_ID);

        // then
        assertThat(result).isEqualTo(pointHistories);
    }

    @DisplayName("포인트 충전 시 UserPoint가 충전되고 UserPoint가 정상 반환된다")
    @Test
    void shouldChargePointAndReturnUpdatedUserPoint() {
        // given
        when(transactionHandler.handleTransaction(USER_ID, 100, TransactionType.CHARGE)).thenReturn(UserPoint.builder().id(USER_ID).point(200).build());

        // when
        UserPoint result = service.chargePoint(USER_ID, 100);

        // then
        assertThat(result).isEqualTo(UserPoint.builder().id(USER_ID).point(200).build());
    }

    @DisplayName("포인트 사용 시 UserPoint가 충전되고 UserPoint가 정상 반환된다")
    @Test
    void usePoint() {
        // given
        when(transactionHandler.handleTransaction(USER_ID, 100, TransactionType.USE)).thenReturn(UserPoint.builder().id(USER_ID).point(0).build());

        // when
        UserPoint result = service.usePoint(USER_ID, 100);

        // then
        assertThat(result).isEqualTo(UserPoint.builder().id(USER_ID).point(0).build());
    }

}