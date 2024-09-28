package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Account(
 * account_id  int primary key auto_increment,
 * username    varchar(255) unique,
 * password    varchar(255)
 * )
 */
public class AccountDAO {
    
    // Add new Account
    // Account exists ? Null : Account
    public Account addAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        
        String sql = "INSERT INTO account (username, password) VALUES (?,?)";
        
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();

            // Return Account with assigned Account ID
            return account;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Get Account By UserName
    public Account getAccountByUserName(Account account){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Account selectedAccount = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                return selectedAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    public Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "SELECT * FROM account WHERE username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Account selectedAccount = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                return selectedAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }
}
