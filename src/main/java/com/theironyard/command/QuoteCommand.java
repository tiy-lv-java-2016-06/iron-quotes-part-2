package com.theironyard.command;

/**
 * Created by vasantia on 8/2/16.
 */
public class QuoteCommand {

    private String quote;

    private String author;

    public QuoteCommand() {
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
}
