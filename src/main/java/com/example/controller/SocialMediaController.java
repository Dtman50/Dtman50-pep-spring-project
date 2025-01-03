package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateAccountException;
import com.example.exception.UnauthorizedUserException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 *  Endpoints:
 * 
 *  POST localhost:8080/register : post a new account. The body will contain a representation 
 *  of a JSON Account, but will not contain an account_id because it is automatically generated.
 * 
 *  POST localhost:8080/login: post a new login. The request body will contain a JSON representation 
 *  of an Account, not containing an account_id because it is automatically generated. In the future, 
 *  this action may generate a Session token to allow the user to securely use the site.
 * 
 *  POST localhost:8080/messages: post a new message.  The request body will contain a JSON 
 *  representation of a message, which should be persisted to the database, but will not contain a 
 *  message_id.
 * 
 *  GET localhost:8080/messages: gets all messages. The response body should contain a JSON 
 *  representation of a list containing all messages retrieved from the database. It is expected for
 *  the list to simply be empty if there are no messages.
 * 
 *  GET localhost:8080/messages/{message_id}: gets single message by message id. The response body 
 *  should contain a JSON representation of the message identified by the message_id. It is expected 
 *  for the response body to simply be empty if there is no such message.
 * 
 *  GET localhost:8080/accounts/{account_id}/messages: gets all messages from user given account id.
 *  The response body should contain a JSON representation of a list containing all messages posted 
 *  by a particular user, which is retrieved from the database. It is expected for the list to simply 
 *  be empty if there are no messages.
 * 
 *  DELETE localhost:8080/messages/{message_id}: deletes a message given message id. The deletion of an 
 *  existing message should remove an existing message from the database. If the message existed, the 
 *  response body should contain the now-deleted message.
 *  
 *  PATCH localhost:8080/messages/{message_id}: updates a message given message id. The request body 
 *  should contain a new message_text values to replace the message identified by message_id. The request 
 *  body can not be guaranteed to contain any other information.
 * 
 *  
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // Account Controllers:

    /**
     * Handler to post a new account.
     * 
     * If all the conditions are met from the Account Service, the response body should contain a JSON of the 
     * Account, including its account_id and return a status 200.
     * - If the registration is not successful due to a duplicate username, the response status should be 409. (Conflict)
     * - If the registration is not successful for some other reason, the response status should be 400. (Client error)
     * 
     * @throws Exception if there are any other issues registering the account.
     * @throws  DuplicateAccountException if the account already exists.
     */
    @PostMapping("register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        Account addedAccount = accountService.registerAccount(account);

        if(addedAccount != null){
            return ResponseEntity.ok(account);
        }
        throw new DuplicateAccountException("Account exists. Try again with different Username.");
        
    }


     /**
     * Handler to post a new login.
     * 
     * If all conditions are met in the Account Service class, the response body should contain a JSON of the 
     * account in the response body, including its account_id and return a status 200.
     * - If the login is not successful, the response status should be 401. (Unauthorized)
     * 
     * @throws Exception if there are any other issues logging in the account.
     * @throws UnauthorizeUserException will be thrown if there is an issue with the username or password.
     */
    @PostMapping("login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account) {
        Account loginAccount = accountService.loginAccount(account);
        
        if (loginAccount != null) {
            return ResponseEntity.ok(loginAccount);
        } else {
            throw new UnauthorizedUserException();
        }
        
    }



     // Message Controllers:

    /**
     * Handler to post a new message.
     * 
     * If all the conditions are met from the Message Service, the response body should contain a JSON of the 
     * Account, including its message_id and return a status 200.
     * - If the creation of the message is not successful, the response status should be 400. (Client error)
     * 
     * @throws Exception if there are any other issues creating message.
     */
    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);

        if(createdMessage != null) {
            return ResponseEntity.ok(createdMessage);
        }
        return ResponseEntity.badRequest().body(message);
    }


     /**
     * Handler to retrieve all messages.
     * 
     * It is expected for the list to simply be empty if there are no messages. 
     * The response status should always be 200, which is the default.
     * 
     * @throws Exception if there are any other issues retrieving messages.
     */
    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }


     /**
     * Handler to retrieve the single message given the message_id.
     * 
     * The response body should contain a JSON representation of the message identified by the message_id.
     * It is expected for the response body to simply be empty if there is no such 
     * message. The response status should always be 200, which is the default.
     * 
     * @throws Exception if there are any other issues retrieving message.
     */
    @GetMapping("messages/{message_id}")
    public ResponseEntity<Message> getMessageByID(@PathVariable String message_id) {
        Message message = messageService.getMessageByID(Integer.parseInt(message_id));

        if (message != null) {
            return ResponseEntity.ok(message);
        }
        
        return ResponseEntity.ok(null);

    }


     /**
     * Handler to retrieve the messages given the account id.
     * 
     * It is expected for the list to simply be empty if there are no messages.
     * The response status should always be 200, which is the default
     * 
     * @throws Exception if there are any other issues retrieving messages.
     */
    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountID(@PathVariable String account_id) {
        List<Message> messages = messageService.getMessagesByAccountID(Integer.parseInt(account_id));
        if(messages != null) {
            return ResponseEntity.status(HttpStatus.OK).body(messages);
        } 
        return ResponseEntity.ok(messages);
    }


     /**
     * Handler to delete a message given the message id.
     * 
     * If the message existed, the response body should contain the now-deleted message. The response status 
     * should be 200, which is the default.
     * 
     * - If the message did not exist, the response status should be 200, but the response body should be empty. 
     * This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint 
     * should respond with the same type of response.
     * 
     * @throws Exception if there are any other issues deleting message.
     */
    @DeleteMapping("messages/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("message_id") Integer messageId) {
        // rows affected from delete
        int rowsAffected = messageService.deleteMessage(messageId);

        if(rowsAffected > 0) { // loop
            return ResponseEntity.ok(rowsAffected); // if rows were affected return status 200 and the number of rows in the body
        } else {
            return ResponseEntity.ok().build(); // else return status 200 and nothing in the body
        }
        
    }


     /**
     * Handler to update a message given the message id.
     * 
     * If the update is successful, the response body should contain the number of rows updated (1), and the
     * response status should be 200, which is the default. The message existing on the database should have 
     * the updated message_text.
     * 
     * - If the update of the message is not successful for any reason, the response status should be 400. (Client error)
     * 
     * @throws Exception if there are any other issues updating the message.
     */

    @PatchMapping("messages/{message_id}")
    public ResponseEntity<?> updateMessage(@PathVariable("message_id") int messageId, @RequestBody Message message) {
        int rowsAffected = messageService.updateMessage(messageId, message);
        if (rowsAffected > 0) {
            return ResponseEntity.ok(rowsAffected);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
