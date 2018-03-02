/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester;

import com.mansueli.mailtester.email.Account;
import com.mansueli.mailtester.email.CurrentAccount;
import com.mansueli.mailtester.utils.EmailUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author Mansueli
 */
public class AccountController implements Initializable {

    private CurrentAccount currentAcc;
    @FXML
    private TextField emailField;
    @FXML
    private TextField smtpServerField;
    @FXML
    private TextField imapServerField;
    @FXML
    private TextField imapPortField;
    @FXML
    private TextField smtpPortField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField nameField;
    @FXML
    private CheckBox defaultBox;

    @Override

    public void initialize(URL location, ResourceBundle resources) {
        currentAcc = MainController.currentAccount;
        emailField.textProperty().bindBidirectional(currentAcc.getEmail());
        passwordField.textProperty().bindBidirectional(currentAcc.getPassword());
        imapServerField.textProperty().bindBidirectional(currentAcc.getImapserver());
        smtpServerField.textProperty().bindBidirectional(currentAcc.getSmtpserver());
        imapPortField.textProperty().bindBidirectional(currentAcc.getImapport());
        smtpPortField.textProperty().bindBidirectional(currentAcc.getSmtpport());
        nameField.textProperty().bindBidirectional(currentAcc.getName());
        System.out.println("INITIAL\n" + currentAcc);
        defaultBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (defaultBox.isSelected()) {
                    imapServerField.setDisable(true);
                    smtpServerField.setDisable(true);
                    imapPortField.setDisable(true);
                    smtpPortField.setDisable(true);
                    System.out.println("ON\n" + currentAcc);
                } else {
                    imapServerField.setDisable(false);
                    smtpServerField.setDisable(false);
                    imapPortField.setDisable(false);
                    smtpPortField.setDisable(false);
                    System.out.println("OFF\n" + currentAcc);
                }

            }
        });
        passwordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (EmailUtils.isInvalidEmailAddress(emailField.getText()) || passwordField.getText().isEmpty()) {
                    imapServerField.setText("invalid email or password");
                    smtpServerField.setText("invalid email or password");
                } else {
                    currentAcc.setAccount(new Account(emailField.getText(), passwordField.getText()));
                }
            }
        });
        emailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (EmailUtils.isInvalidEmailAddress(emailField.getText()) || passwordField.getText().isEmpty()) {
                        imapServerField.setText("invalid email or password");
                        smtpServerField.setText("invalid email or password");
                    } else {
                        currentAcc.setAccount(new Account(emailField.getText(), passwordField.getText()));
                    }
                } catch (Exception e) {
                }
            }
        });

    }

}
