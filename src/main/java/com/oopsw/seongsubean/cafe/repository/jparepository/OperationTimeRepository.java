package com.oopsw.seongsubean.cafe.repository.jparepository;

import com.oopsw.seongsubean.cafe.domain.OperationTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationTimeRepository extends JpaRepository<OperationTime, Integer> {

  Optional<OperationTime> findByCafeIdAndWeekday(Integer cafeId, String weekday);

  Optional<OperationTime> findByCafeId(Integer cafeId);

  List<OperationTime> findAllByCafeId(Integer cafeId);

  void deleteAllByCafeId(int cafeId);
}
