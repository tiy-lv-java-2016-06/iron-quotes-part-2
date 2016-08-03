package com.theironyard.services;

import com.theironyard.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by EddyJ on 8/1/16.
 */
public interface TagRepository extends JpaRepository <Tag, Integer> {
    Tag findFirstByValue(String value);
}
