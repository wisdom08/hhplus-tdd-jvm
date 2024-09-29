package io.hhplus.tdd.point.business;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.infrastructure.PointHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointHistoryHandlerTest {

    @InjectMocks
    private PointHistoryHandler pointHistoryHandler;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    private List<PointHistory> pointHistories;

    private static final long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        pointHistories = List.of(PointHistory.builder().id(USER_ID).build());
    }

    @DisplayName("포인트 히스토리 조회 시 List<PointHistory>가 정상 반환된다")
    @Test
    void shouldReturnPointHistoriesForUser() {
        // given
        when(pointHistoryRepository.selectAllByUserId(USER_ID)).thenReturn(pointHistories);

        // when
        List<PointHistory> result = pointHistoryHandler.getPointHistories(USER_ID);

        // then
        assertThat(result).isEqualTo(pointHistories);
        verify(pointHistoryRepository, only()).selectAllByUserId(USER_ID);
        verifyNoMoreInteractions(pointHistoryRepository);
    }

    @DisplayName("포인트 히스토리 추가 시 PointHistoryRepository의 insert()가 호출된다")
    @Test
    void shouldAddPointHistory() {
        // given
        long newPoint = 200;
        TransactionType transactionType = TransactionType.CHARGE;

        // when
        pointHistoryHandler.addPointHistory(USER_ID, newPoint, transactionType);

        // then
        verify(pointHistoryRepository, only()).insert(eq(USER_ID), eq(newPoint), eq(transactionType), anyLong());
        verifyNoMoreInteractions(pointHistoryRepository);
    }
}