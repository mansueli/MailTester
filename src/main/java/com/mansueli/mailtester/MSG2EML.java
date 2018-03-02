/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester;

import java.io.File;
import java.io.FileOutputStream;
import javafx.scene.control.Alert;
import javax.mail.internet.MimeMessage;
import org.simplejavamail.converter.EmailConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mansueli
 */
public class MSG2EML {

    private static final Logger logger = LoggerFactory.getLogger(SmtpController.class);

    public static boolean convert(String msgPath, String emlPath) {
        try {
            MimeMessage message = EmailConverter.outlookMsgToMimeMessage(new File(msgPath));
            message.writeTo(new FileOutputStream(new File(emlPath)));
            return true;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Convertion ERROR");
            alert.setHeaderText("There was an error while performing the conversion.");
            alert.setContentText(e.getLocalizedMessage());
            logger.error("Error on convertion " + e.getLocalizedMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        String msg = "C:\\Users\\Mansueli\\Desktop\\CA LG.LogEntry Greške - DM okruženje.msg";
        String eml = "C:\\Users\\Mansueli\\Desktop\\CA LG.LogEntry Greške - DM okruženje.eml";
        System.out.println("result" + convert(msg, eml));
    }
}
