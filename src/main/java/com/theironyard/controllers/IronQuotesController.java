package com.theironyard.controllers;

import com.theironyard.command.QuoteCommand;
import com.theironyard.command.TagCommand;
import com.theironyard.command.UserCommand;
import com.theironyard.entities.Quote;
import com.theironyard.entities.Tag;
import com.theironyard.entities.User;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.NotLoggedIn;
import com.theironyard.exceptions.TokenExpiredException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.services.QuoteRepository;
import com.theironyard.services.TagRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vasantia on 8/1/16.
 */

@RestController
public class IronQuotesController {

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public User createUser(@RequestBody UserCommand command)
            throws PasswordStorage.CannotPerformOperationException, PasswordStorage.InvalidHashException {

        User user = userRepository.findFirstByName(command.getUsername());
        if(user == null){
            user = new User(command.getUsername(), PasswordStorage.createHash(command.getPassword()));
            userRepository.save(user);
        }
        else if(!PasswordStorage.verifyPassword(command.getPassword(), user.getPassword())){
            throw new LoginFailedException();
        }
        return user;
    }

    public User checkLogin(UserCommand command) throws Exception{
        User user = userRepository.findFirstByName(command.getUsername());
        if(user == null){
            throw new UserNotFoundException();
        }

        if(!PasswordStorage.verifyPassword(command.getPassword(), user.getPassword())){
            throw new LoginFailedException();
        }

        if(!user.isTokenValid()){
            throw new TokenExpiredException();
        }

        return user;
    }

    @RequestMapping(path = "/token", method = RequestMethod.POST)
    public Map getToken(@RequestBody UserCommand command) throws Exception {
        User user = checkLogin(command);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", user.getToken());
        return tokenMap;
    }

    @RequestMapping(path = "/token/regenerate", method = RequestMethod.PUT)
    public Map regenerateToken(@RequestBody UserCommand command) throws Exception{
        User user = checkLogin(command);
        String token = user.regenerate();
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return tokenMap;
    }

    public User getUserFromAuth(String auth){
        User user = userRepository.findFirstByToken(auth.split(" ")[1]);
        if(!user.isTokenValid()){
            throw new TokenExpiredException();
        }
        return user;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public Quote create(@RequestBody Quote quote, @RequestHeader(value = "Authorization") String auth) throws IOException {

        User user = getUserFromAuth(auth);
        quoteRepository.save(quote);
        return quote;
    }

    @RequestMapping(path = "/quotes/{id}/tags", method = RequestMethod.POST)
    public Quote addTag(@PathVariable int id, @RequestBody TagCommand command) throws Exception {
        Quote quote = quoteRepository.getOne(id);
        if(quote == null){
            throw new Exception("Quote doesn't exist");
        }

        Tag tag = tagRepository.findFirstByValue(command.getValue());
        if(tag == null){
            tag = new Tag(command.getValue());
            tagRepository.save(tag);
        }

        quote.addTag(tag);
        quoteRepository.save(quote);

        return quote;
    }

    @RequestMapping(path = "/quotes", method = RequestMethod.GET)
    public List<Quote> showQuotes(){

        return quoteRepository.findAll();
    }

    @RequestMapping(path = "/tags", method = RequestMethod.GET)
    public List<Tag> showTags(){

        return tagRepository.findAll();
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> showUsers(){

        return userRepository.findAll();
    }

    @RequestMapping(path = "/update/quotes/{id}", method = RequestMethod.PUT)
    public Quote updateQuote(@PathVariable int id, @RequestBody QuoteCommand command) throws Exception {

        Quote updateQuote = quoteRepository.getOne(id);
        if(updateQuote == null){
            throw new Exception("Quote doesn't exist");
        }

        updateQuote.setQuote(command.getQuote());
        updateQuote.setAuthor(command.getAuthor());
        quoteRepository.save(updateQuote);
        return updateQuote;
    }

    @RequestMapping(path = "/update/tags/{id}", method = RequestMethod.PUT)
    public Tag updateTag(@PathVariable int id, @RequestBody TagCommand command) throws Exception {

        Tag updateTag = tagRepository.getOne(id);
        if(updateTag == null){
            throw new Exception("Tag doesn't exist");
        }

        updateTag.setValue(command.getValue());
        tagRepository.save(updateTag);
        return updateTag;
    }

    @RequestMapping(path = "/delete/quotes/{id}", method = RequestMethod.DELETE)
    public void deleteQuote(@PathVariable int id) throws Exception {

        Quote quote = quoteRepository.getOne(id);
        if(quote == null){
            throw new Exception("Quote doesn't exist");
        }

        quoteRepository.delete(id);
    }

    @RequestMapping(path = "/delete/tags/{id}", method = RequestMethod.DELETE)
    public void deleteTags(@PathVariable int id) throws Exception {

        Tag tag = tagRepository.getOne(id);
        if(tag == null){
            throw new Exception("Tag doesn't exist");
        }

        tagRepository.delete(id);
    }

}
