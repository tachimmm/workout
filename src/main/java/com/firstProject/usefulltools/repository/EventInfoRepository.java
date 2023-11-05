package com.firstProject.usefulltools.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.firstProject.usefulltools.entity.EventInfo;




public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {
    List<EventInfo> findByUsername(String username);

    void deleteByUsername(@Param("username") String username);
}
