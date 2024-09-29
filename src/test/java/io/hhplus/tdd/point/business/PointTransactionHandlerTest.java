package io.hhplus.tdd.point.business;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointTransactionHandlerTest {

    @InjectMocks
    private PointTransactionHandler transactionHandler;

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryHandler pointHistoryHandler;

    private UserPoint userPoint;

    private static final long USER_ID = 1L;
    private static final long INITIAL_POINT = 200L;

    @BeforeEach
    void setUp() {
        userPoint = UserPoint.builder().id(USER_ID).point(INITIAL_POINT).build();
    }

    @DisplayName("포인트 충전 시 UserPoint가 업데이트되고 히스토리가 추가된다")
    @Test
    void shouldChargePointsAndAddHistory() {
        // given
        long pointToCharge = 100L;
        when(userPointRepository.selectById(USER_ID)).thenReturn(userPoint);
        when(userPointRepository.insertOrUpdate(USER_ID, INITIAL_POINT + pointToCharge)).thenReturn(userPoint);

        // when
        UserPoint result = transactionHandler.handleTransaction(USER_ID, pointToCharge, TransactionType.CHARGE);

        // then
        verify(userPointRepository).selectById(USER_ID);
        verify(userPointRepository).insertOrUpdate(USER_ID, INITIAL_POINT + pointToCharge);
        verify(pointHistoryHandler).addPointHistory(USER_ID, INITIAL_POINT + pointToCharge, TransactionType.CHARGE);

        assertThat(result).isEqualTo(userPoint);
    }

    @DisplayName("포인트 사용 시 UserPoint가 업데이트되고 히스토리가 추가된다")
    @Test
    void shouldUsePointsAndAddHistory() {
        // given
        long pointToUse = 50L;
        when(userPointRepository.selectById(USER_ID)).thenReturn(userPoint);
        when(userPointRepository.insertOrUpdate(USER_ID, INITIAL_POINT - pointToUse)).thenReturn(userPoint);

        // when
        UserPoint result = transactionHandler.handleTransaction(USER_ID, pointToUse, TransactionType.USE);

        // then
        verify(userPointRepository).selectById(USER_ID);
        verify(userPointRepository).insertOrUpdate(USER_ID, INITIAL_POINT - pointToUse);
        verify(pointHistoryHandler).addPointHistory(USER_ID, INITIAL_POINT - pointToUse, TransactionType.USE);

        assertThat(result).isEqualTo(userPoint);
    }
}