/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester;

import com.mansueli.mailtester.email.CurrentAccount;
import com.mansueli.mailtester.utils.SystemOutListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SegmentedButton;

/**
 * FXML Controller class
 *
 * @author Mansueli
 */
public class MainController implements Initializable {
    public static CurrentAccount currentAccount = CurrentAccount.getInstance();

    @FXML
    private AnchorPane tooglePanel;
    @FXML
    private AnchorPane mainArea;
    @FXML
    private ToggleButton smtpButton;
    @FXML
    private ToggleButton imapButton;
    @FXML
    private ToggleButton accountButton;
    @FXML
    private ToggleButton msg2emlButton;
    @FXML
    private ToggleButton resultsButton;
    @FXML
    private ToggleGroup mainToggleGroup;
    private static final List<String> log = new ArrayList<String>();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SystemOutListener sout = SystemOutListener.GetSingleton();
        smtpButton.getStyleClass().add("too");
        imapButton.getStyleClass().add("too");
        accountButton.getStyleClass().add("acc");
        msg2emlButton.getStyleClass().add("too");
        resultsButton.getStyleClass().add("too");
        //             Image img = new Image(getClass().getResourceAsStream("/res/imageExporting.png")); /src/res/imageExporting.png
        //https://stackoverflow.com/questions/16360323/javafx-table-how-to-add-components/16366638#16366638

        mainToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (smtpButton.isSelected()) {
                    loadFXML("smtp");
                } else if (imapButton.isSelected()) {
                    loadFXML("imap");
                } else if (msg2emlButton.isSelected()) {
                    loadFXML("msg2eml");
                } else if (resultsButton.isSelected()) {
                    loadFXML("results");
                } else {
                    loadFXML("account");
                }
            }
        });
        accountButton.setSelected(true);
    }

    private void loadFXML(String name) {
        String fxmlFile;
        switch (name) {
            case "smtp":
                fxmlFile = "smtp.fxml";
                break;
            case "imap":
                fxmlFile = "imap.fxml";
                break;
            case "msg2eml":
                fxmlFile = "msg2eml.fxml";
                break;
            case "results":
                fxmlFile = "results.fxml";
                break;
            default:
                fxmlFile = "account.fxml";
        }//FXMLLoader.load(getClass().getResource("/res/fxml/Main.fxml"));
        try {
            mainArea.getChildren().clear();
            VBox vbox = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlFile));
            vbox.getStylesheets().add("/styles/main.css");
            mainArea.getChildren().add(vbox);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // secPane.getChildren().add(newLoadedPane);
    }
}
