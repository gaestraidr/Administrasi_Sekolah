/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class NotaFormController extends BaseController {

    
    // Core Variable
    public Siswa selectedSiswa;
    
    // Create Form Component
    @FXML public TableView createTableView;
    @FXML public JFXComboBox createSelectSiswa;
    @FXML public Label createKelas;
    @FXML public Label createAgama;
    @FXML public Label createTmptTglLahir;
    @FXML public Label createJenisKelamin;
    @FXML public Label createAlamat;
    @FXML public Label createBeasiswa;
    @FXML public JFXComboBox createTipeTunjangan;
    @FXML public Label createTotalTagihan;
    @FXML public Label createStaff;
    
    // Manage Form Component
    @FXML public TableView manageTableView;
    
    @FXML public JFXComboBox manageTipeSearch;
    @FXML public JFXComboBox manageSelectModel;
    @FXML public JFXTextField manageSearchId;
    @FXML public JFXDatePicker manageTanggalNota;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupCreateForm();
        setupManageForm();
    }
    
    public void setupCreateForm() {
        TableColumn column1 = this.createColumn("Nama Tunjangan", "nama_tunjangan", createTableView, 0.25);
        TableColumn column2 = this.createColumn("Nominal", "nominal_tunjangan", createTableView, 0.25);
        TableColumn column3 = this.createColumn("Tanggal Pengisuan", "issued_date", createTableView, 0.25);
        TableColumn column4 = this.createColumn("Tanggal Tenggang", "grace_date", createTableView, 0.25);
        
        createTableView.getColumns().addAll(column1, column2, column3, column4);
        
        createTableView.setRowFactory(tv -> new TableRow<Tunjangan>() {
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
            createSelectSiswa.getItems().add(data.nama + " - " + data.nis);

        for (var data : Tunjangan.all())
            createTipeTunjangan.getItems().add(data.nama_tunjangan);
        
        createTipeTunjangan.getSelectionModel().selectFirst();
        createStaff.setText(App.loggedUser.nama);
    }
    
    public void setupManageForm() {
        manageTipeSearch.getItems().add("Kelas");
        manageTipeSearch.getItems().add("Siswa");
        manageTipeSearch.getItems().add("Tunjangan");
        manageTipeSearch.getItems().add("ID Nota");
        
        TableColumn column1 = this.createColumn("ID Nota", "id", manageTableView, 0.15);
        TableColumn column2 = this.createColumn("Nama Siswa", "siswa_name", manageTableView, 0.35);
        TableColumn column3 = this.createColumn("Tanggal Transaksi", "created_at", manageTableView, 0.25);
        TableColumn column4 = new TableColumn("Cetak");
        column4.prefWidthProperty().bind(manageTableView.widthProperty().multiply(0.25));
        
        column4.setCellFactory(ActionButtonTableCell.<Nota>forTableColumn("Cetak", "bg-primary, text-white", FontAwesomeIcon.PRINT, (Nota nota) -> {
            this.showCetakPopup(nota);
            return nota;
        }));
        
        manageTableView.getColumns().addAll(column1, column2, column3, column4);
        
        // Disable component
        manageSelectModel.setDisable(true); manageSearchId.setDisable(true);
        
        // Listener
        manageSearchId.textProperty().addListener((obs, oldval, newval) -> {searchModelUsed(null);});
    }
    
    public void showCetakPopup(Nota nota) {
        try {
            FXMLLoader loader = App.getFXMLLoader("view/CetakForm");
            Parent layout = (Parent)loader.load();
            CetakFormController popupController = (CetakFormController)loader.getController();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            popupStage.getIcons().add(App.getIconImage());
            popupStage.setTitle("Cetak Nota: " + nota.getId() + " | " + nota.Siswa().nama);
            
            popupStage.setResizable(false);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            popupStage.show();
            
            popupController.stage = popupStage;
            popupController.notaData(nota);
            popupController.initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            
            controller.initRekapanData(new Nota());
            
            popupStage.show();
            controller.doPrint();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @FXML
    public void storeNota(ActionEvent event) {
        if (createTableView.getItems().size() <= 0) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "No Tunjangan", "Tidak ada Tunjangan", 
                    "Siswa ini tidak mempunyai tunjangan yang di pilih. Tambahkan manual di Menu Data Tunjangan");
            return;
        }
        
        Nota model = new Nota();
        model.staff_id = 0;
        model.siswa_id = selectedSiswa.id;
        model.tunjangan_id = String.valueOf(Tunjangan.get("nama_tunjangan", createTipeTunjangan.getValue().toString()).id);
        model.created_at = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        model.updated_at = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        model.store();
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Nota Saved", "Nota Tersimpan", 
                    "Nota telah berhasil di simpan di database!");
    }
    
    @FXML
    public void searchTypeChanged(ActionEvent event) {
        manageSelectModel.getItems().clear(); manageSelectModel.setDisable(true); manageSearchId.setDisable(true);
        String sel = manageTipeSearch.getValue().toString(); 
        if (sel.equals("Kelas")) {
            manageSelectModel.setDisable(false);
            for (var data : Kelas.all())
                manageSelectModel.getItems().add(data.nama_kelas);
        }
        else if (sel.equals("Siswa")) {
            manageSelectModel.setDisable(false);
            for (var data : Siswa.all())
                manageSelectModel.getItems().add(data.nama + " - " + data.nis);
        }
        else if (sel.equals("Tunjangan")) {
            manageSelectModel.setDisable(false);
            for (var data : Tunjangan.all())
                manageSelectModel.getItems().add(data.nama_tunjangan);
        }
        else if (sel.equals("ID Nota")) {
            manageSearchId.setDisable(false);
        }
        manageTableView.getItems().clear();
    }
    
    @FXML
    public void searchModelUsed(ActionEvent event) {
        String sel = manageTipeSearch.getValue().toString(); manageTableView.getItems().clear();
        for (var data : Nota.all()) {
            try {
                if (data.Siswa() == null || 
                        manageTanggalNota.getValue() != null && !data.created_at.split(" ")[0].equals(manageTanggalNota.getValue().toString()))
                    continue;

                if (sel.equals("ID Nota")) {
                    if (!manageSearchId.getText().isEmpty() && data.getId().contains(manageSearchId.getText()))
                        manageTableView.getItems().add(data);
                }
                else { // Model selected
                    if (manageSelectModel.getValue() == null)
                        break;

                    String selModel = manageSelectModel.getValue().toString();

                    if (sel.equals("Kelas") && data.Siswa().Kelas().nama_kelas.equals(selModel)) {
                        manageTableView.getItems().add(data);
                    }
                    else if (sel.equals("Siswa") && data.Siswa().nama.equals(selModel.split("-")[0].trim())) {
                        manageTableView.getItems().add(data);
                    }
                    else if (sel.equals("Tunjangan") && data.Tunjangan().nama_tunjangan.equals(selModel)) {
                        manageTableView.getItems().add(data);
                    }
                }
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }
    } 
    
    @FXML
    public void siswaChanged(ActionEvent event) {
        selectedSiswa = Siswa.get("nama", createSelectSiswa.getValue().toString().split("-")[0].trim());
        createKelas.setText(selectedSiswa.Kelas().nama_kelas);
        createAgama.setText(selectedSiswa.agama);
        createTmptTglLahir.setText(selectedSiswa.tmpt_tgl_lahir);
        createJenisKelamin.setText(selectedSiswa.getJenis_kelamin());
        createAlamat.setText(selectedSiswa.alamat);
        createBeasiswa.setText(selectedSiswa.beasiswa == 0 ? "Ya, Diskon 50%" : "Tidak, Diskon 0%");
        
        this.tipeTunjanganChanged(null);
    }
    
    @FXML 
    public void tipeTunjanganChanged(ActionEvent event) {
        int nominal = 0; String selTunj = createTipeTunjangan.getValue().toString(); createTableView.getItems().clear();
        for (var data : selectedSiswa.tunjangan) {
            if (data.nama_tunjangan.equals(selTunj)) {
                nominal += data.nominal_tunjangan;
                createTableView.getItems().add(data);
            }
        }
        
        createTotalTagihan.setText("Rp. " + UtilityMiscHelper.formatCurrency(String.valueOf(selectedSiswa.beasiswa == 0 ? (nominal - ((nominal / 100) * 50)) : nominal)));
    }
    
    @FXML
    public void backToMenu(ActionEvent event) { 
        stage.close();
    }
    
    public TableColumn createColumn(String title, String property, TableView table) {
        return this.createColumn(title, property, table, 0.1);
    }
    
    public TableColumn createColumn(String title, String property, TableView table, double sizeMultiplier) {
        TableColumn column = new TableColumn(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.prefWidthProperty().bind(table.widthProperty().multiply(sizeMultiplier));
        column.setResizable(false);
        
        return column;
    }
    
    
    private static boolean empty(String str) {
        if (str == null || str.isEmpty())
            return true;
        
        return false;
    }
    
}
