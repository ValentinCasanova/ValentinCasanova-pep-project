package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;
import java.util.ArrayList;


public class MessageService {
    private MessageDAO messageDAO;

    // No-args constructor for messageService which creates an messageDAO.
    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    /**
     * Constructor for a messageService when a messageDAO is provided.
     * @param bookDAO
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    // Persist new message
    public Message newMessage(Message message){
        // is not blank and under 255 char
        if (!message.message_text.isBlank() && message.message_text.length() < 255){
            // posted_by refers to existing user
            if(messageDAO.userExists(message)){
                return messageDAO.newMessage(message);
            }
        }
        return null;
        
    }

    // Get Existing Message
    public Message getMessage(String message_id){
        return messageDAO.getMessage(message_id);
    }

    // Get all Messages
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    // Delete Message
    public Message deleteMessage(String message_id){
        return messageDAO.deleteMessage(message_id);
    }

    // Update Message
    public Message updateMessage(String message_id, String message_text){
        // Check not blank and <255 characters
        if(!message_text.isBlank() && message_text.length() < 255){
            return messageDAO.updateMessage(message_id, message_text);
        }
        return null;
    }

    public List<Message> getAllUserMessages(String account_id){
        return messageDAO.getAllUserMessages(account_id);
    }
    
}
