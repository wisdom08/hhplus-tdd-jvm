package io.hhplus.tdd.point.presentation;

import io.hhplus.tdd.point.business.PointService;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{userId}")
    public UserPoint point(@PathVariable("userId") long userId) {
        log.info("포인트 조회 요청 id: {}", userId);
        return pointService.getPointBy(userId);
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{userId}/histories")
    public List<PointHistory> history(@PathVariable("userId") long userId) {
        log.info("포인트 충전/이용 내역 조회 요청 id: {}", userId);
        return pointService.getPointHistoryBy(userId);
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{userId}/charge")
    public UserPoint charge(@PathVariable("userId") long userId, @RequestBody long amount) {
        log.info("포인트 충천 요청 id: {}, amount: {}", userId, amount);
        return pointService.chargePoint(userId, amount);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{userId}/use")
    public UserPoint use(@PathVariable("userId") long userId, @RequestBody long pointToCharge) {
        log.info("포인트 사용 요청 id: {}, amount: {}", userId, pointToCharge);
        return pointService.usePoint(userId, pointToCharge);
    }
}
