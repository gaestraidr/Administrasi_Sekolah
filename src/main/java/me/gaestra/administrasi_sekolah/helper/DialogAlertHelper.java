/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.helper;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Ganesha
 */
public class DialogAlertHelper {
    
    public static boolean showDialogPrompted(Alert.AlertType type, String title, String header, String content, String yes, String no) {
        ButtonType onYes = new ButtonType(yes, ButtonBar.ButtonData.OK_DONE);
        ButtonType onNo = new ButtonType(no, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(type,
                content,
                onYes,
                onNo);

        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();

        return result.orElse(onNo) == onYes;
    }
    
    public static void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
}
