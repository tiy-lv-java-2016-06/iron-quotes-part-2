package com.theironyard.services;

import com.theironyard.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by EddyJ on 8/1/16.
 */
public interface QuoteRepository extends JpaRepository <Quote, Integer> {
}
