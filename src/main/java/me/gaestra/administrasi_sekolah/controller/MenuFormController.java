/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.gaestra.administrasi_sekolah.helper.ControlEventHelper;
import me.gaestra.administrasi_sekolah.helper.ControlTransformHelper;

/**
 * FXML Controller class
 *
 * @author Ganesha
 */
public class MenuFormController extends BaseController {
    
    // Main Component
    @FXML public Label menuTimestamp;
    public boolean toggleHandler = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initTimestamp();
    }
    
    private void initTimestamp() {
        final Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.millis( 500 ),
                event -> {
                    menuTimestamp.setText(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT).format(LocalDateTime.now()));
                    menuTimestamp.setTextFill(!toggleHandler ? Color.web("#aba7a7") : Color.web("#ffffff"));
                    toggleHandler = !toggleHandler;
                }
            )
        );
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
    }
    
    @FXML
    public void hoverAnimation(Event event) {
        ControlTransformHelper.transitionFillColor((Control)event.getSource());
    }
    
    @FXML
    public void menuGoTo(ActionEvent event) {
        CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS).execute(() -> {
            Platform.runLater(() -> {
                try {
                    String str = ((JFXButton)event.getSource()).getText();
                    if (str.contains("Siswa"))
                        ControlEventHelper.showWindow("view/SiswaForm");
                    else if (str.contains("Tunjangan"))
                        ControlEventHelper.showWindow("view/TunjanganForm");
                    else if (str.contains("Staff"))
                        ControlEventHelper.showWindow("view/StaffForm");
                    else if (str.contains("Nota"))
                        ControlEventHelper.showWindow("view/NotaForm");
                    else if (str.contains("Kelas"))
                        ControlEventHelper.showWindow("view/KelasForm");
                } catch (Exception e) {}
            });
        });
    }
    
    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }
    
}
