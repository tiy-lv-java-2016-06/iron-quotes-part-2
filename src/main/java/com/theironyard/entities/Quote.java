package com.theironyard.entities;

import com.theironyard.utilities.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by EddyJ on 8/1/16.
 */
@Entity
@Table(name = "quotes")
public class Quote {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String author;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column
    private LocalDateTime dateCreated = LocalDateTime.now();

    @ManyToMany
    private Collection<Tag> tags;

    public Quote() {
    }

    public Quote(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getTimeCreated() {
        return dateCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.dateCreated = timeCreated;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public Tag editTag(Integer id, String value) {
        Tag editTag = null;
        List<Tag> tagList = new ArrayList(getTags());
        for (Tag tag : tagList) {
            if (tag.getId() == id) {
                tag.setValue(value);
                editTag = tag;
            }
        }
        this.tags.add(editTag);
        return editTag;
    }

    public void deleteTag(Tag tag) {
        this.tags.remove(tag);
    }
}
