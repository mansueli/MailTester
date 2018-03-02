/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester.email;

import com.mansueli.mailtester.utils.EmailUtils;
import java.util.Properties;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

/**
 *
 * @author Mansueli
 */
public class TransferEmails {

    private static void transfert(Folder folder_source, Folder folder_dest, int from_msg, int to_msg)
            throws Exception {
        // Check if the source folder contains messages
        if (folder_source.getMessageCount() == 0) {
            System.out.println("source folder is empty");
            folder_source.close(false);
            System.exit(1);
        }
        //Create the destination folder if it does not exist
        if (!folder_dest.exists()) {
            folder_dest.create(Folder.HOLDS_MESSAGES);
            System.out.println("Folder created: " + folder_dest.getFullName());
        }
        Message[] msgs = folder_source.getMessages(from_msg, to_msg);
        // Copy selected messages into the destination folder
        folder_source.copyMessages(msgs, folder_dest);
        System.out.println("successfully");
        folder_source.close(false);
    }
    public static void TransferEmails(Account accountFrom, Account accountDest, String fromFolder, String destFolder, int amount) throws Exception {

        try {
            Store fromStore = EmailUtils.getIMAPStore(accountFrom);
            Folder folderFrom = fromStore.getFolder(fromFolder);
            folderFrom.open(Folder.READ_WRITE);

            Store destStore = EmailUtils.getIMAPStore(accountDest);
            Folder folderDest = destStore.getFolder(destFolder);
            folderDest.open(Folder.READ_WRITE);
            transfert(folderFrom, folderDest, 1, amount);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
//    public static void main(String argv[]) throws Exception {
//        Properties props = System.getProperties();
//        props.setProperty("mail.store.protocol", "imaps");
//        try {
//            Session session = Session.getDefaultInstance(props, null);
//            Store store = session.getStore("imaps");
//            store.connect("imap.gmail.com", "microsofty.mobile@gmail.com", "rogo1234");
//            session.setDebug(false);
//            System.out.println("Connection successful");
//            Folder folderFrom = store.getFolder("Inbox");
//            folderFrom.open(Folder.READ_WRITE);
//
//            ///other
//            Session session2 = Session.getDefaultInstance(props, null);
//            Store store2 = session2.getStore("imaps");
//            store2.connect("zwareonline.com", "t2@zwareonline.com", "t2oms");
////            store2.connect("secureimap.t-online.de", "oms.support@t-online.de", "dark01funeral");1
//            session2.setDebug(false);
//            System.out.println("Connection successful");
//            Folder folderTo = store2.getFolder("Inbox");
//            folderTo.open(Folder.READ_WRITE);
//            transfert(folderFrom, folderTo, 1, 10);
//        } catch (Exception err) {
//            err.printStackTrace();
//        }
//        System.exit(0);
//    }
}
