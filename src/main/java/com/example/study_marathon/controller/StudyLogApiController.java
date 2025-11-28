package com.example.study_marathon.controller;

import com.example.study_marathon.entity.StudyLogs;
import com.example.study_marathon.entity.Users;
import com.example.study_marathon.repository.StudyLogsRepository;
import com.example.study_marathon.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/study-logs")
public class StudyLogApiController {

    private static final Logger log = LoggerFactory.getLogger(StudyLogApiController.class);

    private final UsersRepository usersRepository;
    private final StudyLogsRepository studyLogsRepository;

    public StudyLogApiController(UsersRepository usersRepository, StudyLogsRepository studyLogsRepository) {
        this.usersRepository = usersRepository;
        this.studyLogsRepository = studyLogsRepository;
    }

    @GetMapping
    public List<StudyLogs> getAllLogsOfTestUser() {
        Users user = usersRepository.findByEmail("test@example.com")
                .orElseThrow(() -> new IllegalStateException("test user not found"));

        List<StudyLogs> logs = studyLogsRepository.findByUserOrderByStudyDateDesc(user);
        log.info("Fetched {} study logs for test user.", logs.size());
        return logs;
    }
}
