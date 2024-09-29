package io.hhplus.tdd.point.infrastructure;

import io.hhplus.tdd.point.domain.UserPoint;

public interface UserPointRepository {
    UserPoint selectById(Long id);

    UserPoint insertOrUpdate(long id, long amount);
}
