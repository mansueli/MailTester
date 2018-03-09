/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester;

import com.mansueli.mailtester.email.Account;
import com.mansueli.mailtester.email.CurrentAccount;
import com.mansueli.mailtester.utils.EmailUtils;
import com.sun.mail.imap.IMAPFolder;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mansueli
 */
public class IMAPController implements Initializable {
    
    private final IntegerProperty totalMsgs = new SimpleIntegerProperty(0);
    private final IntegerProperty totalFolders = new SimpleIntegerProperty(0);
    private final Logger logger = LoggerFactory.getLogger(IMAPController.class);
    private Account currentAcc;
    @FXML
    private Panel imapPanel;
    @FXML
    private ScrollPane listIMAP;
    private Stage progressStage;
    private String imapText;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        CurrentAccount currentAccount = MainController.currentAccount;
        currentAcc = currentAccount.getAccount();
        listIMAP.setPrefSize(750.00, 300.0);
        listIMAP.setMinSize(600.0, 250);
        if (EmailUtils.isInvalidEmailAddress(currentAcc.getEmail())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No account was properly set.");
            alert.setContentText("Please define an account before using this.");
            alert.showAndWait();
            logger.error("No account set error | address ->" + currentAcc.getEmail());
        } else {
            
            imapPanel.setHeading(new Label("running"));
            imapPanel.getStyleClass().add("panel-primary");
            TaskService service = new TaskService();
            ProgressBar progressBar = new ProgressBar();
            progressBar.setPrefSize(400, 100);
            progressBar.progressProperty().bind(service.progressProperty());
            progressStage = new Stage();
            VBox progressPanel = new VBox();
            Text label = new Text("Testing IMAP");
            BorderPane pane = new BorderPane();
            pane.setCenter(label);
            label.setDisable(true);
            label.setStyle("-fx-font: 16px Helvetica; -fx-fill: mintcream;");
            addCSStoText(pane);
            label.minHeight(100.0);
            label.minWidth(400.0);
            label.prefWidth(400.0);
            label.prefHeight(100.0);
            progressPanel.getChildren().add(pane);
            progressPanel.getChildren().add(progressBar);
            progressPanel.setPrefSize(400, 200);
            progressStage.setScene(new Scene(progressPanel));
            progressStage.setAlwaysOnTop(true);
            progressStage.setTitle("Testing IMAP...");
            service.setOnScheduled(e -> progressStage.show());
            service.setOnSucceeded(e -> {
                System.out.println("INFO Task succedded");
                Label successLabel = new Label("Tested\tTotal Folders: " + totalFolders.getValue() + "\t msgs: " + totalMsgs.getValue());
                successLabel.getStyleClass().add("h2");
                progressPanel.getScene().getWindow().hide();
                progressStage.hide();
                imapPanel.setHeading(successLabel);
                imapPanel.getStyleClass().add("panel-success");
                TextArea ta = new TextArea(imapText);
                ta.setPrefSize(750.00, 345.0);
                ta.setMinSize(600.0, 250);
                ta.setEditable(false);
                listIMAP.setContent(ta);
            });
            service.progressProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                if (!service.isRunning()) {
                    System.out.println("INFO TASK NOT RUNNING");
                    Label successLabel = new Label("new Label(\"\\\"Tested                                      | Total Folders: \" "
                            + "+ totalFolders.getValue() + \"| msgs: \" + totalMsgs.getValue())");
                    progressPanel.getScene().getWindow().hide();
                    progressStage.hide();
                    imapPanel.getStyleClass().add("panel-info");
                    TextArea ta = new TextArea(imapText);
                    ta.setPrefSize(750.00, 345.0);
                    ta.setMinSize(600.0, 250);
                    ta.setEditable(false);
                    listIMAP.setContent(ta);
                }
            });
            service.setOnFailed((WorkerStateEvent event) -> {
                String message = service.getMessage();
                String[] errors = message.split("#");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(errors[0]);//+ e.getLocalizedMessage());
                alert.setContentText("Details: " + errors[1]);// + e.toString());
                alert.showAndWait();
                imapPanel.setHeading(new Label("Weird error :o | Task Failed o.O"));
                imapPanel.getStyleClass().add("panel-success");
                logger.error("No account set error");
            });
            service.setOnCancelled((WorkerStateEvent event) -> {
                String message = service.getMessage();
                String[] errors = message.split("#");
                Label errorLabel = new Label("ERROR while testing, check logs for details.");
                errorLabel.getStyleClass().add("h1");
                imapPanel.setHeading(errorLabel);
                imapPanel.getStyleClass().add("panel-danger");
                TextArea ta = new TextArea(errors[0] + "\n\n" + errors[1]);
                ta.setPrefSize(750.00, 300.0);
                ta.setMinSize(600.0, 250);
                listIMAP.setContent(ta);
                logger.error("ERROR it wasn't possible to connect with IMAP properly\nERROR" + errors[1] + "\n" + errors[0]);
                
            });
            service.messageProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (newValue.contains("#")) {
                    progressPanel.getScene().getWindow().hide();
                    progressStage.hide();
                    service.cancel();
                }
            });
            service.restart();
        }
    }

