package com.testExam.authenticationSystem.entities;

import com.testExam.authenticationSystem.utilities.CryptoConverter;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"token"})
@Table(name="users_data")
public class UserData {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "user_id", updatable = false, nullable = false)
    private long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    @Convert(converter = CryptoConverter.class)
    private String password;

    @Column(name = "token", unique = true)
    private String token;

    public UserData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
