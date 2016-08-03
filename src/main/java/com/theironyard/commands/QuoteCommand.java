package com.theironyard.commands;

/**
 * Created by EddyJ on 8/2/16.
 */
public class QuoteCommand {

    private String text;

    private String author;

    public QuoteCommand() {
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
}
