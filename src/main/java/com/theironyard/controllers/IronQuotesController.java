package com.theironyard.controllers;

import com.theironyard.commands.QuoteCommand;
import com.theironyard.commands.TagCommand;
import com.theironyard.commands.UserCommand;
import com.theironyard.entities.Quote;
import com.theironyard.entities.Tag;
import com.theironyard.entities.User;
import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.exceptions.TokenExpiredException;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.services.QuoteRepository;
import com.theironyard.services.TagRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by EddyJ on 8/1/16.
 */
@RestController
public class IronQuotesController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    TagRepository tagRepository;

    @RequestMapping(path = "/quotes", method = RequestMethod.POST)
    public Quote createQuote(@RequestHeader(value = "Authorization") String userToken,@RequestBody Quote quote){
        getUserFromAuth(userToken);
        quoteRepository.save(quote);
        return quote;
    }

    @RequestMapping(path = "/quotes",method = RequestMethod.GET)
    public List<Quote> getAllQuotes(){
        List<Quote> quoteList = quoteRepository.findAll();
        return quoteList;
    }

    @RequestMapping(path = "/quotes/{id}",method = RequestMethod.GET)
    public Quote getQuote(@PathVariable Integer id){
        Quote quote = quoteRepository.findOne(id);
        return quote;
    }

    @RequestMapping(path = "/quotes/{id}",method = RequestMethod.PUT)
    public Quote updateQuote(@RequestHeader(value = "Authorization") String userToken,@PathVariable Integer id, @RequestBody QuoteCommand quoteCommand){
        getUserFromAuth(userToken);
        Quote quote = quoteRepository.findOne(id);
        quote.setText(quoteCommand.getText());
        quote.setAuthor(quoteCommand.getAuthor());
        quoteRepository.save(quote);
        return quote;
    }

    @RequestMapping(path = "/quotes/{id}",method = RequestMethod.DELETE)
    public void deleteQuote(@RequestHeader(value = "Authorization") String userToken,@PathVariable Integer id) throws Exception {
        getUserFromAuth(userToken);
        Quote deletedQuote = quoteRepository.findOne(id);
        if(deletedQuote == null){
            throw new Exception("Quote does not exist");
        }
        quoteRepository.delete(deletedQuote);
    }

    @RequestMapping(path = "/tags",method = RequestMethod.GET)
    public List<Tag> getAllTag(){
        List<Tag> tagList = tagRepository.findAll();
        return tagList;
    }

    @RequestMapping(path = "/tags/{id}",method = RequestMethod.GET)
    public Tag getTag(@PathVariable Integer id){
        Tag tag = tagRepository.findOne(id);
        return tag;
    }

    @RequestMapping(path = "/quotes/{id}/tags",method = RequestMethod.POST)
    public Tag addTag(@RequestHeader(value = "Authorization") String userToken,@PathVariable Integer id, @RequestBody TagCommand tagcommand){
        getUserFromAuth(userToken);
        Quote quote = quoteRepository.findOne(id);
        Tag tag = tagRepository.findFirstByValue(tagcommand.getValue());
        if (tag == null){
            tag = new Tag(tagcommand.getValue());
            tagRepository.save(tag);
        }
        quote.addTag(tag);
        tagRepository.save(tag);
        return tag;
    }

    @RequestMapping(path = "/quotes/{quoteId}/tags/{tagId}",method = RequestMethod.DELETE)
    public void deleteTag(@RequestHeader(value = "Authorization") String userToken,@PathVariable Integer quoteId, @PathVariable Integer tagId){
        getUserFromAuth(userToken);
        Quote quote = quoteRepository.findOne(quoteId);
        quote.deleteTag(tagRepository.getOne(tagId));
        quoteRepository.save(quote);
    }

    @RequestMapping(path = "/quotes/{quoteId}/tags/{tagId}",method = RequestMethod.PUT)
    public Tag updateTag(@RequestHeader(value = "Authorization") String userToken,@PathVariable Integer quoteId, @PathVariable Integer tagId ,@RequestBody TagCommand tagComannd){
        getUserFromAuth(userToken);
        Quote quote = quoteRepository.findOne(quoteId);
        Tag tag = quote.editTag(tagId, tagComannd.getValue());
        tagRepository.save(tag);
        return tag;
    }

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public User createUser(@RequestBody UserCommand userCommand) throws PasswordStorage.CannotPerformOperationException {
        User user = userRepository.findByUsername(userCommand.getUsername());
        if (user == null){
            user = new User(userCommand.getUsername(), PasswordStorage.createHash(userCommand.getPassword()));
            userRepository.save(user);
        }
        return user;
    }

    @RequestMapping(path = "/token", method = RequestMethod.POST)
    public Map getToken(@RequestBody UserCommand userCommand) throws Exception {
        User user = checkLogin(userCommand);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("token", user.getToken());
        return tokens;
    }
    @RequestMapping(path = "/token/regenerate",method = RequestMethod.PUT)
    public String regenerateToken(@RequestBody UserCommand userCommand) throws Exception{
        User user = checkLogin(userCommand);
        return user.regenerate();
    }

    public User checkLogin(UserCommand userCommand) throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        User user = userRepository.findByUsername(userCommand.getUsername());
        if(user == null){
            throw new UserNotFoundException();
        }
        if(!PasswordStorage.verifyPassword(userCommand.getPassword(), user.getPassword())){
            throw new LoginFailedException();
        }
        if(user.getExpiration().isBefore(LocalDateTime.now())){
            throw new TokenExpiredException();
        }
        return user;
    }

    public User getUserFromAuth(String userToken){
        User user = userRepository.findFirstByToken(userToken.split(" ")[1]);
        if(!user.isTokenValid()){
            throw new TokenExpiredException();
        }
        return user;
    }
}
