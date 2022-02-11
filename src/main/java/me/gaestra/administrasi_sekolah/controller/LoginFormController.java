/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import me.gaestra.administrasi_sekolah.App;
import me.gaestra.administrasi_sekolah.helper.ControlEventHelper;
import me.gaestra.administrasi_sekolah.model.Staff;

/**
 * FXML Controller class
 *
 * @author Ganesha
 */
public class LoginFormController extends BaseController {

    /**
     * Initializes the controller class.
     */
    
    // Login Form Component
    @FXML public AnchorPane loginForm;
    @FXML public JFXButton loginButton;
    @FXML public JFXTextField loginUsername;
    @FXML public JFXPasswordField loginPassword;
    @FXML public JFXSpinner loginSpinner;
    @FXML public Label loginError;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Do Nothig
    }    
    
    @FXML
    public void doLogin() {
        String username = loginUsername.getText(); String password = loginPassword.getText();
        loginForm.setVisible(false); loginSpinner.setVisible(true); loginPassword.setText("");
        
        CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute(() -> {
            Platform.runLater(() -> {
                if (!this.checkAuth(username, password)) {
                    this.showError("Username / Password salah");
                    return;
                }

                if (username.equals("master")) {
                    App.loggedUser = new Staff();
                    App.loggedUser.id = 0;
                    App.loggedUser.nama = "Master Staff";
                    App.loggedUser.jabatan = "Akun Master";
                    App.loggedUser.username = "master";
                    App.loggedUser.alamat = "-";
                }
                else App.loggedUser = Staff.get("username", username);
                
                ControlEventHelper.showWindow("view/MenuForm");
                ((Stage)loginForm.getScene().getWindow()).close();
            });
        });
    }
    
    @FXML
    public void doExit() {
        CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute(() -> Platform.exit());
    }
    
    private void showError(String str) {
        loginForm.setVisible(true); loginSpinner.setVisible(false);
        loginError.setText(str);
    }
    
    private boolean checkAuth(String username, String password) {
        if (Staff.count() == 0 && (!username.equals("master") || !password.equals("master"))) // If no staff data
            return false;
        else if (Staff.count() > 0 && Staff.get(String.format("username = '%1$s' AND password = '%2$s'", username, password)) == null)
            return false;
        
        return true;
    }
}
