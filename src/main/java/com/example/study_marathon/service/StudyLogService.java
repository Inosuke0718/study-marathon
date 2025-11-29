package com.example.study_marathon.service;

import com.example.study_marathon.dto.StudyLogForm;
import com.example.study_marathon.dto.WeeklyRankingDto;
import com.example.study_marathon.entity.StudyLogs;
import com.example.study_marathon.entity.Users;
import com.example.study_marathon.repository.StudyLogsRepository;
import com.example.study_marathon.repository.UsersRepository;
import com.example.study_marathon.util.SecurityUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudyLogService {

    private final StudyLogsRepository studyLogsRepository;
    private final UsersRepository usersRepository;

    public StudyLogService(StudyLogsRepository studyLogsRepository, UsersRepository usersRepository) {
        this.studyLogsRepository = studyLogsRepository;
        this.usersRepository = usersRepository;
    }

    private Users getCurrentUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        return usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found: id=" + userId));
    }

    @Transactional(readOnly = true)
    public List<StudyLogs> getLatestLogs() {
        Users user = getCurrentUser();
        return studyLogsRepository.findTop20ByUserOrderByStudyDateDesc(user);
    }

    @Transactional(readOnly = true)
    public int getWeeklyTotalMinutesForCurrentUser() {
        Users user = getCurrentUser();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        List<StudyLogs> weeklyLogs = studyLogsRepository
                .findByUserAndStudyDateBetweenOrderByStudyDateAsc(user, startOfWeek, today);

        return weeklyLogs.stream()
                .mapToInt(StudyLogs::getDuration)
                .sum();
    }

    @Transactional(readOnly = true)
    public List<WeeklyRankingDto> getWeeklyRankingForCurrentWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        Pageable topTen = PageRequest.of(0, 10);

        return studyLogsRepository.findWeeklyRanking(startOfWeek, today, topTen);
    }

    @Transactional
    public void createLog(StudyLogForm form) {
        Users user = getCurrentUser();

        StudyLogs log = new StudyLogs();
        log.setUser(user);
        log.setStudyDate(form.getStudyDate());
        log.setDuration(form.getDuration());
        log.setContent(form.getContent());
        log.setCreatedAt(LocalDateTime.now());

        studyLogsRepository.save(log);
    }

    @Transactional
    public void deleteLog(Long id) {
        Users currentUser = getCurrentUser();

        Optional<StudyLogs> optionalLog = studyLogsRepository.findById(id);
        StudyLogs log = optionalLog.orElseThrow(() -> new IllegalArgumentException("StudyLog not found: id=" + id));

        if (!log.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Cannot delete other user's study log");
        }

        studyLogsRepository.delete(log);
    }
}
