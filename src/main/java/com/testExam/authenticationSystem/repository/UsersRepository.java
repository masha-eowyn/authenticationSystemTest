package com.testExam.authenticationSystem.repository;

import com.testExam.authenticationSystem.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<UserData, UUID> {
    Optional<UserData> findByEmail(String email);
    Optional<UserData> findByEmailAndPassword(String email, String password);
    Optional<UserData> findByToken(String token);
}
