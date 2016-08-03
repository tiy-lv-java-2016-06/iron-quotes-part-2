package com.theironyard.Controllers;

import com.theironyard.Command.QuoteCommand;
import com.theironyard.Command.TagCommand;
import com.theironyard.Command.UserCommand;
import com.theironyard.Entities.Quote;
import com.theironyard.Entities.Tag;
import com.theironyard.Entities.User;
import com.theironyard.Services.QuoteRepository;
import com.theironyard.Services.TagRepository;
import com.theironyard.Services.UserRepository;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.TokenExpiredException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Nigel on 8/1/16.
 */

@RestController
public class IronQuotesController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    TagRepository tagRepository;

    //*********CREATE-PATHS*********//
    @RequestMapping(path = "/quotes", method = RequestMethod.POST)
    public Quote createQuote(@RequestBody Quote quote,
                             @RequestHeader(value = "Authorization") String auth) {

        User user = getUserFromAuth(auth);

        quote.setAuthor(user);

        quoteRepository.save(quote);

        return quote;

    }

    @RequestMapping(path = "/quotes/{id}/tags", method = RequestMethod.POST)
    public Quote createTag(@PathVariable int id, @RequestBody TagCommand command,
                           @RequestHeader(value = "Authorization") String auth) throws Exception {

        User user = getUserFromAuth(auth);
        Quote quote = quoteRepository.getOne(id);

        if (quote == null) {
            throw new Exception("Quote doesn't exist");
        }

        Tag tag = tagRepository.findFirstByValue(command.getValue());
        if (tag == null) {
            tag = new Tag(command.getValue());
            tagRepository.save(tag);
        }

        quote.addTag(tag);
        quoteRepository.save(quote);
        return quote;

    }

    //*********READ-PATHS*********//
    @RequestMapping(path = "/quotes/{id}", method = RequestMethod.GET)
    public Quote getQuote(@PathVariable int id) throws Exception {

        Quote quote = quoteRepository.getOne(id);

        if (quote == null) {
            throw new Exception("Quote doesn't exist");
        }

        return quote;
    }

    @RequestMapping(path = "/tags/{id}", method = RequestMethod.GET)
    public Tag getTag(@PathVariable int id) throws Exception {

        Tag tag = tagRepository.getOne(id);

        if (tag == null) {
            throw new Exception("Tag doesn't exit");
        }

        return tag;
    }

    //*********UPDATE-PATHS*********//
    @RequestMapping(path = "/quotes/{id}", method = RequestMethod.PUT)
    public Quote updateQuote(@PathVariable int id, @RequestBody QuoteCommand command, @RequestHeader(value = "Authorization") String auth) throws Exception {

        User user = getUserFromAuth(auth);
        Quote quote = quoteRepository.getOne(id);

        if (quote == null) {
            throw new Exception("Quote doesn't exist");
        }

        quote.setQuote(command.getQuote());

        quoteRepository.save(quote);

        return quote;
    }

    @RequestMapping(path = "/quotes/{quoteId}/tags/{tagId}", method = RequestMethod.PUT)
    public Tag updateTag(@PathVariable int quoteId, @PathVariable int tagId, @RequestBody TagCommand command,
                         @RequestHeader(value = "Authorization") String auth) throws Exception {

        User user = getUserFromAuth(auth);
        Quote quote = quoteRepository.getOne(quoteId);

        if (quote == null) {
            throw new Exception("Quote doesn't exist");
        }

        Tag tag = quote.editTag(tagId, command.getValue());
        if (tag == null) {
            throw new Exception("There is no such tag for this Quote");
        }

        return tag;
    }

    //*********DELETE-PATHS*********//
    @RequestMapping(path = "/quotes/{id}", method = RequestMethod.DELETE)
    public List<Quote> deleteQuote(@PathVariable int id, @RequestHeader(value = "Authorization") String auth) throws Exception {

        User user = getUserFromAuth(auth);
        Quote quote = quoteRepository.getOne(id);

        if (quote == null) {
            throw new Exception("Quote doesn't exist");
        } else {
            quoteRepository.delete(quote);
        }
        return quoteRepository.findAll();
    }

    @RequestMapping(path = "/quotes/{quoteId}/tags/{tagId}", method = RequestMethod.DELETE)
    public Quote deleteTag(@PathVariable int tagId, @PathVariable int quoteId,
                           @RequestHeader(value = "Authorization") String auth) throws Exception {

        User user = getUserFromAuth(auth);

        Tag tag = tagRepository.getOne(tagId);
        Quote quote = quoteRepository.getOne(quoteId);
        if (tag == null) {
            throw new Exception("Tag doesn't exist");
        } else {
            quote.deleteTag(tag);
            quoteRepository.save(quote);
        }

        return quote;
    }


    //*********LIST-PATHS*********//
    @RequestMapping(path = "/quotes", method = RequestMethod.GET)
    public List<Quote> getQuotes() {
        return quoteRepository.findAll();
    }

    @RequestMapping(path = "/tags", method = RequestMethod.GET)
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }


    //*********USER-PATHS & METHODS*********//

    @RequestMapping(path = "/token", method = RequestMethod.POST)
    public Map getToken(@RequestBody UserCommand command) throws Exception {
        User user = checkUser(command);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", user.getToken());
        return tokenMap;
    }

    @RequestMapping(path = "/token/regenerate", method = RequestMethod.PUT)
    public Map regenerateToken(@RequestBody UserCommand command) throws Exception {
        User user = checkUser(command);
        String token = user.regenerate();
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return tokenMap;
    }

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public User login(@RequestBody UserCommand command) throws Exception {
        User user = userRepository.findFirstByName(command.getName());

        if (user == null) {
            user = new User(command.getName(), PasswordStorage.createHash(command.getPassword()));
            userRepository.save(user);
        } else if (!PasswordStorage.verifyPassword(command.getPassword(), user.getPassword())) {
            throw new LoginFailedException();
        }

        return user;
    }

    public User checkUser(UserCommand command) throws Exception {
        User user = userRepository.findFirstByName(command.getName());
        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!PasswordStorage.verifyPassword(command.getPassword(), user.getPassword())) {
            throw new LoginFailedException();
        }

        if (!user.isTokenValid()) {
            throw new TokenExpiredException();
        }

        return user;
    }

    public User getUserFromAuth(String auth) {
        User user = userRepository.findFirstByToken(auth.split(" ")[1]);
        if (!user.isTokenValid()) {
            throw new TokenExpiredException();
        }
        return user;
    }
}