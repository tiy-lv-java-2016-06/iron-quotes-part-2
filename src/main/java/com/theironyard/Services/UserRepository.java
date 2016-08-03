package com.theironyard.Services;

import com.theironyard.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Nigel on 8/1/16.
 */
public interface UserRepository extends JpaRepository<User, Integer>{
    User findFirstByName(String name);
    User findFirstByToken(String token);
}
