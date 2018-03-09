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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

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
        if (isAccountValid()) {
            setAccountonFields();
        }
        nameField.textProperty().bindBidirectional(currentAcc.getName());
        emailField.textProperty().bindBidirectional(currentAcc.getEmail());
        passwordField.textProperty().bindBidirectional(currentAcc.getPassword());
        currentAcc.getImapserver().bindBidirectional(imapServerField.textProperty());
        smtpServerField.textProperty().bindBidirectional(currentAcc.getSmtpserver());
        currentAcc.getImapport().bindBidirectional(imapPortField.textProperty());
        smtpPortField.textProperty().bindBidirectional(currentAcc.getSmtpport());
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
//                    System.out.println("OFF\n" + currentAcc);
                }

            }
        });
        ChangeListener<String> advanced = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (imapPortField.getText().isEmpty() || smtpPortField.getText().isEmpty()) {
                        imapServerField.setText("invalid PORT");
                        smtpServerField.setText("invalid PORT");
                    } else {
                        if (imapServerField.getText().isEmpty() || smtpServerField.getText().isEmpty()) {
                            imapServerField.setText("no server defined");
                            smtpServerField.setText("no server defined");
                        } else {
                            //Account(String email, String pass, String smtp, String imap, int smtpPort, int imapPort, String name)
                            currentAcc.setAccount(new Account(emailField.getText(), passwordField.getText(), smtpServerField.getText(), imapServerField.getText(),
                                    Integer.parseInt(smtpPortField.getText()), Integer.parseInt(imapPortField.getText()), nameField.getText()));
                        }
                    }
                } catch (Exception e) {

                }
            }
        };
        smtpServerField.textProperty().addListener(advanced);
        imapServerField.textProperty().addListener(advanced);
        smtpPortField.textProperty().addListener(advanced);
        imapPortField.textProperty().addListener(advanced);
        passwordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (EmailUtils.isInvalidEmailAddress(emailField.getText()) || passwordField.getText().isEmpty()) {
                    imapServerField.setText("invalid email or password");
                    smtpServerField.setText("invalid email or password");
                } else {
                    currentAcc.setAccount(new Account(emailField.getText(), passwordField.getText(), nameField.getText()));
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
                        currentAcc.setAccount(new Account(emailField.getText(), passwordField.getText(), nameField.getText()));
                    }
                } catch (Exception e) {
                   // System.out.println("Null pointer somewhere : " + e.getMessage());
                }
            }
        });

    }

    private boolean isAccountValid() {
        try {
            if (currentAcc.getEmail().get().isEmpty()
                    || currentAcc.getPassword().get().isEmpty()
                    || currentAcc.getImapserver().get().isEmpty()
                    || currentAcc.getSmtpserver().get().isEmpty()) {
                return false;
            } else {
                try {
                    int i = Integer.parseInt(currentAcc.getSmtpport().get());
                    i = Integer.parseInt(currentAcc.getImapport().get());
                    return true;
                } catch (NumberFormatException e) {
                    //reset defaults if something went wrong.
                    smtpPortField.setText("587");
                    imapPortField.setText("993");
                    return false;
                }

            }

        } catch (Exception e) {
            //System.out.println("INFO account wasn't valid/defined when this view started.");
            return false;
        }
    }

    private void setAccountonFields() {
        nameField.setText(currentAcc.getName().get());
        emailField.setText(currentAcc.getEmail().get());
        imapServerField.setText(currentAcc.getImapserver().get());
        smtpServerField.setText(currentAcc.getSmtpserver().get());
        smtpPortField.setText(currentAcc.getSmtpport().get());
        imapPortField.setText(currentAcc.getImapport().get());
    }
}
