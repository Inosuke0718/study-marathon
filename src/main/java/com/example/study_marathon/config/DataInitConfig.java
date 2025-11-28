package com.example.study_marathon.config;

import com.example.study_marathon.entity.StudyLogs;
import com.example.study_marathon.entity.Users;
import com.example.study_marathon.repository.StudyLogsRepository;
import com.example.study_marathon.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitConfig {

    private static final Logger log = LoggerFactory.getLogger(DataInitConfig.class);

    @Bean
    public CommandLineRunner dataInitializer(UsersRepository usersRepository, StudyLogsRepository studyLogsRepository) {
        return args -> {
            if (usersRepository.count() > 0) {
                log.info("Skip data initialization because users already exist.");
                return;
            }

            Users user = new Users();
            user.setUsername("test-user");
            user.setEmail("test@example.com");
            user.setPassword("dummy-password");
            user.setTotalPoints(0);
            usersRepository.save(user);

            StudyLogs log1 = new StudyLogs();
            log1.setUser(user);
            log1.setStudyDate(LocalDate.now());
            log1.setDuration(60);
            log1.setContent("Java学習");
            log1.setCreatedAt(LocalDateTime.now());

            StudyLogs log2 = new StudyLogs();
            log2.setUser(user);
            log2.setStudyDate(LocalDate.now().minusDays(1));
            log2.setDuration(90);
            log2.setContent("Spring Boot学習");
            log2.setCreatedAt(LocalDateTime.now().minusDays(1));

            studyLogsRepository.save(log1);
            studyLogsRepository.save(log2);

            log.info("Inserted test user and study logs.");
        };
    }
}
