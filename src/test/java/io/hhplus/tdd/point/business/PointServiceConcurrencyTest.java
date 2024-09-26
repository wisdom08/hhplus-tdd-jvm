package io.hhplus.tdd.point.business;


import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointServiceConcurrencyTest {

    private final PointService pointService;
    private final PointHistoryHandler pointHistoryHandler;

    @Autowired
    public PointServiceConcurrencyTest(PointService pointService, PointHistoryHandler pointHistoryHandler) {
        this.pointService = pointService;
        this.pointHistoryHandler = pointHistoryHandler;
    }

    @DisplayName("동시에 100번 충전 요청을 했을 때 정상적으로 모두 충전된다")
    @Test
    void testConcurrentPointCharge() throws InterruptedException, ExecutionException {
        // given
        long userId = 1L;
        int threadCount = 100;

        // when: 쓰레드 100개 * 100원 씩 충전 = 총 만원 충전
        List<CompletableFuture<Void>> futures = IntStream.range(0, threadCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    pointService.chargePoint(userId, 100);
                })).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();


        // then
        UserPoint finalPoint = pointService.getPointBy(userId);
        assertThat(finalPoint.point()).isEqualTo(10000);
        List<PointHistory> pointHistoriesByThreadCount = pointHistoryHandler.getPointHistories(userId);
        assertThat(pointHistoriesByThreadCount).hasSize(threadCount);
    }

    @DisplayName("동시에 100번 사용 요청을 했을 때 정상적으로 모두 충전된다")
    @Test
    void testConcurrentPointUse() {
        // given: 초기 잔고 백만원 충전
        long userId = 2L;
        pointService.chargePoint(userId, 1000000);

        int threadCount = 100;

        // when: 쓰레드 100개 * 10,000씩 사용 = 총 백만원 사용
        List<CompletableFuture<Void>> futures = IntStream.range(0, threadCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    pointService.usePoint(userId, 10000);
                })).toList();

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        // then
        UserPoint finalPoint = pointService.getPointBy(userId);
        assertThat(finalPoint.point()).isEqualTo(0);
        List<PointHistory> pointHistoriesByThreadCount = pointHistoryHandler.getPointHistories(userId);
        assertThat(pointHistoriesByThreadCount).hasSize(threadCount+1); // 스레드 개수(100개) + 초기 백만원 충전 기록
    }

    @DisplayName("동시에 충전 50번 + 사용 50번 요청이 들어올 때 모두 정상적으로 처리된다")
    @Test
    void testConcurrentChargeAndUse() {
        // given: 초기 잔고 백만원 충전
        long userId = 3L;
        pointService.chargePoint(userId, 10000);

        int threadCount = 100;

        // when
        List<CompletableFuture<Void>> futures = IntStream.range(0, threadCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    if (i % 2 == 0) {
                        pointService.chargePoint(userId, 100);
                    } else {
                        pointService.usePoint(userId, 50);
                    }
                })).toList();

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        // then
        UserPoint finalPoint = pointService.getPointBy(userId);
        /**
         * 최종 포인트 계산
         * 충전: 50 * 100 = 5000
         * 사용: 50 * 50 = 2500
         * 10000(초기 잔고) + 5000(충전) - 2500(사용) = 7500
        **/
        assertThat(finalPoint.point()).isEqualTo(10000 + 5000 - 2500);

        List<PointHistory> pointHistoriesByThreadCount = pointHistoryHandler.getPointHistories(userId);
        assertThat(pointHistoriesByThreadCount).hasSize(threadCount + 1); // 초기 백만원 충전 기록 포함
        assertThat(pointHistoriesByThreadCount.stream().filter(v -> TransactionType.CHARGE.equals(v.type())).count()).isEqualTo(threadCount/2 + 1); // 초기 충전 1 더해야 함
        assertThat(pointHistoriesByThreadCount.stream().filter(v -> TransactionType.USE.equals(v.type())).count()).isEqualTo(threadCount/2);
    }
}
