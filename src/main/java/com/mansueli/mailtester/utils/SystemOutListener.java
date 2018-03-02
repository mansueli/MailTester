/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.mailtester.utils;

import com.mansueli.mailtester.ResultsController;
import java.io.PrintStream;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Mansueli
 */
public class SystemOutListener {
    private static SystemOutListener singleton;
    private static PrintStream oldPrinter;
    public static SystemOutListener GetSingleton() {
        if (singleton == null) {
            singleton = new SystemOutListener();
            singleton.ReplaceStandartSystemOutPrintStream();
        }
        return singleton;
    }

    private void FireSystemOutPrintln(String message) {
        ResultsController.addToLog(message);
    }

    private void ReplaceStandartSystemOutPrintStream() {
        oldPrinter = (PrintStream)System.err;
        System.setOut(new PrintStream(System.out) {
            @Override
            public void println(String s) {
                FireSystemOutPrintln(s);
                super.println(s);
            }
        });
    }
    public static void noLogPrintf(String s){
        oldPrinter.print(s);
    }
}
