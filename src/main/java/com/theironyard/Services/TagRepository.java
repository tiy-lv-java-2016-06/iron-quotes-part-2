package com.theironyard.Services;

import com.theironyard.Entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Nigel on 8/1/16.
 */
public interface TagRepository extends JpaRepository<Tag, Integer>{
    Tag findFirstByValue(String value);
}
