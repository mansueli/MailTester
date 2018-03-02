/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester;

import com.mansueli.mailtester.email.CurrentAccount;
import com.mansueli.mailtester.utils.SystemOutListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author Mansueli
 */
public class MainController implements Initializable {

    public static CurrentAccount currentAccount = CurrentAccount.getInstance();
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MainController.class);
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
            case "msg2eml":
                fxmlFile = "msg2eml.fxml";
                break;
            case "results":
                fxmlFile = "results.fxml";
                break;
            case "smtp":
                if (isAccountDefined()) {
                    fxmlFile = "smtp.fxml";
                } else {
                    fxmlFile = "account.fxml";
                }
                break;
            case "imap":
                if (isAccountDefined()) {
                    fxmlFile = "imap.fxml";
                } else {
                    fxmlFile = "account.fxml";
                }
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

    private boolean isAccountDefined() {
        try {
            String acc = currentAccount.getAccount().getEmail();
            String pwd = currentAccount.getAccount().getPassword();
            if (acc.isEmpty() || pwd.isEmpty()) {
                throw new Exception("No email defined");
            }
            if (pwd.isEmpty()) {
                throw new Exception("No password defined");
            }
            return true;
        } catch (Exception e) {
            showErrorDialog(e.getMessage(), e.toString());
            accountButton.setSelected(true);
            return false;
        }
    }

    private void showErrorDialog(String s, String errorCode) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("NO Account ERROR");
        alert.setHeaderText("You need to set an account before using this function.");
        alert.setContentText(errorCode);
        alert.showAndWait();
    }
}
