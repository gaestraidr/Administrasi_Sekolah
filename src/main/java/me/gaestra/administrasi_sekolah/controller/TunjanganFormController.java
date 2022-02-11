/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.gaestra.administrasi_sekolah.ActionButtonTableCell;
import me.gaestra.administrasi_sekolah.App;
import me.gaestra.administrasi_sekolah.helper.DialogAlertHelper;
import me.gaestra.administrasi_sekolah.helper.UtilityMiscHelper;
import me.gaestra.administrasi_sekolah.model.Kelas;
import me.gaestra.administrasi_sekolah.model.Nota;
import me.gaestra.administrasi_sekolah.model.Siswa;
import me.gaestra.administrasi_sekolah.model.Tunjangan;

/**
 * FXML Controller class
 *
 * @author Ganesha
 */
public class TunjanganFormController extends BaseController {

    // Controller Variable Component
    public Tunjangan selectedModel;
    public Siswa selectedSiswa;
    
    // Core Form Component
    @FXML public JFXListView<Label> menuListView;
    @FXML public TabPane formTabPane;
    @FXML public Tab formTab1;
    @FXML public Tab formTab2;
    
    // Create Form Component
    @FXML public JFXTextField createNama;
    @FXML public JFXTextField createNominal;
    @FXML public JFXComboBox createMode;
    @FXML public JFXComboBox createSpecified;
    @FXML public JFXDatePicker createFirstDate;
    @FXML public JFXDatePicker createSecondDate;
    
    // Manage Form Component
    @FXML public TableView manageTableView;
    @FXML public Label manageSelectedModel;
    
    @FXML public JFXTextField manageNama;
    @FXML public JFXTextField manageNominal;
    @FXML public JFXComboBox manageMode;
    @FXML public JFXComboBox manageSpecified;
    @FXML public JFXDatePicker manageFirstDate;
    @FXML public JFXDatePicker manageSecondDate;
    
    // Manage Siswa Tunjangan Form Component
    @FXML public TableView manageSiswaTunjanganTableView;
    