//private void IMAPTest(){
//    System.out.println("");
//    }
    private class TaskService extends Service<Void> {
        
        @Override
        protected Task<Void> createTask() {
            Task<Void> task = new Task<Void>() {
                
                @Override
                protected Void call() throws Exception {
                    try {
                        Properties props = System.getProperties();
                        props.setProperty("mail.store.protocol", "imaps");
                        props.setProperty("mail.store.protocol", "imaps");
                        props.setProperty("mail.imap.auth.login.disable", "false");
                        props.setProperty("mail.imap.sasl.enable", "true");
                        props.setProperty("mail.imap.sasl.mechanisms", "LOGIN");                        
                        props.setProperty("mail.imap.auth.mechanisms", "LOGIN PLAIN");
                        props.setProperty("mail.imap.starttls.enable", "true");
                        String required = (currentAcc.getImapPort() == 143 ? "true" : "false");
                        props.setProperty("mail.imap.ssl.enable", (currentAcc.getImapPort() == 143 ? "false" : "true"));
                        props.setProperty("mail.imap.starttls.required", required);
                        props.setProperty("mail.imap.ssl.protocols", "TLSv1.2");
                        props.setProperty("mail.imap.connectiontimeout", "2000");
                        props.setProperty("mail.imap.timeout", "5000");
                        //reseting count, in case the user runs again :) 
                        int tMsgs = 0;
                        int tFolders = 0;
                        
                        Session session = Session.getDefaultInstance(props, null);
                        session.setDebug(true);
                        String imapstore = (currentAcc.getImapPort() == 143 ? "imap" : "imaps");
                        logger.debug("IMAP start");
                        Store store = session.getStore(imapstore);
                        logger.debug("attempting to login on " + currentAcc.getImap() + ":" + currentAcc.getImapPort() + "\n with account: " + currentAcc.getEmail() + " pwd: " + currentAcc.getPassword());
                        
                        store.connect(currentAcc.getImap(), currentAcc.getImapPort(), currentAcc.getEmail(), currentAcc.getPassword());
                        logger.debug("DEBUG Connection successful");
                        javax.mail.Folder[] folders = store.getDefaultFolder().list("*");
                        StringBuilder sb = new StringBuilder();
                        for (javax.mail.Folder folder : folders) {
                            if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                                sb.append("folder: ").append(folder.getFullName()).append("\t\t\t\t").append("  msgs: ").append(folder.getMessageCount()).append("\n");
                                logger.debug("folder: " + folder.getFullName() + "  msgs: " + folder.getMessageCount());
                                tFolders++;
                                tMsgs += folder.getMessageCount();
                                updateProgress(tFolders, folders.length);
                            }
                            imapText = sb.toString();
                            totalMsgs.set(tMsgs);
                            totalFolders.setValue(tFolders);
                        }
                        IMAPFolder ff = (IMAPFolder) folders[0];                     
                        ff.open(Folder.READ_ONLY);
                        try {
                            ff.idle(true);
                            logger.info("INFO IDLE command was successful");
                        } catch (MessagingException mse) {
                            logger.error("ERROR server doesn't support IDLE" +mse.getMessage());
                        }
                    } catch (Exception e) {
                        logger.error("ERROR" + e.toString());
                        updateMessage(e.getMessage() + "#" + e.toString());
                        super.failed();
                        boolean a = super.cancel();
                        
                    }
                    return null;
                }
            };
            return task;
        }
    }
    
    private void showErrorDialog(String s, String errorCode) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("SEND ERROR");
        alert.setHeaderText("We couldn't send the message because of " + s.toLowerCase());
        alert.setContentText(errorCode);
        alert.showAndWait();
        imapPanel.getStyleClass().add("panel-danger");
    }

    private void addCSStoText(Pane btn) {
        final String css = "      -fx-background-color: \n"
                + "        linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),\n"
                + "        linear-gradient(#020b02, #3a3a3a),\n"
                + "        linear-gradient(#9d9e9d 0%, #6b6a6b 20%, #343534 80%, #242424 100%),\n"
                + "        linear-gradient(#8a8a8a 0%, #6b6a6b 20%, #343534 80%, #262626 100%),\n"
                + "        linear-gradient(#777777 0%, #606060 50%, #505250 51%, #2a2b2a 100%);\n"
                + "    -fx-background-insets: 0,1,4,5,6;\n"
                + "    -fx-background-radius: 9,8,5,4,3;\n"
                + "    -fx-padding: 15 30 15 30;\n"
                + "    -fx-font-family: \"Helvetica\";\n"
                + "    -fx-font-size: 18px;\n"
                + "    -fx-font-weight: bold;\n"
                + "    -fx-text-fill: white;\n"
                + "    -fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);";        
        btn.setStyle(css);
    }
}
