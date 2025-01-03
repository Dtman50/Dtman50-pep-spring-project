package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    /**
     * Retrieve all Messages from the message table.
     *
     * @return list of all messages.
     */
    public List<Message> getAllMessages() {
        List<Message> messages = messageRepository.findAll();
        return messages;
    }


     /**
     * Retrieve Message from the message table by message id.
     *
     * @return single message by id.
     * @param id the id of the message to be retrieved
     */
    public Message getMessageByID(Integer id) {
        return (Message)messageRepository.findByMessageId(id);
         
    }


     /**
     * Retrieve all Messages from the message table given an account id.
     *
     * @return list of messages by specific account.
     */
    public List<Message> getMessagesByAccountID(int id) {
        List<Message> messages = messageRepository.findByPostedBy(id);
        return messages;

    }


     /**
     * The deletion of an existing message should remove an existing message from the database.
     * 
     * @param id the message_id of the message to be deleted
     */
    public int deleteMessage(int id) {
        // if the message to be deleted exists delete it and return 1 representing the row affected
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0; // the message doesnt exist return 0
    }


     /**
     * Insert a message into the Message table.
     * 
     * The creation of the message will be successful if and only if the message_text is not blank, 
     * is under 255 characters, and posted_by refers to a real, existing user. 
     * 
     * @return Message returns newly inserted message object
     * @param message the message object to be inserted into the database
     */
    public Message createMessage(Message message) {
        // store all messages to make sure user exists for new message
        List<Message> messages = messageRepository.findAll();
        for (Message mess : messages) {
            if (mess.getPostedBy().equals(message.getPostedBy()) ) { // check if user exists
                if(message.getMessageText().length() > 0 && message.getMessageText().length() < 255) { // check for correct requirements
                    return messageRepository.save(message); // insert the message
                } else {
                    return null; // requirements are valid
                }
            }
        }
        return null; // the user doesnt exist
    }


     /**
     * Update the message by the message id to a new message text. The update of a message should be 
     * successful if and only if the message id already exists and the new message_text is not blank 
     * and is not over 255 characters.
     *
     * @param id a message ID.
     * @param message a message object. the message object does not contain a flight ID.
     */

    public int updateMessage(int id, Message message) {
        // message that needs to be updated
        Message messageToUpdate = messageRepository.findByMessageId(id);
        // if it exists
        if (messageToUpdate != null) {
            if(message.getMessageText().length() == 0) { // check if the message being passed in is not empty
                return 0; // cant update message
            } else if (message.getMessageText().length() > 255) { // check if the message isnt too long
                return 0; // cant update message
            } else { // otherwise update the message text of the message to be updated
                messageToUpdate.setMessageText(message.getMessageText()); 
                messageRepository.save(messageToUpdate); // then save(insert) the message
                return 1; // return the number of rows affected;
            }
        }
        return 0; // the message to update doesnt exist
    }
}
