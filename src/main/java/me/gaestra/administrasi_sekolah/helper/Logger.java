/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Alert;

/**
 *
 * @author Ganesha
 */
public class Logger {
    
    private static final String LogFile = "log_" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()) + ".log";
    private static final String LogFolder = "logs";
    
    public static void info(String content) {
        logToFile(LogVerbose.Info, content);
    }
    
    public static void error(String content) {
        logToFile(LogVerbose.Error, content);
    }
    
    public static void debug(String content) {
        logToFile(LogVerbose.Debug, content);
    }
    
    public static void log(String mode, String content) {
        logToFile(mode, content);
    }
    
    private static void logToFile(String mode, String content) {
        // New Directory
        new File("." + File.separator + LogFolder).mkdirs();
        
        try(FileWriter fw = new FileWriter(LogFolder + File.separator + LogFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(String.format("[LOG] [%1$s] [%2$s]: %3$s", mode,
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()), content));
        } catch (IOException e) {
            StringWriter errors = new StringWriter(); e.printStackTrace(new PrintWriter(errors));
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "Error: Append Logging", "Error saat memasukan log data.", 
                    "Terjadi kesalahan saat memasukan log data ke file:\r\n\r\n" + errors.toString());
        }
    }
    
    public interface LogVerbose {
        public static final String Error = "E";
        public static final String Info = "I";
        public static final String Debug = "D";
    }
}
