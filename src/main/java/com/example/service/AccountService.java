package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateAccountException;
import com.example.exception.UnauthorizedUserException;
import com.example.repository.AccountRepository;


@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    
    /**
     * Retrieve all accounts from the Account table.
     * 
     * @return List of Accounts.
     */
    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    /**
     * Use the AccountRepository to persist and register an account. The given Account will not have an id provided.
     * The registration will be successful if and only if the username is not blank, the password 
     * is at least 4 characters long, and an Account with that username does not already exist.
     * 
     * @return the account object that was inserted
     * @param account the account object of the account to be registered
     * @throws DuplicateAccountException
     */
    public Account registerAccount(Account account) {
        // Get all accounts
        List<Account> accounts = (List<Account>) accountRepository.findAll();
        String message = "Account already exists. Enter another account.";
        
        // loop
        for (Account acc : accounts) {
            if(account.getUsername().equals(acc.getUsername())){ // check if the account already exists and throw exception if so
                throw new DuplicateAccountException(message);
            } else { // Otherwise, check if the username and password meet requirements and insert and return the account if so
                if(account.getUsername().length() > 0 && account.getPassword().length() > 4) {
                    return accountRepository.save(account);
                }
            }
        }

        return null;
    }

    

    /**
     * Use the AccoutRepository to login an account. The given Account will not have an id provided.
     * The login will be successful if and only if the username and password provided in the 
     * request body JSON match a real account existing on the database.
     *
     * @param account the account object to be logged in.
     * @return the account if that account exists in the DB else return null.
     */
    public Account loginAccount(Account account) {
        //Get account that matches by usernames
        Account loginAccount = accountRepository.findByUsername(account.getUsername());

        //  account exists
        if(loginAccount != null) {
            if(account.getUsername().equals(loginAccount.getUsername()) && account.getPassword().equals(loginAccount.getPassword())) {
                return loginAccount; // pass in the acc if the credentials are validated
            } else { // Credentials don't match.
                throw new UnauthorizedUserException("Credentials don't match. Try again with different username/password.");
            }
        }

        return null; // no username exists 
    }
}
