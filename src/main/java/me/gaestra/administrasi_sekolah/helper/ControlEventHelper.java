/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.gaestra.administrasi_sekolah.App;
import me.gaestra.administrasi_sekolah.controller.BaseController;

/**
 *
 * @author Ganesha
 */
public class ControlEventHelper {
    
    public static BaseController showWindow(String fxml) {
        FXMLLoader loader = App.getFXMLLoader(fxml);
        BaseController controller = null;
        try {
            Parent layout = (Parent)loader.load();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            popupStage.getIcons().add(App.getIconImage());
            popupStage.setTitle(App.appTitle);
            controller = (BaseController)loader.getController();
            controller.stage = popupStage;
            
            popupStage.setResizable(false);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            popupStage.show();
            
        } catch (Exception e) { e.printStackTrace(); }
        return controller;
    }
}
