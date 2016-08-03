package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theironyard.utilities.LocalDateTimeConverter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by EddyJ on 8/1/16.
 */
@Entity
@Table(name = "users")
public class User {

    public static final int TOKEN_EXPIRATION = 30;

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    @ColumnDefault("'ABCDEFGHIJKLMNO'")
    @JsonIgnore
    private String token;

    @Column(nullable = false)
    @ColumnDefault("'2000-12-12'")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime expiration;

    public User() {
        setTokenAndExiperation();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        setTokenAndExiperation();
    }

    public String generateToken(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public void setTokenAndExiperation(){
        token = generateToken();
        expiration = LocalDateTime.now().plus(TOKEN_EXPIRATION, ChronoUnit.DAYS);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public String regenerate(){
        setTokenAndExiperation();
        return token;
    }
    public boolean isTokenValid(){
        return expiration.isAfter(LocalDateTime.now());
    }
}
