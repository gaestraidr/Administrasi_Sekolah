/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.gaestra.administrasi_sekolah.App;
import me.gaestra.administrasi_sekolah.helper.DialogAlertHelper;
import me.gaestra.administrasi_sekolah.model.Kelas;
import me.gaestra.administrasi_sekolah.model.Siswa;

/**
 * FXML Controller class
 *
 * @author Ganesha
 */
public class SiswaFormController extends BaseController {

    // Controller Variable Component
    public Siswa selectedModel;
    
    // Core Form Component
    @FXML public JFXListView<Label> menuListView;
    @FXML public TabPane formTabPane;
    @FXML public Tab formTab1;
    @FXML public Tab formTab2;
    
    // Create Form Component
    @FXML public JFXTextField createNis;
    @FXML public JFXTextField createNama;
    @FXML public JFXComboBox createKelas;
    @FXML public JFXComboBox createAgama;
    @FXML public JFXDatePicker createTmptTglLahir;
    @FXML public ToggleGroup createJenisKelamin;
    @FXML public ToggleGroup createBeasiswa;
    @FXML public JFXTextArea createAlamat;
    
    // Manage Form Component
    @FXML public TableView manageTableView;
    @FXML public Label manageSelectedModel;
    
    @FXML public JFXTextField manageNis;
    @FXML public JFXTextField manageNama;
    @FXML public JFXComboBox manageKelas;
    @FXML public JFXComboBox manageAgama;
    @FXML public JFXDatePicker manageTmptTglLahir;
    @FXML public ToggleGroup manageJenisKelamin;
    @FXML public ToggleGroup manageBeasiswa;
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
        Label menu1 = new Label("Manage Siswa");
        Label menu2 = new Label("Tambah Siswa");
        
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
        LinkedList<Kelas> kelas = Kelas.all();
        var itemCol = createKelas.getItems();
        
        if (kelas.size() != 0) 
            for (var data : kelas)
                itemCol.add(data.nama_kelas);
        else itemCol.add("Belum ada Kelas");
        
        createKelas.getSelectionModel().selectFirst();
        
        createAgama.getItems().add("Islam");
        createAgama.getItems().add("Kristen");
        createAgama.getItems().add("Hindu");
        createAgama.getItems().add("Buddha");
        
