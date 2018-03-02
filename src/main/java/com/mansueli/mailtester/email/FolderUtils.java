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
import javax.mail.MessagingException;
import javax.mail.*;
import com.mansueli.mailtester.utils.EmailUtils;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Store;
import javax.mail.Folder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mansueli
 */
public class FolderUtils {

    private static Store getConnectedStore(Account account) throws MessagingException {
        return EmailUtils.getIMAPStore(account);
    }

    public static boolean createFolder(Account account, String folderName) throws MessagingException {
        Store store = getConnectedStore(account);
        Folder defaultFolder = store.getDefaultFolder();
        return createFolder(store, defaultFolder, folderName);
    }

    private static boolean createFolder(Store store, Folder parent, String folderName) {
        boolean isCreated = true;

        try {
            Folder newFolder = parent.getFolder(folderName);
            isCreated = newFolder.create(Folder.HOLDS_MESSAGES);
            System.out.println("created: " + isCreated);

        } catch (Exception e) {
            System.out.println("Error creating folder: " + e.getMessage());
            e.printStackTrace();
            isCreated = false;
        }
        return isCreated;
    }

    public static boolean generateManyFolders(Account account, String prefix, int amount) throws MessagingException {
        Store store = getConnectedStore(account);
        boolean test = false;
        for (int i = 1; i <= amount; i++) {
            System.out.println("Creating \"" + prefix + "\"" + i);
            test = createFolder(account, prefix + i);
        }
        return false;
    }

    public static void deleteFolder(Account account, String folderName) throws Exception {
        Store store = EmailUtils.getIMAPStore(account);
        Folder folder = findSubFolder(store.getDefaultFolder(), folderName);
        folder.delete(true);
    }

    public static Folder findSubFolder(Folder parentFolder, String name) {
        Folder[] subfolders;
        try {
            subfolders = parentFolder.list();
            for (Folder f : subfolders) {
                if (f.getName().toLowerCase().contains(name.toLowerCase())) {
                    return f;
                } else {
                    try {
                        Folder[] subsub = f.list();
                        if (subsub != null) {
                            findSubFolder(f, name);
                        }
                    } catch (Exception e) {
                            return null;
                    }
                }
            }
        } catch (MessagingException ex) {
            Logger.getLogger(FolderUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    public static void main(String[] args) throws MessagingException {
//        ServerSettings server= new ServerSettings("o365");
//        server.setEmail("manyfolders@t2.onmicrosoft.com");
//        generateManyFolders(server,300);
//    }
}
