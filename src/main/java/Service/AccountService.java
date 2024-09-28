package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    // No-args constructor for accountService which creates an AccountDAO.
    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    /**
     * Constructor for a AccountService when a AccountDAO is provided.
     * @param bookDAO
     */
    public AccountService(AccountDAO AccountDAO){
        this.accountDAO = AccountDAO;
    }

    // Persist New Account
    public Account addAccount(Account account) {
        // Username is not empty
        if (account.getUsername().isBlank()){
            return null;
        }
        // Password length > 4
        if (account.getPassword().length() <= 4){
            return null;
        }

        if (accountDAO.getAccountByUserName(account) == null){
            return accountDAO.addAccount(account);
        } else{
            return null;
        }
    }

    public Account getAccountByUserName(Account account){
        return accountDAO.getAccountByUserName(account);
    }

    public Account login(Account account){
        return accountDAO.login(account);
    }
}