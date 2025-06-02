package com.oopsw.seongsubean.cafe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.oopsw.seongsubean.cafe.domain.OperationTime;
import com.oopsw.seongsubean.cafe.repository.jparepository.OperationTimeRepository;
import com.oopsw.seongsubean.config.PropertyConfig;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PropertyConfig.class)
@Transactional
public class OperationTimeRepositoryTest {

  @Autowired
  private OperationTimeRepository operationTimeRepository;

  @Test
  public void addOperationTimeTest() {
    OperationTime operationTime = OperationTime.builder()
        .weekday("월")
        .openTime(LocalTime.of(9, 0))
        .closeTime(LocalTime.of(21, 0))
        .cafeId(9).build();

    OperationTime newOperationTime = operationTimeRepository.save(operationTime);

    assertThat(newOperationTime).isNotNull();
    assertThat(newOperationTime).isEqualTo(operationTime);
  }

  @Test
  public void findByCafeIdAndWeekdayTest() {
    Optional<OperationTime> operationTime = operationTimeRepository.findByCafeIdAndWeekday(3,
        "월요일");

    assertThat(operationTime).isPresent();
    assertThat(operationTime.get().getCafeId()).isEqualTo(3);
    assertThat(operationTime.get().getWeekday()).isEqualTo("월요일");
  }

  @Test
  public void findByCafeIdTest() {
    List<OperationTime> operationTimeList = operationTimeRepository.findAllByCafeId(3);

    assertThat(operationTimeList).isNotNull();
    assertThat(operationTimeList.size()).isEqualTo(6);
  }

  @Test
  public void removeByOperationTimeIdTest() {
    operationTimeRepository.deleteAllByCafeId(3);

    List<OperationTime> operationTimeList = operationTimeRepository.findAllByCafeId(3);

    assertThat(operationTimeList.size()).isEqualTo(0);
  }

  @Test
  public void setOperationTimeTest() {
    OperationTime existingOperationTime = operationTimeRepository.findById(40)
        .orElseThrow(() -> new RuntimeException("운영시간 없음"));

    existingOperationTime.setOpenTime(LocalTime.of(7, 0));
    existingOperationTime.setCloseTime(LocalTime.of(23, 0));

    OperationTime operationTime = operationTimeRepository.save(existingOperationTime);

    assertThat(operationTime).isNotNull();
    assertThat(operationTime).isEqualTo(existingOperationTime);
  }
}
