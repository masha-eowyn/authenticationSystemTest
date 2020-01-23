package com.testExam.authenticationSystem.controllers;

import com.testExam.authenticationSystem.entities.UserData;
import com.testExam.authenticationSystem.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="users/")
public class UsersController {

    private static final int TOKEN_LENGTH = 255;
    private static final int RANDOM_TOKEN_RETRIES = 5;

    private final UsersService usersService;

    @PostMapping(path = "register/email/{email}/password/{password}", produces = "application/json")
    public ResponseEntity<String> registerUser(@PathVariable(value="email") String email,
                                               @PathVariable(value="password") String password) {
        if (usersService.findUser(email).isPresent())
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);

        try {
            usersService.save(new UserData(email, password));
            return ResponseEntity.ok("User registered");

        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to register user: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "login/email/{email}/password/{password}", produces = "application/json")
    public ResponseEntity<String> loginUser(@PathVariable(value="email") String email,
                                       @PathVariable(value="password") String password) {
        Optional<UserData> optionalUserData = usersService.findUser(email, password);

        if (optionalUserData.isEmpty())
            return new ResponseEntity<>("User wasn't found", HttpStatus.BAD_REQUEST);

        try {
            UserData userData = optionalUserData.get();
            String token = getNewToken(RANDOM_TOKEN_RETRIES);

            userData.setToken(token);
            usersService.save(userData);

            return ResponseEntity.ok(token);

        } catch (Exception ex) {
            return new  ResponseEntity<>("Failed to login: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "data/token/{token}", produces = "application/json")
    public ResponseEntity<String> getData(@PathVariable(value="token") String token) {
        Optional<UserData> optionalUserData = usersService.findUserByToken(token);

        if (optionalUserData.isEmpty())
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);

        UserData userData = optionalUserData.get();
        return ResponseEntity.ok(userData.getEmail());
    }

    private String getNewToken(int retries) throws Exception {
        String token = RandomStringUtils.random(TOKEN_LENGTH, true, true);

        while (usersService.isTokenTaken(token) && retries > 0) {
            token = RandomStringUtils.random(TOKEN_LENGTH, true, true);
            retries--;
        }

        if (usersService.isTokenTaken(token))
            throw new Exception("Couldn't get random token after " + retries + " retries");

        return token;
    }
}
