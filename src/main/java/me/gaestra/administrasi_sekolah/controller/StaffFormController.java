/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import me.gaestra.administrasi_sekolah.App;
import me.gaestra.administrasi_sekolah.helper.DialogAlertHelper;
import me.gaestra.administrasi_sekolah.model.Staff;

/**
 * FXML Controller class
 *
 * @author Ganesha
 */
public class StaffFormController extends BaseController {

    // Controller Variable Component
    public Staff selectedModel;
    
    // Core Form Component
    @FXML public JFXListView<Label> menuListView;
    @FXML public TabPane formTabPane;
    @FXML public Tab formTab1;
    @FXML public Tab formTab2;
    
    // Create Form Component
    @FXML public JFXTextField createNama;
    @FXML public JFXTextField createJabatan;
    @FXML public JFXTextField createUsername;
    @FXML public JFXPasswordField createPassword;
    @FXML public JFXTextArea createAlamat;
    
    // Manage Form Component
    @FXML public TableView manageTableView;
    @FXML public Label manageSelectedModel;
    
    @FXML public JFXTextField manageNama;
    @FXML public JFXTextField manageJabatan;
    @FXML public JFXTextField manageUsername;
    @FXML public JFXPasswordField managePassword;
    @FXML public JFXTextArea manageAlamat;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupListView();
        setupCreateForm();
        setupManageForm();
    }
    
    public void setupListView() {
        Label menu1 = new Label("Manage Staff");
        Label menu2 = new Label("Tambah Staff");
        
        menu1.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FILE, "20px"));
        menu2.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PLUS, "20px"));
        
        menuListView.getItems().add(menu1);
        menuListView.getItems().add(menu2);
        
        menuListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Label> observable, Label oldValue, Label newValue) -> {
            String str = newValue.getText();
            SingleSelectionModel<Tab> select = formTabPane.getSelectionModel();
            
            if (str.contains("Manage")) 
                select.select(0);
            else if (str.contains("Tambah"))
                select.select(1);
        });
    }
    
    public void setupCreateForm() {
        
    }
    
    public void setupManageForm() {
        TableColumn column1 = this.createColumn("Nama", "nama", 0.25);
        TableColumn column2 = this.createColumn("Jabatan", "jabatan", 0.25);
        TableColumn column3 = this.createColumn("Username", "username", 0.25);
        TableColumn column4 = this.createColumn("Alamat", "alamat", 0.25);
        
        manageTableView.getColumns().addAll(column1, column2, column3, column4);
        
        // Listener
        this.textFieldChangeListener(manageNama);
        this.textFieldChangeListener(manageJabatan);
        this.textFieldChangeListener(manageUsername);
        this.textFieldChangeListener(managePassword);

        manageAlamat.textProperty().addListener((observable, oldValue, newValue) -> { selectedModel.isEdited = true; });
        
        for (var data : Staff.all())
            manageTableView.getItems().add(data);
        
        // Disable Component
        manageNama.setDisable(true); manageJabatan.setDisable(true); manageUsername.setDisable(true); managePassword.setDisable(true); manageAlamat.setDisable(true);
    }
    
    public TableColumn createColumn(String title, String property) {
        return this.createColumn(title, property, 0.1);
    }
    
    public TableColumn createColumn(String title, String property, double sizeMultiplier) {
        TableColumn column = new TableColumn(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.prefWidthProperty().bind(manageTableView.widthProperty().multiply(sizeMultiplier));
        column.setResizable(false);
        
        return column;
    }
    
    @FXML
    public void addDataToModel(ActionEvent event) {
        if (!this.verifyInput()) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Input Invalid", "Input data Invalid", 
                    "Input data yang di masukan tidak lengkap / invalid, mohon cek kembali!");
            return;
        }
        
        if (!this.isUsernameAvailable(createUsername.getText())) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Input Invalid", "Input data Invalid", 
                    "Username sudah terpakai! Mohon gunakan username yang lain.");
            return;
        }
        
        Staff model = new Staff();
        model.nama = createNama.getText();
        model.jabatan = createJabatan.getText();
        model.username = createUsername.getText();
        model.password = createPassword.getText();
        model.alamat = createAlamat.getText();
        
        model.store();
        
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "SUCCESS: Input Tersimpan", "Input data berhasil di simpan", 
                    "Input data yang di masukan telah tersimpan di database!");
        this.truncateInput();
        this.refreshTable();
    }
    
    @FXML
    public void updateModelData(ActionEvent event) {
        if (!empty(managePassword.getText()) && managePassword.getText().length() < 8) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Input Invalid", "Input data Invalid", 
                    "Password tidak boleh kurang dari 8 karakter!");
            return;
        }
        
        if (!this.isUsernameAvailable(manageUsername.getText())) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Input Invalid", "Input data Invalid", 
                    "Username sudah terpakai! Mohon gunakan username yang lain.");
            return;
        }
        
        if (selectedModel == null || !selectedModel.isEdited || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Update Data", "Ubah Data", 
                        "Perubahan di Data yang sedang di pilih jika di lanjutkan akan terubah dengan input yang di masukan. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        int index = manageTableView.getItems().indexOf(selectedModel);
        
        selectedModel.nama = manageNama.getText();
        selectedModel.jabatan = manageJabatan.getText();
        selectedModel.username = manageUsername.getText();
        if (!empty(managePassword.getText()) && managePassword.getText().length() > 8)
            selectedModel.password = managePassword.getText();
        selectedModel.alamat = manageAlamat.getText();
        
        selectedModel.update();
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Data Changed", "Data berhasil di ubah", "Data telah berhasil di ubah dan tersimpan di database!");
        selectedModel.isEdited = false;
        manageTableView.getItems().set(index, selectedModel);
    }
    
    @FXML
    public void deleteModelData(ActionEvent event) {
        if (selectedModel == null || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Delete Data", "Hapus Data", 
                        "Data yang terpilih jika di lanjutkan akan di hapus dari database. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        selectedModel.delete();
        manageTableView.getItems().remove(selectedModel);
        this.truncateManageInput();
    }
    
    @FXML
    public void itemClicked(MouseEvent event) {
        if (event.getClickCount() != 2 || manageTableView.getItems().size() <= 0) //Checking double click
            return;
        
        if (selectedModel != null && selectedModel.isEdited && 
                !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Discard Change", "Buang perubahan", 
                        "Data yang sedang di pilih telah di edit dan belum di save, jika melanjutkan perubahan akan di buang. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        if (selectedModel != null) 
            selectedModel.isEdited = false;
        
        // Enable Component
        manageNama.setDisable(false); manageJabatan.setDisable(false); manageUsername.setDisable(false); managePassword.setDisable(false); manageAlamat.setDisable(false);
        
        selectedModel = (Staff)manageTableView.getSelectionModel().getSelectedItem();
        manageSelectedModel.setText("Selected: " + selectedModel.nama);
        
        manageNama.setText(selectedModel.nama);
        manageJabatan.setText(selectedModel.jabatan);
        manageUsername.setText(selectedModel.username);
        manageAlamat.setText(selectedModel.alamat);
        managePassword.setText("");
        
        // Trigger back
        selectedModel.isEdited = false;
    }
    
    @FXML
    public void inputChanged(ActionEvent event) {
        selectedModel.isEdited = true;
    }
    
    @FXML
    public void backToMenu(ActionEvent event) {
        if (selectedModel != null && selectedModel.isEdited && !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Back to Menu", "Kembali ke Menu", 
                        "Perubahan di Data yang sedang di pilih jika di lanjutkan akan terubah dengan input yang di masukan. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        stage.close();
    }
    
    public void textFieldChangeListener(TextField textfield) {
        textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            selectedModel.isEdited = true;
        });
    }
     
    private boolean verifyInput() {
        if (empty(createNama.getText()) ||
                empty(createJabatan.getText()) ||
                empty(createUsername.getText()) ||
                empty(createPassword.getText()) || createPassword.getText().length() < 8 ||
                empty(createAlamat.getText()))
            return false;
        
        return true;
    }
    
    private boolean isUsernameAvailable(String str) {
        return Staff.get("username", str) == null;
    }
    
    private void refreshTable() {
        manageTableView.getItems().clear();
        for (var data : Staff.all()) {
            manageTableView.getItems().add(data);
        }
    }
    
    private void truncateInput() {
        createNama.setText("");
        createJabatan.setText("");
        createUsername.setText("");
        createPassword.setText("");
        createAlamat.setText("");
    }
    
    private void truncateManageInput() {
        manageNama.setText("");
        manageJabatan.setText("");
        manageUsername.setText("");
        managePassword.setText("");
        manageAlamat.setText("");
        manageSelectedModel.setText("Selected: None");
        
        selectedModel = null;
        // Disable Component
        manageNama.setDisable(true); manageJabatan.setDisable(true); manageUsername.setDisable(true); managePassword.setDisable(true); manageAlamat.setDisable(true);
    }
    
    private static boolean empty(Object obj) { 
        if (obj == null)
            return true;
        
        return obj instanceof String && ((String)obj).isEmpty();
    }
}
