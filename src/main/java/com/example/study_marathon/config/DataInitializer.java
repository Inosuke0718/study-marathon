package com.example.study_marathon.config;

import com.example.study_marathon.entity.StudyLogs;
import com.example.study_marathon.entity.Users;
import com.example.study_marathon.repository.StudyLogsRepository;
import com.example.study_marathon.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component("phase6DataInitializer")
@Profile("!prod")
public class DataInitializer implements CommandLineRunner {

    private final UsersRepository usersRepository;
    private final StudyLogsRepository studyLogsRepository;

    public DataInitializer(UsersRepository usersRepository, StudyLogsRepository studyLogsRepository) {
        this.usersRepository = usersRepository;
        this.studyLogsRepository = studyLogsRepository;
    }

    @Override
    public void run(String... args) {
        // すでにダミーユーザーがいる場合は何もしない
        if (usersRepository.findByUsername("user1").isPresent()) {
            return;
        }

        Users user1 = createUser("user1", "user1@example.com");
        Users user2 = createUser("user2", "user2@example.com");
        Users user3 = createUser("user3", "user3@example.com");

        usersRepository.saveAll(List.of(user1, user2, user3));

        LocalDate today = LocalDate.now();
        // サービス側(getWeeklyTotalMinutesForCurrentUser / getWeeklyRankingForCurrentWeek)と同じく
        // 「今週の月曜 〜 今日」になるように開始日を計算
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);

        // user1: 今週多めに勉強
        for (int i = 0; i < 10; i++) {
            LocalDate date = startOfWeek.plusDays(i % 7);
            createStudyLog(user1, date, 60 + (i * 5), "ダミー学習(user1)-" + i);
        }

        // user2: 中くらい
        for (int i = 0; i < 10; i++) {
            LocalDate date = startOfWeek.plusDays(i % 7);
            createStudyLog(user2, date, 30 + (i * 3), "ダミー学習(user2)-" + i);
        }

        // user3: 少なめ (今週内にのみ配置)
        for (int i = 0; i < 10; i++) {
            LocalDate date = startOfWeek.plusDays(i % 7);
            createStudyLog(user3, date, 20 + (i * 2), "ダミー学習(user3)-" + i);
        }
    }

    private Users createUser(String username, String email) {
        Users user = new Users();
        user.setUsername(username);
        // パスワードは後で本登録する前提なので仮の値
        user.setPassword("password");
        user.setEmail(email);
        user.setTotalPoints(0);
        return user;
    }

    private void createStudyLog(Users user, LocalDate date, int minutes, String content) {
        StudyLogs log = new StudyLogs();
        log.setUser(user);
        log.setStudyDate(date);
        log.setDuration(minutes);
        log.setContent(content);
        log.setCreatedAt(LocalDateTime.now());
        studyLogsRepository.save(log);
    }
}
