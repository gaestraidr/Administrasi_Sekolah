/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import me.gaestra.administrasi_sekolah.helper.ControlTransformHelper;
import me.gaestra.administrasi_sekolah.helper.DialogAlertHelper;
import me.gaestra.administrasi_sekolah.helper.UtilityMiscHelper;
import me.gaestra.administrasi_sekolah.model.Nota;

/**
 * FXML Controller class
 *
 * @author Ganesha
 */
public class CetakFormController extends BaseController {

    // Core component
    public Nota nota;
    public Stage stage;
    
    // Cetak Form Component
    @FXML public TableView cetakTableView;
    @FXML public Label cetakNotaId;
    @FXML public Label cetakNamaSiswa;
    @FXML public Label cetakNis;
    @FXML public Label cetakStaff;
    @FXML public Label cetakSubTotal;
    @FXML public Label cetakBeasiswa;
    @FXML public Label cetakTotal;
    
    @FXML public JFXButton cetakCancel;
    @FXML public JFXButton cetakPrint;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn column1 = ControlTransformHelper.createColumn("Tipe Tunjangan", "nama_tunjangan", cetakTableView, 0.33);
        TableColumn column2 = ControlTransformHelper.createColumn("Tanggal Pengisuan", "issued_date", cetakTableView, 0.33);
        TableColumn column3 = ControlTransformHelper.createColumn("Nominal Tunjangan", "nominal_tunjangan", cetakTableView, 0.33);
        
        cetakTableView.getColumns().addAll(column1, column2, column3);
    }
    
    public void notaData(Nota _nota) {
        nota = _nota;
    }
    
    public void initData() {
        cetakNotaId.setText("Nota No. : " + nota.getId());
        cetakNamaSiswa.setText("Siswa : " + nota.Siswa().nama);
        cetakNis.setText("NIS : " + nota.Siswa().nis);
        cetakStaff.setText(nota.staff_id <= 0 ? "Akun Master" : nota.Staff().nama);
        int subTotal = 0;
        
        for (var tunj : nota.Siswa().tunjangan) {
            if (tunj.id == Integer.valueOf(nota.tunjangan_id)) {
                cetakTableView.getItems().add(tunj);
                subTotal += tunj.nominal_tunjangan;
            }
        }
        
        cetakSubTotal.setText("Rp. " + UtilityMiscHelper.formatCurrency(String.valueOf(subTotal)));
        cetakBeasiswa.setText(nota.Siswa().beasiswa == 0 ? "50%" : "0%");
        cetakTotal.setText("Rp. " + UtilityMiscHelper.formatCurrency(String.valueOf(nota.Siswa().beasiswa == 0 ? subTotal - ((subTotal / 100) * 50) : subTotal)));
        
        cetakStaff.requestFocus();
    }
    
    @FXML
    public void printDocument(ActionEvent event) {
        cetakCancel.setVisible(false); cetakPrint.setVisible(false);
        Platform.runLater(() -> {
            PrinterJob job = PrinterJob.createPrinterJob();
            job.setPrinter(Printer.getDefaultPrinter());
            if (job != null) {
                if (job.printPage(cetakTableView.getParent())) {
                    DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Nota Printed", "Nota berhasil di print!", "Nota telah berhasil di print ke PDF!");
                    job.endJob();
                }
            }
            cetakCancel.setVisible(true); cetakPrint.setVisible(true);
        });
        
    }
    
    @FXML
    public void backToNotaForm(ActionEvent event) {
        stage.close();
    }
    
}
