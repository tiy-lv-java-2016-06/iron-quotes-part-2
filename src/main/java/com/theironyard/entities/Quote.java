package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.theironyard.utilities.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Created by vasantia on 8/1/16.
 */

@Entity
@Table(name = "quotes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Quote {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String quote;

    @Column
    private String author;

    @Column
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dateCreated = LocalDateTime.now();

    @ManyToMany
    Collection<Tag> tags;

    public Quote() {
    }

    public Quote(String quote, String author) {
        this.quote = quote;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void addTag(Tag tag){
        this.tags.add(tag);
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }
}
