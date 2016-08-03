package com.theironyard.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theironyard.utilities.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Created by Nigel on 8/1/16.
 */
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String value;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Collection<Quote> quotes;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(nullable = true)
    private LocalDateTime added_at = LocalDateTime.now();

    public Tag() {
    }

    public Tag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Collection<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(Collection<Quote> quotes) {
        this.quotes = quotes;
    }

    public LocalDateTime getAdded_at() {
        return added_at;
    }

    public void setAdded_at(LocalDateTime added_at) {
        this.added_at = added_at;
    }

    public int getId() {
        return id;
    }
}
