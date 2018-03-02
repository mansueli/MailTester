/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mansueli
 */
public class MSG2EMLController implements Initializable {

    private final Logger logger = LoggerFactory.getLogger(MSG2EMLController.class);
    private File sourceFile;
    private File destFile;
    @FXML
    private Button sourceButton;
    @FXML
    private Button destButton;
    @FXML
    private VBox msg2emlPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("MSG2EML tab selected");
    }

    @FXML
    private void sourcePick(ActionEvent event) {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions
                = new FileChooser.ExtensionFilter(
                        "Outlook Email messages", "*.msg");
        fc.getExtensionFilters().add(fileExtensions);
        sourceFile = fc.showOpenDialog(msg2emlPane.getScene().getWindow());
        logger.debug("selected as source the file " + sourceFile.getAbsolutePath());
        sourceButton.setText(sourceFile.getName());
    }

    @FXML
    private void destPick(ActionEvent event) {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions;
        fileExtensions = new FileChooser.ExtensionFilter("Email messages", "*.eml");
        fc.getExtensionFilters().add(fileExtensions);
        destFile = fc.showSaveDialog(msg2emlPane.getScene().getWindow());
        logger.debug("selected as source the file " + destFile.getAbsolutePath());
        destButton.setText(destFile.getName());
    }

    @FXML
    private void convertMail(ActionEvent event) {
        ResultsController.resetLog();
        try {
            String path = sourceFile.getAbsolutePath();
            if (path.isEmpty() || path == null) {
                throw new Exception("No source file selected");
            }
            MSG2EML.convert(sourceFile.getAbsolutePath(), destFile.getAbsolutePath());
            sourceButton.setText("select source file");
            destButton.setText("select output file");
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("SENT :)");
            alert.setContentText("The file was converted successfully.");
            alert.showAndWait();
//            alert.setHeaderText("The file was converted successfully.");
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Problem in the file conversion was found.");
            alert.setContentText("Could not convert file ");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();
            Label label = new Label("The exception stacktrace was:");
            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);
            logger.error("File error " + ex.getLocalizedMessage());
// Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();
        }
    }

}
