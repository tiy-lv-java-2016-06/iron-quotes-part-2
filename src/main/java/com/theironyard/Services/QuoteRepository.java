package com.theironyard.Services;

import com.theironyard.Entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Nigel on 8/1/16.
 */
public interface QuoteRepository extends JpaRepository<Quote, Integer>{
}
