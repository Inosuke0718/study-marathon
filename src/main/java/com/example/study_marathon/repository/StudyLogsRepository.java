package com.example.study_marathon.repository;

import com.example.study_marathon.dto.WeeklyRankingDto;
import com.example.study_marathon.entity.StudyLogs;
import com.example.study_marathon.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StudyLogsRepository extends JpaRepository<StudyLogs, Long> {

    List<StudyLogs> findByUserOrderByStudyDateDesc(Users user);

    List<StudyLogs> findByUserAndStudyDateBetweenOrderByStudyDateAsc(Users user, LocalDate start, LocalDate end);

    List<StudyLogs> findTop20ByUserOrderByStudyDateDesc(Users user);

    @Query("SELECT new com.example.study_marathon.dto.WeeklyRankingDto(u.username, SUM(l.duration)) " +
            "FROM StudyLogs l JOIN l.user u " +
            "WHERE l.studyDate BETWEEN :start AND :end " +
            "GROUP BY u.id, u.username " +
            "ORDER BY SUM(l.duration) DESC")
    List<WeeklyRankingDto> findWeeklyRanking(@Param("start") LocalDate start,
                                            @Param("end") LocalDate end,
                                            Pageable pageable);
}
