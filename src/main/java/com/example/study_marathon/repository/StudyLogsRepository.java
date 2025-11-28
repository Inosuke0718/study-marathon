package com.example.study_marathon.repository;

import com.example.study_marathon.entity.StudyLogs;
import com.example.study_marathon.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudyLogsRepository extends JpaRepository<StudyLogs, Long> {

    List<StudyLogs> findByUserOrderByStudyDateDesc(Users user);

    List<StudyLogs> findByUserAndStudyDateBetweenOrderByStudyDateAsc(Users user, LocalDate start, LocalDate end);

    List<StudyLogs> findTop20ByUserOrderByStudyDateDesc(Users user);
}
