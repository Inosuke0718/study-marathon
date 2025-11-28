package com.example.study_marathon.service;

import com.example.study_marathon.dto.UserRegisterForm;
import com.example.study_marathon.entity.Users;
import com.example.study_marathon.repository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerNewUser(UserRegisterForm form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("パスワードと確認用パスワードが一致しません。");
        }

        usersRepository.findByUsername(form.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("そのユーザー名は既に使用されています。");
        });

        Users user = new Users();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setTotalPoints(0);

        usersRepository.save(user);
    }
}
