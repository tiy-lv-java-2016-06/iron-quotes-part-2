package com.theironyard.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.theironyard.utilities.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Nigel on 8/1/16.
 */
@Entity
@Table(name = "quotes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Quote {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String quote;

    @ManyToOne
    private User author;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(nullable = true)
    private LocalDateTime created_at = LocalDateTime.now();

    @ManyToMany
    private Collection<Tag> tags;

    public Quote() {
    }

    public Quote(String quote) {
        this.quote = quote;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag){
        this.tags.add(tag);
    }

    public Tag editTag(int id, String value){
        Tag tag = null;
        List<Tag> tagList = new ArrayList(getTags());
        for (Tag searchTag : tagList){
            if (searchTag.getId() == id){
                searchTag.setValue(value);
                tag = searchTag;
            }
        }
        if (tag !=null) {
            this.tags.add(tag);
        }
        return tag;
    }

    public void deleteTag(Tag tag){
        this.tags.remove(tag);
    }

    public int getId() {
        return id;
    }
}