        createAgama.getSelectionModel().selectFirst();
    }
    
    public void setupManageForm() {
        TableColumn column1 = this.createColumn("NIS", "nis");
        TableColumn column2 = this.createColumn("Nama", "nama");
        TableColumn column3 = this.createColumn("Kelas", "kelas");
        TableColumn column4 = this.createColumn("Agama", "agama");
        TableColumn column5 = this.createColumn("Tempat, Tanggal Lahir", "tmpt_tgl_lahir", 0.15);
        TableColumn column6 = this.createColumn("Jenis Kelamin", "jenis_kelamin");
        TableColumn column7 = this.createColumn("Beasiswa", "beasiswa");
        TableColumn column8 = this.createColumn("Alamat", "alamat", 0.25);
        
        manageTableView.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7, column8);
        
        for (var data : Siswa.all()) {
            manageTableView.getItems().add(data);
        }
        
        LinkedList<Kelas> kelas = Kelas.all();
        var itemCol = manageKelas.getItems();
        
        if (kelas.size() != 0) 
            for (var data : kelas)
                itemCol.add(data.nama_kelas);
        else itemCol.add("Belum ada Kelas");
        
        manageKelas.getSelectionModel().selectFirst();
        
        manageAgama.getItems().add("Islam");
        manageAgama.getItems().add("Kristen");
        manageAgama.getItems().add("Hindu");
        manageAgama.getItems().add("Buddha");
        
        manageAgama.getSelectionModel().selectFirst();
        
        // Listener
        this.textFieldChangeListener(manageNis);
        this.textFieldChangeListener(manageNama);

        manageAlamat.textProperty().addListener((observable, oldValue, newValue) -> { selectedModel.isEdited = true; });
        manageTmptTglLahir.valueProperty().addListener((observable, oldValue, newValue) -> { selectedModel.isEdited = true; });
        manageJenisKelamin.selectedToggleProperty().addListener((observable, oldValue, newValue) -> { selectedModel.isEdited = true; });
        manageBeasiswa.selectedToggleProperty().addListener((observable, oldValue, newValue) -> { selectedModel.isEdited = true; });
        
        // Disable Component
        manageNis.setDisable(true); manageNama.setDisable(true); manageKelas.setDisable(true); manageAgama.setDisable(true); manageAlamat.setDisable(true);
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
        
        Siswa model = new Siswa(createNis.getText(), 
                createNama.getText(), 
                Kelas.get("nama_kelas", createKelas.getValue().toString()).id, 
                createAgama.getValue().toString(), 
                createTmptTglLahir.getValue().toString(), 
                createJenisKelamin.getToggles().indexOf(createJenisKelamin.getSelectedToggle()),
                createBeasiswa.getToggles().indexOf(createBeasiswa.getSelectedToggle()), 
                createAlamat.getText());
        
        model.store();
        
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "SUCCESS: Input Tersimpan", "Input data berhasil di simpan", 
                    "Input data yang di masukan telah tersimpan di database!");
        this.truncateInput();
        this.refreshTable();
    }
    
    @FXML
    public void updateModelData(ActionEvent event) {
        if (selectedModel == null || !selectedModel.isEdited || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Update Data", "Ubah Data", 
                        "Perubahan di Data yang sedang di pilih jika di lanjutkan akan terubah dengan input yang di masukan. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        int index = manageTableView.getItems().indexOf(selectedModel);
        
        selectedModel.nis = manageNis.getText();
        selectedModel.nama = manageNama.getText();
        selectedModel.kelas_id = Kelas.get("nama_kelas", manageKelas.getValue().toString()).id;
        selectedModel.agama = manageAgama.getValue().toString();
        selectedModel.tmpt_tgl_lahir = manageTmptTglLahir.getValue().toString();
        selectedModel.jenis_kelamin = manageJenisKelamin.getToggles().indexOf(manageJenisKelamin.getSelectedToggle());
        selectedModel.beasiswa = manageBeasiswa.getToggles().indexOf(manageBeasiswa.getSelectedToggle());
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
        manageNis.setDisable(false); manageNama.setDisable(false); manageKelas.setDisable(false); manageAgama.setDisable(false); manageAlamat.setDisable(false);
        
        selectedModel = (Siswa)manageTableView.getSelectionModel().getSelectedItem();
        manageSelectedModel.setText("Selected: " + selectedModel.nama);
        
        manageNis.setText(selectedModel.nis);
        manageNama.setText(selectedModel.nama);
        manageAlamat.setText(selectedModel.alamat);
        manageTmptTglLahir.setValue(LocalDate.parse(selectedModel.tmpt_tgl_lahir));
        manageJenisKelamin.getToggles().get(selectedModel.jenis_kelamin).setSelected(true);
        manageBeasiswa.getToggles().get(selectedModel.beasiswa).setSelected(true);
        manageKelas.getSelectionModel().select(selectedModel.Kelas().nama_kelas);
        manageAgama.getSelectionModel().select(selectedModel.agama);
        
        // Trigger back
        selectedModel.isEdited = false;
    }
    
    @FXML
    public void printRekapan() {
        FXMLLoader loader = App.getFXMLLoader("view/RekapanForm");
        RekapanFormController controller = null;
        try {
            Parent layout = (Parent)loader.load();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            popupStage.getIcons().add(App.getIconImage());
            popupStage.setTitle(App.appTitle);
            controller = (RekapanFormController)loader.getController();
            controller.stage = popupStage;
            
            popupStage.setResizable(false);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            
            controller.initRekapanData(new Siswa());
            
            popupStage.show();
            controller.doPrint();
        } catch (Exception e) { e.printStackTrace(); }
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
    
    public void textFieldChangeListener(JFXTextField textfield) {
        textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            selectedModel.isEdited = true;
        });
    }
     
    private boolean verifyInput() {
        if (empty(createNis.getText()) ||
                empty(createNama.getText()) ||
                empty(createAlamat.getText()) ||
                empty(createTmptTglLahir.getValue()))
            return false;
        
        return true;
    }
    
    private void refreshTable() {
        manageTableView.getItems().clear();
        for (var data : Siswa.all()) {
            manageTableView.getItems().add(data);
        }
    }
    
    private void truncateInput() {
        createNis.setText("");
        createNama.setText("");
        createAlamat.setText("");
        createTmptTglLahir.setValue(null);
        createKelas.getSelectionModel().selectFirst();
        createAgama.getSelectionModel().selectFirst();
    }
    
    private void truncateManageInput() {
        manageNis.setText("");
        manageNama.setText("");
        manageAlamat.setText("");
        manageTmptTglLahir.setValue(null);
        manageKelas.getSelectionModel().selectFirst();
        manageAgama.getSelectionModel().selectFirst();
        manageSelectedModel.setText("Selected: None");
        selectedModel = null;
    }
    
    private static boolean empty(Object obj) { 
        if (obj == null)
            return true;
        
        return obj instanceof String && ((String)obj).isEmpty();
    }
}
