/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester;

import com.mansueli.mailtester.email.Account;
import com.mansueli.mailtester.email.CurrentAccount;
import com.mansueli.mailtester.utils.EmailUtils;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.activation.FileDataSource;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mansueli
 */
public class SmtpController implements Initializable {

//    @FXML
//    private Button createButton;
    @FXML
    private AnchorPane smtpPane;
    @FXML
    private TextField accountBox;
    @FXML
    private ToggleGroup smtpToggleGroup;
    @FXML
    private ToggleButton emlButton;
    @FXML
    private ToggleButton attachButton;
    @FXML
    private ToggleButton htmlButton;
    @FXML
    private ToggleButton plainButton;
    @FXML
    private TextField toBox;
    @FXML
    private Button sendButton;
    private final Logger logger = LoggerFactory.getLogger(SmtpController.class);
    private File attachment;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CurrentAccount currentAcc = MainController.currentAccount;
        if (EmailUtils.isInvalidEmailAddress(currentAcc.getEmail().getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No account was properly set.");
            alert.setContentText("Please define an account before using this.");
            alert.showAndWait();
            logger.error("No account set error");
        } else {

            accountBox.textProperty().bind(currentAcc.getEmail());
            System.out.println("\n\nSMTP\n" + currentAcc);
            setButtonGraphic(emlButton, "/res/eml_unselected.png", "/res/eml_selected.png");
            setButtonGraphic(attachButton, "/res/attachment_unselected.png", "/res/attachment_selected.png");
            setButtonGraphic(htmlButton, "/res/html_unselected.png", "/res/html_selected.png");
            setButtonGraphic(plainButton, "/res/plain_unselected.png", "/res/plain_selected.png");
            if (!accountBox.getText().isEmpty() || EmailUtils.isEmailValid(accountBox.getText())) {
                toBox.setDisable(false);
                if (!toBox.getText().isEmpty() || EmailUtils.isEmailValid(toBox.getText())) {
                    sendButton.setDisable(false);
                } else {
                    sendButton.setDisable(true);
                }
            } else {
                toBox.setDisable(true);
                toBox.setText("Invalid email set in the account");
            }
            toBox.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!toBox.getText().isEmpty() || EmailUtils.isEmailValid(toBox.getText())) {
                        sendButton.setDisable(false);
                    } else {
                        sendButton.setDisable(true);
                    }
                }
            });
            accountBox.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!accountBox.getText().isEmpty() || EmailUtils.isEmailValid(accountBox.getText())) {
                        toBox.setDisable(false);
                        if (!toBox.getText().isEmpty() || EmailUtils.isEmailValid(toBox.getText())) {
                            sendButton.setDisable(false);
                        } else {
                            sendButton.setDisable(true);
                        }
                    } else {
                        toBox.setDisable(true);
                        toBox.setText("Invalid email set in the account");
                    }
                }
            });
            plainButton.setSelected(true);
            smtpToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                    if (attachButton.isSelected()) {
                        attachment = selectFile("Select File to attach");
                    }
                }
            });
        }
    }

    private void setButtonGraphic(ToggleButton button, String pathSelected, String pathUnselected) {
        final Image unselected = new Image(getClass().getResourceAsStream(pathSelected));
        final Image selected = new Image(getClass().getResourceAsStream(pathUnselected));
        final ImageView toggleImage = new ImageView();
        button.setGraphic(toggleImage);
        toggleImage.getStyleClass().add("-fx-padding: 0px;");
        toggleImage.imageProperty().bind(Bindings
                .when(button.selectedProperty())
                .then(selected)
                .otherwise(unselected)
        );
        //button.getStylesheets().add("email");
    }

    @FXML
    private void handleSendAction(ActionEvent event) {
        ResultsController.resetLog();
        if (plainButton.isSelected()) {
            sendPlainMail();
        } else if (emlButton.isSelected()) {
            sendEMLMail();
        } else if (attachButton.isSelected()) {
            sendMailWithAttachment();
        } else if (htmlButton.isSelected()) {
            sendHTMLMail();
        }
    }

    private void sendPlainMail() {
        try {
            Account fromAcc = MainController.currentAccount.getAccount();
            Mailer mailer = defineMailer(fromAcc);
            Lorem lorem = LoremIpsum.getInstance();
            int randomNum = ThreadLocalRandom.current().nextInt(3, 10);
            StringBuilder body = new StringBuilder();
            body = body.append(lorem.getParagraphs(randomNum - 1, ThreadLocalRandom.current().nextInt(1, 4)));
            body = body.append("\n\n").append(lorem.getName()).append("\n").append(lorem.getEmail()).append("\n").append(lorem.getPhone());
            Email email = EmailBuilder.startingBlank()
                    .from("Happy Tester", fromAcc.getEmail())
                    .to(toBox.getText())
                    .withSubject(lorem.getWords(randomNum))
                    .withPlainText(body.toString())
                    .buildEmail();
            mailer.sendMail(email);
            showOKDialog();
        } catch (Exception e) {
            logger.error("not sent "+ e.toString());
            showErrorDialog(e.getLocalizedMessage(), e.toString());
        }
    }

    private void sendEMLMail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void sendMailWithAttachment() {
        try {
            Account fromAcc = MainController.currentAccount.getAccount();
            Mailer mailer = defineMailer(fromAcc,30);
            Lorem lorem = LoremIpsum.getInstance();
            int randomNum = ThreadLocalRandom.current().nextInt(3, 10);
            StringBuilder body = new StringBuilder();
            body = body.append(lorem.getHtmlParagraphs(randomNum - 1, ThreadLocalRandom.current().nextInt(1, 4)));
            body = body.append("\n\n").append(lorem.getName()).append("\n").append(lorem.getEmail()).append("\n").append(lorem.getPhone());
            logger.info("making email with attachment");
            System.out.println("error vaca");
            Email email = EmailBuilder.startingBlank()
                    .from("Happy Tester", fromAcc.getEmail())
                    .to(toBox.getText())
                    .withSubject(lorem.getWords(randomNum))
                    .withAttachment(attachment.getName(), new FileDataSource(attachment.getAbsolutePath()))
                    .withHTMLText(body.toString())
                    .buildEmail();
            
            mailer.sendMail(email);
        } catch (Exception e) {
            logger.error("Couldn't send message " + e.toString());
            showErrorDialog(e.getLocalizedMessage(), e.toString());
        }
    }

    private void sendHTMLMail() {
        try {
            Account fromAcc = MainController.currentAccount.getAccount();
            Mailer mailer = defineMailer(fromAcc);
            Lorem lorem = LoremIpsum.getInstance();
            int randomNum = ThreadLocalRandom.current().nextInt(3, 10);
            StringBuilder body = new StringBuilder();
            body = body.append(lorem.getHtmlParagraphs(randomNum - 1, ThreadLocalRandom.current().nextInt(1, 4)));
            body = body.append("\n\n").append(lorem.getName()).append("\n").append(lorem.getEmail()).append("\n").append(lorem.getPhone());
            Email email = EmailBuilder.startingBlank()
                    .from("Happy Tester", fromAcc.getEmail())
                    .to(toBox.getText())
                    .withSubject(lorem.getWords(randomNum))
                    .withHTMLText(body.toString())
                    .buildEmail();
            mailer.sendMail(email);
        } catch (Exception e) {
            logger.error("Couldn't send message " + e.toString());
            showErrorDialog(e.getLocalizedMessage(), e.toString());
        }
    }

    private File selectFile(String title) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        return chooser.showOpenDialog(new Stage());
    }

    private Mailer defineMailer(Account acc) {
        //15 secounds as standard timeout
        return defineMailer(acc, 15);
    }
    private Mailer defineMailer(Account acc, int timeout) {
        Mailer mailer = MailerBuilder
                .withSMTPServer(acc.getSmtp(), acc.getSmtpPort(), acc.getEmail(), acc.getPassword())
                .withTransportStrategy(getTransportStrategy(acc))
                .withSessionTimeout(timeout * 1000)
                .clearEmailAddressCriteria() // turns off email validation
                .withDebugLogging(true)
                .buildMailer();
        return mailer;
    }
    private TransportStrategy getTransportStrategy(Account acc) {
        TransportStrategy ts;
        switch (acc.getSmtpPort()) {
            case 25:
                ts = TransportStrategy.SMTP;
                break;
            case 465:
                ts = TransportStrategy.SMTPS;
                break;
            case 587:
            default:
                ts = TransportStrategy.SMTP_TLS;
        }
        return ts;
    }

    private void showOKDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SENT!");
        alert.setContentText("We tried to send the message, it will probably arrive. ;-)");
        alert.showAndWait();
    }

    private void showErrorDialog(String s, String errorCode) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("SEND ERROR");
        alert.setHeaderText("We couldn't send the message because of "+s.toLowerCase());
        alert.setContentText(errorCode);
        alert.showAndWait();
    }
}
