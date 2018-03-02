/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester.utils;

import com.mansueli.mailtester.email.Account;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author Mansueli
 */
public class EmailUtils {

    public static Session makeIMAPSSession(Account account) throws MessagingException {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imap");
        props.put("mail.smtp.timeout", 5000);
        props.put("mail.imap.ssl.enable", "true");
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imap");
        System.out.println("connecting store..");
        store.connect(account.getImap(), account.getImapPort(), account.getEmail(), account.getPassword());
        session.setDebug(false);
        System.out.println("Connection successful with " + account.getEmail() + " on " + account.getImap());
        return session;
    }

    public static Store getIMAPStore(Account account) throws MessagingException {
        return makeIMAPSSession(account).getStore();
    }

    public static boolean isEmailValid(String email) {
        final String EMAIL_PATTERN
                = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isInvalidEmailAddress(String email) {
        boolean result = false;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = true;
        }
        return result;
    }
}
