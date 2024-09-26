package io.hhplus.tdd.point.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

class PointHistoryTest {


    @DisplayName("포인트 히스토리가 없으면 IllegalArgumentException이 발생한다")
    @Test
    void throwExceptionWhenNoPointHistory() {
        // given
        // when
        // then
        assertThatThrownBy(() -> PointHistory.isPointHistory(List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("포인트 히스토리가 있으면 IllegalArgumentException이 발생하지 않는다")
    @Test
    void noExceptionWhenPointToChargeIsGreaterThan1() {
        // given
        List<PointHistory> pointHistories = List.of(PointHistory.builder().build());
        // when
        // then
        assertThat(pointHistories).hasSize(1);
        assertThatNoException().isThrownBy(() -> PointHistory.isPointHistory(pointHistories));
    }
}