/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester.email;

/**
 *
 * @author Mansueli
 */
public class Account {

    private String name;
    private String password;
    private String email;
    private String smtp;
    private String imap;
    private int smtpPort = -1;
    private int imapPort = 993;

    /**
     * *
     *
     * @param email email
     * @param pass password
     * @param smtp smtp server
     * @param imap imap server
     * @param smtpPort SMTP port number
     * @param imapPort IMAP port number
     */
    public Account(String email, String pass, String smtp, String imap, int smtpPort, int imapPort, String name) {
        this.password = pass;
        this.email = email;
        this.smtp = smtp;
        this.imap = imap;
        this.smtpPort = smtpPort;
        this.imapPort = imapPort;
        this.name = name;
    }

    public String getName() {
        if (name == null) {
            return "Monkey Tester";
        } else if (name.isEmpty()) {
            return "Doge Tester";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


  
     /**
     * @param email email
     * @param pass password
     * @param name name
     */
    public Account(String email, String pass, String name) {
        this(email, pass, "smtp." + email.split("@")[1], "imap." + email.split("@")[1], 587, 993 , name);
    }

 

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getImap() {
        return imap;
    }

    public void setImap(String imap) {
        this.imap = imap;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public int getImapPort() {
        return imapPort;
    }

    public void setImapPort(int imapPort) {
        this.imapPort = imapPort;
    }

}
