package com.example.study_marathon.service;

import com.example.study_marathon.dto.StudyLogForm;
import com.example.study_marathon.entity.StudyLogs;
import com.example.study_marathon.entity.Users;
import com.example.study_marathon.repository.StudyLogsRepository;
import com.example.study_marathon.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudyLogService {

    private final StudyLogsRepository studyLogsRepository;
    private final UsersRepository usersRepository;

    public StudyLogService(StudyLogsRepository studyLogsRepository, UsersRepository usersRepository) {
        this.studyLogsRepository = studyLogsRepository;
        this.usersRepository = usersRepository;
    }

    // Phase2では仮ユーザーとして固定メールアドレスのユーザーを利用する
    private Users getFixedUser() {
        return usersRepository.findByEmail("test@example.com")
                .orElseThrow(() -> new IllegalStateException("test user not found"));
    }

    @Transactional(readOnly = true)
    public List<StudyLogs> getLatestLogs() {
        Users user = getFixedUser();
        return studyLogsRepository.findTop20ByUserOrderByStudyDateDesc(user);
    }

    @Transactional
    public void createLog(StudyLogForm form) {
        Users user = getFixedUser();

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
        studyLogsRepository.deleteById(id);
    }
}