    @FXML public JFXComboBox manageSiswa;
    @FXML public JFXComboBox manageTipeTunjangan;
    @FXML public JFXDatePicker manageIssuedDate;
    @FXML public JFXDatePicker manageGraceDate;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupListView();
        setupCreateForm();
        setupManageForm();
        setupManageSiswaTunjangan();
    }
    
    public void setupListView() {
        Label menu1 = new Label("Manage Tunjangan");
        Label menu2 = new Label("Tambah Tunjangan");
        Label menu3 = new Label("Manage Tunjangan Siswa");
        
        menu1.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FILE, "20px"));
        menu2.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PLUS, "20px"));
        menu3.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.VCARD, "20px"));
        
        menuListView.getItems().add(menu1);
        menuListView.getItems().add(menu2);
        menuListView.getItems().add(menu3);
        
        menuListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Label> observable, Label oldValue, Label newValue) -> {
            String str = newValue.getText();
            SingleSelectionModel<Tab> select = formTabPane.getSelectionModel();
            
            if ("Manage Tunjangan".equals(str)) 
                select.select(0);
            else if ("Tambah Tunjangan".equals(str))
                select.select(1);
            else if ("Manage Tunjangan Siswa".equals(str))
                select.select(2);
        });
    }
    
    public void setupCreateForm() {
        createMode.getItems().add("Tanpa Periode");
        createMode.getItems().add("Perbulan - Semua");
        createMode.getItems().add("Pertahun - Semua");
        createMode.getItems().add("Pada Tanggal - Semua");
        createMode.getItems().add("Perbulan - Kelas");
        createMode.getItems().add("Pertahun - Kelas");
        createMode.getItems().add("Pada Tanggal - Kelas");
        
        createMode.getSelectionModel().selectFirst();
        
        createNominal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            createNominal.setText(UtilityMiscHelper.formatCurrency(newValue));
        });
        
        createSpecified.setDisable(true); createFirstDate.setDisable(true); createSecondDate.setDisable(true);
    }
    
    public void setupManageForm() {
        TableColumn column1 = this.createColumn("ID", "id");
        TableColumn column2 = this.createColumn("Nama Tunjangan", "nama_tunjangan", 0.15);
        TableColumn column3 = this.createColumn("Nominal Tunjangan", "nominal_tunjangan", 0.15);
        TableColumn column4 = this.createColumn("Mode Periode", "periodic_mode", 0.15);
        TableColumn column5 = this.createColumn("Spesifik Target", "specific_id", 0.15);
        TableColumn column6 = this.createColumn("Dari Tanggal", "first_date");
        TableColumn column7 = this.createColumn("Sampai Tanggal", "second_date", 0.15);
        
        manageTableView.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7);
        
        for (var data : Tunjangan.all()) {
            manageTableView.getItems().add(data);
        }
        
        manageMode.getItems().add("Tanpa Periode");
        manageMode.getItems().add("Perbulan - Semua");
        manageMode.getItems().add("Pertahun - Semua");
        manageMode.getItems().add("Pada Tanggal - Semua");
        manageMode.getItems().add("Perbulan - Kelas");
        manageMode.getItems().add("Pertahun - Kelas");
        manageMode.getItems().add("Pada Tanggal - Kelas");
        
        manageMode.getSelectionModel().selectFirst();
        
        // Listener
        this.textFieldChangeListener(manageNama);
        manageNominal.textProperty().addListener((observable, oldValue, newValue) -> {
            selectedModel.isEdited = true;
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            manageNominal.setText(UtilityMiscHelper.formatCurrency(newValue));
        });

        manageFirstDate.valueProperty().addListener((observable, oldValue, newValue) -> { selectedModel.isEdited = true; });
        manageSecondDate.valueProperty().addListener((observable, oldValue, newValue) -> { selectedModel.isEdited = true; });
        
        // Disable Component
        manageNama.setDisable(true); manageNominal.setDisable(true); manageMode.setDisable(true); manageSpecified.setDisable(true);
        manageFirstDate.setDisable(true); manageSecondDate.setDisable(true);
    }
    
    public void setupManageSiswaTunjangan() {
        TableColumn column1 = this.createColumn("Nama Tunjangan", "nama_tunjangan", 0.15);
        TableColumn column2 = this.createColumn("Tanggal Pengisuan", "issued_date", 0.15);
        TableColumn column3 = this.createColumn("Tanggal Tenggang", "grace_date", 0.15);
        
        // Remove button
        TableColumn column4 = new TableColumn("Hapus");
        column4.setCellFactory(ActionButtonTableCell.<Tunjangan>forTableColumn("Hapus", "bg-danger, text-white", FontAwesomeIcon.TRASH, (Tunjangan tunj) -> {
            if (!DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Delete Data", "Hapus Data", 
                        "Data yang terpilih jika di lanjutkan akan di hapus dari database. Lanjut?", "Lanjutkan", "Tidak"))
                return tunj;
            
            selectedSiswa.tunjangan.remove(tunj);
            selectedSiswa.updateTunjangan();
            manageSiswaTunjanganTableView.getItems().remove(tunj);
            return tunj;
        }));    
        
        manageSiswaTunjanganTableView.getColumns().addAll(column1, column2, column3, column4);
        manageSiswaTunjanganTableView.setRowFactory(tv -> new TableRow<Tunjangan>() {
            @Override
            protected void updateItem(Tunjangan item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !item.grace_date.isEmpty()) {
                    LocalDate graceDate = UtilityMiscHelper.formatToDate(item.grace_date);
                    Nota buktiPemp = Nota.get("siswa_id = " + Siswa.get("nama" , item.nama_siswa).id + " AND tunjangan_id = '"  + item.id + "' AND created_at >= " + item.issued_date);

                    if (LocalDate.now().isAfter(graceDate) && buktiPemp == null) {
                        this.getStyleClass().add("table-row-red");
                    }
                    else this.getStyleClass().clear();
                }
                this.getTableView().refresh();
            }
        });
        
        for (var data : Siswa.all()) 
            manageSiswa.getItems().add(data.nama + " - " + data.nis);
        
        for (var data : Tunjangan.all())
            manageTipeTunjangan.getItems().add(data.nama_tunjangan);
        
        // Disable Component
        manageTipeTunjangan.setDisable(true); manageIssuedDate.setDisable(true); manageGraceDate.setDisable(true);
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
        
        Tunjangan model = new Tunjangan();
        model.nama_tunjangan = createNama.getText();
        model.nominal_tunjangan = UtilityMiscHelper.formatToNumber(createNominal.getText());
        model.periodic_mode = createMode.getItems().indexOf(createMode.getValue().toString());
        model.specific_id = (!createSpecified.isDisable() ? Kelas.get("nama_kelas", createSpecified.getValue().toString()).id : 0 );
        model.first_date = (!createFirstDate.isDisable() ? createFirstDate.getValue().toString() : DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()));
        model.second_date = (!createSecondDate.isDisable() ? createSecondDate.getValue().toString() : DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()));
        
        model.store();
        
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "SUCCESS: Input Tersimpan", "Input data berhasil di simpan", 
                    "Input data yang di masukan telah tersimpan di database!");
        this.truncateInput();
        this.refreshTable();
        this.repopulateDataTunjangan();
    }
    
    @FXML
    public void updateModelData(ActionEvent event) {
        if (selectedModel == null || !selectedModel.isEdited || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Update Data", "Ubah Data", 
                        "Perubahan di Data yang sedang di pilih jika di lanjutkan akan terubah dengan input yang di masukan. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        int index = manageTableView.getItems().indexOf(selectedModel);
        
        selectedModel.nama_tunjangan = manageNama.getText();
        selectedModel.nominal_tunjangan = UtilityMiscHelper.formatToNumber(manageNominal.getText());
        selectedModel.periodic_mode = manageMode.getItems().indexOf(manageMode.getValue().toString());
        selectedModel.specific_id = (!manageSpecified.isDisable() ? Kelas.get("nama_kelas", manageSpecified.getValue().toString()).id : 0);
        selectedModel.first_date = (!manageFirstDate.isDisable() ? manageFirstDate.getValue().toString() : DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()));
        selectedModel.second_date = (!manageSecondDate.isDisable() ? manageSecondDate.getValue().toString() : DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()));
        
        selectedModel.update();
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Data Changed", "Data berhasil di ubah", "Data telah berhasil di ubah dan tersimpan di database!");
        selectedModel.isEdited = false;
        manageTableView.getItems().set(index, selectedModel);
        this.repopulateDataTunjangan();
    }
    
    @FXML
    public void deleteModelData(ActionEvent event) {
        if (selectedModel == null || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Delete Data", "Hapus Data", 
                        "Data yang terpilih jika di lanjutkan akan di hapus dari database. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        selectedModel.delete();
        manageTableView.getItems().remove(selectedModel);
        this.truncateManageInput();
        this.repopulateDataTunjangan();
    }
    
    @FXML
    public void storeOrUpdateTunjanganSiswa(ActionEvent event) {
        if (selectedSiswa == null || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Insert Data", "Masukan Data", 
                        "Data yang di input, jika di lanjutkan akan di masukan ke dalam database. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        Tunjangan tunj = Tunjangan.get("nama_tunjangan", manageTipeTunjangan.getValue().toString());
        tunj.issued_date = manageIssuedDate.getValue().toString();
        tunj.grace_date = manageGraceDate.getValue().toString();
        
        selectedSiswa.tunjangan.add(tunj);
        manageTableView.getItems().add(tunj);
        
        selectedSiswa.updateTunjangan();
        
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Data Changed", "Data berhasil di tambah", "Data telah berhasil di ubah / di tambahkan database!");
        this.refreshTableSiswaTunjangan();
    }
    
    @FXML
    public void changeAccordingToMode(ActionEvent event) {
        int manage = manageMode.getItems().indexOf(manageMode.getValue().toString());
        int create = createMode.getItems().indexOf(createMode.getValue().toString());
        if (((Node)event.getSource()).getId().contains("manage")) {
            this.populateComboBox(manageSpecified, manageFirstDate, manageSecondDate, manage);
            selectedModel.isEdited = true;
        }
        else this.populateComboBox(createSpecified, createFirstDate, createSecondDate, create);
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
        
        // Enable component
        manageNama.setDisable(false); manageNominal.setDisable(false); manageMode.setDisable(false);
        
        selectedModel = (Tunjangan)manageTableView.getSelectionModel().getSelectedItem();
        manageSelectedModel.setText("Selected: " + selectedModel.nama_tunjangan);
        
        manageNama.setText(selectedModel.nama_tunjangan);
        manageNominal.setText(UtilityMiscHelper.formatCurrency(String.valueOf(selectedModel.nominal_tunjangan)));
        
        manageMode.getSelectionModel().select(selectedModel.periodic_mode);
        this.populateComboBox(manageSpecified, manageFirstDate, manageSecondDate, selectedModel.periodic_mode);
        
        if (!manageSpecified.isDisable())
            manageSpecified.getSelectionModel().select(((Kelas)selectedModel.Specified()).nama_kelas);
        
        manageFirstDate.setValue(LocalDate.parse(selectedModel.first_date));
        manageSecondDate.setValue(LocalDate.parse(selectedModel.second_date));
        
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
    
    @FXML
    public void siswaChanged(ActionEvent event) {
        // Enable Component
        manageTipeTunjangan.setDisable(false); manageIssuedDate.setDisable(false); manageGraceDate.setDisable(false);
        
        selectedSiswa = Siswa.get("nama", manageSiswa.getValue().toString().split("-")[0].trim());
        
        manageSiswaTunjanganTableView.getItems().clear();
        
        for (var data : selectedSiswa.tunjangan)
            manageSiswaTunjanganTableView.getItems().add(data);
        
        this.truncateTunjanganSiswaInput();
    }
    
    @FXML
    public void printRekapanTunjangan() {
        printRekapan(1);
    }
    
    @FXML
    public void printRekapanTunjanganSiswa() {
        printRekapan(2);
    }
    
    public void textFieldChangeListener(JFXTextField textfield) {
        textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            selectedModel.isEdited = true;
        });
    }
    
    private void printRekapan(int optionMode) {
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
            
            controller.initRekapanData(new Tunjangan(), optionMode);
            
            popupStage.show();
            controller.doPrint();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void populateComboBox(JFXComboBox box, JFXDatePicker first, JFXDatePicker second, int mode) {
        box.getItems().clear(); first.setDisable(true); second.setDisable(true); box.setDisable(true);
        switch(mode) {
            case Tunjangan.PeriodicMode.MONTHLY_SPECIFIED:
            case Tunjangan.PeriodicMode.YEARLY_SPECIFIED:
            case Tunjangan.PeriodicMode.ONDATE_SPECIFIED:
            {
                second.setDisable(false);
                first.setDisable(false);
                
                box.setDisable(false);
                for (var data : Kelas.all()) {
                    box.getItems().add(data.nama_kelas);
                }
                box.getSelectionModel().selectFirst();
            }
                break;
            case Tunjangan.PeriodicMode.MONTHLY:
            case Tunjangan.PeriodicMode.YEARLY:
            case Tunjangan.PeriodicMode.ONDATE:
                second.setDisable(false);
                first.setDisable(false);
                break;
            case Tunjangan.PeriodicMode.NONE:
            default:
                break;
        }
    }
     
    private boolean verifyInput() {
        if (empty(createNama.getText()) || 
                (!createFirstDate.isDisabled() && createFirstDate.getValue().toString().isEmpty()) ||
                (!createSecondDate.isDisabled() && createSecondDate.getValue().toString().isEmpty()) ||
                empty(createNominal.getText()))
            return false;
        
        return true;
    }
    
    private void refreshTable() {
        manageTableView.getItems().clear();
        for (var data : Tunjangan.all()) {
            manageTableView.getItems().add(data);
        }
    }
    
    private void refreshTableSiswaTunjangan() {
        this.siswaChanged(null);
    }
    
    private void repopulateDataTunjangan() {
        manageTipeTunjangan.getItems().clear();
        for (var data : Tunjangan.all())
            manageTipeTunjangan.getItems().add(data.nama_tunjangan);
        
        this.truncateTunjanganSiswaInput();
    }
    
    private void truncateInput() {
        createNama.setText("");
        createNominal.setText("");
        createMode.getSelectionModel().selectFirst();
        createSpecified.getItems().clear();
        createFirstDate.setValue(null);
        createSecondDate.setValue(null);
        createSpecified.setDisable(true); createFirstDate.setDisable(true); createSecondDate.setDisable(true);
    }
    
    private void truncateManageInput() {
        manageNama.setText("");
        manageNominal.setText("");
        manageMode.getSelectionModel().selectFirst();
        manageSpecified.getItems().clear();
        manageFirstDate.setValue(null);
        manageSecondDate.setValue(null);
        manageSelectedModel.setText("Selected: None");
        selectedModel = null;
    }
    
    private void truncateTunjanganSiswaInput() {
        manageTipeTunjangan.getSelectionModel().selectFirst();
        manageIssuedDate.setValue(null);
        manageGraceDate.setValue(null);
    }
    
    private static boolean empty(Object obj) { 
        if (obj == null)
            return true;
        
        return obj instanceof String && ((String)obj).isEmpty();
    }
}
