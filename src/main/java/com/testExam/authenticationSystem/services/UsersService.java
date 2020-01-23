package com.testExam.authenticationSystem.services;

import com.testExam.authenticationSystem.entities.UserData;
import com.testExam.authenticationSystem.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    public Optional<UserData> findUser(String email) {
        return usersRepository.findByEmail(email);
    }

    public Optional<UserData> findUser(String email, String password) {
        return usersRepository.findByEmailAndPassword(email, password);
    }

    public Optional<UserData> findUserByToken(String token) {
        return usersRepository.findByToken(token);
    }

    public boolean isTokenTaken(String token) {
        return usersRepository.findByToken(token).isPresent();
    }

    public UserData save(UserData userData) {
        return usersRepository.save(userData);
    }
}
