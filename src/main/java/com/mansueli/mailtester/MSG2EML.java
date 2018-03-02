/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javafx.scene.control.Alert;
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

    private static final Logger logger = LoggerFactory.getLogger(MSG2EML.class);

    public static boolean convert(String msgPath, String emlPath) {
        try {
            logger.debug("MSG2EML - start");
            String message = EmailConverter.outlookMsgToEML(new File(msgPath));
            try (PrintStream out = new PrintStream(new FileOutputStream(emlPath))) {
                out.print(message);
            }catch(Exception e){
                logger.error("Couldn't save " +e.getMessage());
            }
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
    public static String convertToEMLString(String msgPath, String emlPath) {
        try {
            logger.debug("MSG2EML - start");
            return EmailConverter.outlookMsgToEML(new File(msgPath));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Convertion ERROR");
            alert.setHeaderText("There was an error while performing the conversion.");
            alert.setContentText(e.getLocalizedMessage());
            logger.error("Error on convertion " + e.getLocalizedMessage());
            return "";
        }
    }
}
