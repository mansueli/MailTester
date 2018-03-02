/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester.email;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Mansueli
 */
public class CurrentAccount {

    private static CurrentAccount instance;   
    private Account account;
    private CurrentAccount(){}
    private final StringProperty email = new SimpleStringProperty();

    public StringProperty getEmail() {
        return email;
    }

    public StringProperty getPassword() {
        return password;
    }

    public StringProperty getImapserver() {
        return imapserver;
    }

    public StringProperty getSmtpserver() {
        return smtpserver;
    }

    public StringProperty getImapport() {
        return imapport;
    }

    public StringProperty getSmtpport() {
        return smtpport;
    }
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty imapserver = new SimpleStringProperty();
    private final StringProperty smtpserver = new SimpleStringProperty();
    private final StringProperty imapport = new SimpleStringProperty();
    private final StringProperty smtpport = new SimpleStringProperty();
    public static CurrentAccount getInstance(){
        if(instance == null){
            instance = new CurrentAccount();
        }
        return instance;
    }
    public void setAccount(Account account){
        this.account = account;
        email.setValue(account.getEmail());
        password.setValue(account.getPassword());
        imapserver.setValue(account.getImap());
        smtpserver.setValue(account.getSmtp());
        imapport.setValue(String.valueOf(account.getImapPort()));
        smtpport.setValue(String.valueOf(account.getSmtpPort()));
    }
    
    public Account getAccount(){
        return account;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(email.getValue()).append("; ");
        sb.append(password.getValue()).append("; ");
        sb.append(imapserver.getValue()).append("; ");
        sb.append(imapport.getValue()).append("; ");
        sb.append(smtpserver.getValue()).append("; ");
        sb.append(smtpport.getValue()).append("; ");
        return sb.toString();
    }

}
