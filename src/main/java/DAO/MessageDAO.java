package DAO;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*  message
 *  message_id int primary key auto_increment,
 *  posted_by int,
 *  message_text varchar(255),
 *  time_posted_epoch bigint,
 *  foreign key (posted_by) references  account(account_id)
 */
public class MessageDAO {
    
    // Check if User Exists
    public boolean userExists(Message message){
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id = ?";
        
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, message.getPosted_by());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    // Persist new Message
    public Message newMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
        
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            
            preparedStatement.executeUpdate();
            
            // Return Message with assigned Message ID
            ResultSet rs = preparedStatement.getGeneratedKeys();
            Message selectedMessage = null;
            while (rs.next()){
                selectedMessage = new Message(
                    rs.getInt("message_id"),
                    message.getPosted_by(),
                    message.getMessage_text(),
                    message.getTime_posted_epoch());
            }
            return selectedMessage;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Get existing message by message_id
    public Message getMessage(String message_id){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.valueOf(message_id));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Message selectedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                return selectedMessage;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    // Get all Messages
    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList();
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Message selectedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(selectedMessage);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    // Delete Message
    public Message deleteMessage(String message_id){
        Connection connection = ConnectionUtil.getConnection();
        
        // Check if Message Exists
        Message message = getMessage(message_id);
        if (message == null){
            return null;
        }
        try{
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.valueOf(message_id));
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return message;
    }

    // Update Message
    public Message updateMessage(String message_id, String message_text){
        Connection connection = ConnectionUtil.getConnection();
        // Check if Message Exists
        Message message = getMessage(message_id);
        if (message == null){
            return null;
        }
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, message_text);
            preparedStatement.setInt(2, Integer.valueOf(message_id));
            preparedStatement.executeUpdate();
            return getMessage(message_id);
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Message> getAllUserMessages(String account_id){
        List<Message> messages = new ArrayList();
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.valueOf(account_id));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Message selectedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(selectedMessage);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
