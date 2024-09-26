package io.hhplus.tdd.point.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

class UserPointTest {

    @DisplayName("충전 포인트 검증")
    @Nested
    class chargePoint {

        @DisplayName("충전하려는 포인트가 0 이하라면 IllegalArgumentException이 발생한다")
        @ValueSource(longs = {0, -1, -2, -3})
        @ParameterizedTest
        void throwExceptionWhenPointToChargeIsLessThanZero(long pointToCharge) {
            // given
            UserPoint userPoint = UserPoint.builder().id(1).point(pointToCharge).build();
            // when
            // then
            assertThatThrownBy(() -> userPoint.chargePoint(pointToCharge))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("충전하려는 포인트가 1 이상이라면 IllegalArgumentException이 발생하지 않는다")
        @ValueSource(longs = {1, 100, 1000})
        @ParameterizedTest
        void noExceptionWhenPointToChargeIsGreaterThan1(long pointToCharge) {
            // given
            UserPoint userPoint = UserPoint.builder().id(1).point(pointToCharge).build();
            // when
            // then
            assertThatNoException().isThrownBy(() -> userPoint.chargePoint(pointToCharge));
        }

        @DisplayName("충전 포인트가 백만원 초과라면 IllegalArgumentException이 발생한다")
        @ValueSource(longs = {1000001, 90000000, 100000000})
        @ParameterizedTest
        void throwExceptionWhenPointToChargeIsGreaterThan1000000(long pointToCharge) {
            // given
            UserPoint userPoint = UserPoint.builder().id(1).point(pointToCharge).build();
            // when
            // then
            assertThatThrownBy(() -> userPoint.chargePoint(pointToCharge))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("충전 포인트가 백만원 이하라면  IllegalArgumentException이 발생하지 않는다")
        @ValueSource(longs = {1000000, 999999, 50000})
        @ParameterizedTest
        void noExceptionWhenPointToChargeIsLessThan1000000(long pointToCharge) {
            // given
            UserPoint userPoint = UserPoint.builder().id(1).point(pointToCharge).build();
            // when
            // then
            assertThatNoException().isThrownBy(() -> userPoint.chargePoint(pointToCharge));
        }
    }

    @DisplayName("사용 포인트 검증")
    @Nested
    class usePoint {

        @DisplayName("사용 포인트가 현재 포인트인 잔고를 초과하면 IllegalArgumentException이 발생한다")
        @ValueSource(longs = {101, 999, 100000})
        @ParameterizedTest
        void throwExceptionWhenPointToUseIsGreaterThanBalance(long pointToUse) {
            // given
            long balance = 100;
            UserPoint userPoint = UserPoint.builder().id(1).point(balance).build();
            // when
            // then
            assertThatThrownBy(() -> userPoint.usePoint(pointToUse))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("사용 포인트가 현재 포인트인 잔고 이하라면 IllegalArgumentException이 발생하지 않는다")
        @ValueSource(longs = {100, 50, 1})
        @ParameterizedTest
        void noExceptionWhenPointToUseIsLessThanBalance(long pointToUse) {
            // given
            long balance = 100;
            UserPoint userPoint = UserPoint.builder().id(1).point(balance).build();
            // when
            // then
            assertThatNoException().isThrownBy(() -> userPoint.usePoint(pointToUse));
        }

        @DisplayName("사용 포인트가 0 이하라면 IllegalArgumentException이 발생한다")
        @ValueSource(longs = {0, -1, -2, -3})
        @ParameterizedTest
        void throwExceptionWhenPointToChargeIsLessThanZero(long pointToUse) {
            // given
            UserPoint userPoint = UserPoint.builder().id(1).point(pointToUse).build();
            // when
            // then
            assertThatThrownBy(() -> userPoint.usePoint(pointToUse))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("사용 포인트가 1 이상이라면  IllegalArgumentException이 발생하지 않는다")
        @ValueSource(longs = {1, 10, 100})
        @ParameterizedTest
        void noExceptionWhenPointToChargeIsLessThan1000000(long pointToUse) {
            // given
            UserPoint userPoint = UserPoint.builder().id(1).point(pointToUse).build();
            // when
            // then
            assertThatNoException().isThrownBy(() -> userPoint.usePoint(pointToUse));
        }
    }
}