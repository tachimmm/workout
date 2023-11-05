package com.firstProject.usefulltools.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.firstProject.usefulltools.entity.RecodeInfo;

public interface RecodeRepository extends JpaRepository<RecodeInfo, Long> {
        List<RecodeInfo> findByUsername(String username);

        @Query("SELECT r FROM RecodeInfo r WHERE r.username = :username AND r.item = :item AND r.date_column = :date_column")
        List<RecodeInfo> findByUsernameAndItemAndDateColumn(@Param("username") String username,
                        @Param("item") String item,
                        @Param("date_column") String date_column);

        @Query("SELECT r FROM RecodeInfo r WHERE r.username = :username AND r.item = :item ")
        List<RecodeInfo> findByUsernameAndItem(@Param("username") String username, @Param("item") String item);

        @Query("SELECT r FROM RecodeInfo r WHERE r.username = :username  AND r.date_column = :date_column")
        List<RecodeInfo> findByUsernameAndDateColumn(@Param("username") String username,
                        @Param("date_column") String date_column);

        @Query("SELECT r FROM RecodeInfo r ORDER BY r.date_column DESC")
        List<RecodeInfo> findByUsernameAndLastDate(@Param("username") String username);

        void deleteByUsername(@Param("username") String username);
}
