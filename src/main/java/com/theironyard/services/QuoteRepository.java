package com.theironyard.services;

import com.theironyard.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by vasantia on 8/1/16.
 */
public interface QuoteRepository extends JpaRepository<Quote, Integer> {

}
