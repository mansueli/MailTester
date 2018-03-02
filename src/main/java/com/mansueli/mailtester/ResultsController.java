/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mansueli
 */
public class ResultsController implements Initializable {

    private static ArrayList<String> log = new ArrayList<>();

    public static void addToLog(String s) {
        if (log.size() > 1000) {
            resetLog();
        }
        log.add(s);
    }

    public static void resetLog() {
        log = new ArrayList<>();
    }

    @FXML
    private ScrollPane webPane;
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ResultsController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parseLog();
    }

    private void parseLog() {
        webPane.setContent(browser);
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n").append("<html lang=\"en-US\">").append("<head>")
                .append("<meta charset=utf-8>\n" + "<title>results</title>").append("</head>\n<body>");
        for (String s : log) {
//            SystemOutListener.noLogPrintf("logger:"+styledLog(s));
            sb.append(styledLog(s));
        }
        sb.append("</body>\n</html>");
        String pageSource = sb.toString();
        webEngine.loadContent(pageSource);
//        webEngine.reload();
    }

    private static String styledLog(String log) {
        String style = "";
        // IMAP & SMTP codes
        if (Character.isDigit(log.charAt(0))||log.charAt(0)=='*') {
            style += "font-family: 'Courier'; color: #0059B2;"; //"-fx-fill: #0059B2;";
        }
        else if (log.toLowerCase().indexOf("info")!= -1){
             style += "font-family: 'Courier'; color: #AAAAAA;"; //"-fx-fill: #B28500;";   
        }
        else if (log.toLowerCase().indexOf("deb") != -1){
            style += "font-family: 'Courier'; color: #B28500;"; //"-fx-fill: #B28500;";        
        }
        else if ((log.toLowerCase().indexOf("excep") != -1)||(log.toLowerCase().indexOf("err") != -1)) {
            style += "font-family: 'Courier'; color: #DD5025;"; //"-fx-fill: #DD5025;";
        }else{
            style += "font-family: 'Courier';";
        }
        StringBuilder sb = new StringBuilder();
        String styled = sb.append("<p style=\"").append(style).append("\">").append(log).append("</p>").toString();
        return styled;
    }
}
