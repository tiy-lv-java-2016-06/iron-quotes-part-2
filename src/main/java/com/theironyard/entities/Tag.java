package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theironyard.utilities.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Created by EddyJ on 8/1/16.
 */
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String value;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column
    private LocalDateTime dateAdded = LocalDateTime.now();

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Collection<Quote> quotes;

    public Tag() {
    }

    public Tag(String value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Collection<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(Collection<Quote> quotes) {
        this.quotes = quotes;
    }
}
