package com.oopsw.seongsubean.cafe.repository.jparepository;

import com.oopsw.seongsubean.cafe.domain.MenuInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuInfoRepository extends JpaRepository<MenuInfo, Integer> {

  Page<MenuInfo> findByCafeId(Integer cafeId, Pageable pageable);

  MenuInfo findByMenuId(Integer menuId);

}
