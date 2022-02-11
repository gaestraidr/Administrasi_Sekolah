/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import me.gaestra.administrasi_sekolah.App;
import me.gaestra.administrasi_sekolah.helper.ControlTransformHelper;
import me.gaestra.administrasi_sekolah.helper.DialogAlertHelper;
import me.gaestra.administrasi_sekolah.helper.UtilityMiscHelper;
import me.gaestra.administrasi_sekolah.model.Model;
import me.gaestra.administrasi_sekolah.model.Nota;
import me.gaestra.administrasi_sekolah.model.Siswa;
import me.gaestra.administrasi_sekolah.model.Tunjangan;

/**
 * FXML Controller class
 *
 * @author Ganesha
 */
public class RekapanFormController extends BaseController {
    
    // Core Component
    @FXML public AnchorPane cetakRootLayout;
    
    @FXML public TableView cetakTableView;
    @FXML public VBox cetakBoxLayout;
    
    @FXML public Label cetakStaff;
    @FXML public Label cetakJabatan;
    @FXML public Label cetakDate;
    @FXML public Label cetakTitle;
    
    @FXML public JFXButton cetakCancel;
    @FXML public JFXButton cetakPrint;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Do Nothing
    }
    
    public void initRekapanData(Model data) { initRekapanData(data, 1); }
    public void initRekapanData(Model data, int optionMode) {
        if (data instanceof Siswa) {
            cetakTitle.setText("Rekapan Data Siswa");
            
            TableColumn column1 = ControlTransformHelper.createColumn("NIS", "nis", cetakTableView, 0.125);
            TableColumn column2 = ControlTransformHelper.createColumn("Nama", "nama", cetakTableView, 0.125);
            TableColumn column3 = ControlTransformHelper.createColumn("Kelas", "kelas", cetakTableView, 0.125);
            TableColumn column4 = ControlTransformHelper.createColumn("Agama", "agama", cetakTableView, 0.125);
            TableColumn column5 = ControlTransformHelper.createColumn("Tempat, Tanggal Lahir", "tmpt_tgl_lahir", cetakTableView, 0.125);
            TableColumn column6 = ControlTransformHelper.createColumn("Jenis Kelamin", "jenis_kelamin", cetakTableView, 0.125);
            TableColumn column7 = ControlTransformHelper.createColumn("Beasiswa", "beasiswa", cetakTableView, 0.125);
            TableColumn column8 = ControlTransformHelper.createColumn("Alamat", "alamat", cetakTableView, 0.125);

            cetakTableView.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7, column8);
            
            for (var siswa : Siswa.all())
                cetakTableView.getItems().add(siswa);
            
            this.wrapTable(cetakTableView);
        }
        else if (data instanceof Tunjangan) {
            cetakTitle.setText("Rekapan Data Tunjangan");
            
            if (optionMode == 1) {
                TableColumn column1 = ControlTransformHelper.createColumn("ID", "id", cetakTableView, 0.1);
                TableColumn column2 = ControlTransformHelper.createColumn("Nama Tunjangan", "nama_tunjangan", cetakTableView, 0.15);
                TableColumn column3 = ControlTransformHelper.createColumn("Nominal Tunjangan", "nominal_tunjangan", cetakTableView, 0.15);
                TableColumn column4 = ControlTransformHelper.createColumn("Mode Periode", "periodic_mode", cetakTableView, 0.15);
                TableColumn column5 = ControlTransformHelper.createColumn("Spesifik Target", "specific_id", cetakTableView, 0.15);
                TableColumn column6 = ControlTransformHelper.createColumn("Dari Tanggal", "first_date", cetakTableView, 0.1);
                TableColumn column7 = ControlTransformHelper.createColumn("Sampai Tanggal", "second_date", cetakTableView, 0.15);

                cetakTableView.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7);

                for (var tunj : Tunjangan.all())
                    cetakTableView.getItems().add(tunj);

                this.wrapTable(cetakTableView);
            }
            else { // optionMode == 2
                LinkedList<Tunjangan> tunjCollection = new LinkedList();
                
                for (var siswa : Siswa.all())
                    tunjCollection.addAll(siswa.tunjangan);
                
                tunjCollection.sort((o1, o2) -> {
                    try {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(o1.issued_date);
                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(o2.issued_date);
                        return (int)(date2.getTime() - date1.getTime());
                    } catch (Exception e) { e.printStackTrace(); }
                    return 0;
                });
                Collections.reverse(tunjCollection);
                
                cetakBoxLayout.getChildren().clear();
                TableView currTable = null;
                String currDate = "";

                for (Tunjangan tunj : tunjCollection) {
                    String tunjDate = this.getDateAnotherFormat(tunj.issued_date);
                    if (!currDate.equals(tunjDate)) {
                        currDate = tunjDate;
                        cetakBoxLayout.getChildren().add(this.getCaptionLabel(currDate));
                        currTable = addTunjanganSiswaTableView();
                    }

                    currTable.getItems().add(tunj);
                    this.wrapTable(currTable);
                }
            }
        }
        else if (data instanceof Nota) {
            cetakTitle.setText("Rekapan Data Nota");
            
            cetakBoxLayout.getChildren().clear();
            TableView currTable = null;
            String currDate = "";
            
            for (var nota : Nota.all()) {
                String notaDate = this.getDateAnotherFormat(nota.created_at.split(" ")[0]);
                if (!currDate.equals(notaDate)) {
                    currDate = notaDate;
                    cetakBoxLayout.getChildren().add(this.getCaptionLabel(currDate));
                    currTable = addNotaTableView();
                }

                currTable.getItems().add(nota);
                this.wrapTable(currTable);
            }
        }
        
        cetakDate.setText("Jakarta, " + DateTimeFormatter.ofPattern("dd MMMM yyyy").format(LocalDateTime.now()));
        cetakJabatan.setText(App.loggedUser.jabatan);
        cetakStaff.setText(App.loggedUser.nama);
        
        cetakStaff.requestFocus();
    }
    
    @FXML
    public void doPrint() {
        cetakCancel.setVisible(false); cetakPrint.setVisible(false);
        Platform.runLater(() -> {
            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.getDefaultPageLayout();

            // Printable Area Size
            double pWidth = pageLayout.getPrintableWidth();
            double pHeight = pageLayout.getPrintableHeight();

            // Root Layout dimensions
            double nWidth = cetakRootLayout.getBoundsInParent().getWidth();
            double nHeight = cetakRootLayout.getBoundsInParent().getHeight();

            // Calculate Spacing
            double widthLeft = pWidth - nWidth;
            double heightLeft = pHeight - nHeight;

            double scale;

            if (widthLeft < heightLeft) scale = pWidth / nWidth;
            else scale = pHeight / nHeight;

            // Preserve Ratio
            cetakRootLayout.getTransforms().add(new Scale(scale, scale));
            stage.close();
            
            PrinterJob job = PrinterJob.createPrinterJob();
            if(job.printPage(pageLayout, cetakRootLayout)){
                DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Rekapan Printed", "Rekapan berhasil di print!", "Rekapan telah berhasil di print ke PDF!");
                job.endJob();
            }
            
            // Undo Change
            cetakRootLayout.getTransforms().remove(scale);
            cetakCancel.setVisible(true); cetakPrint.setVisible(true);
        });
    }
    
    @FXML
    public void doExit() {
        
    }
    
    private TableView addNotaTableView() {
        TableView retTable = new TableView();
        retTable.setPrefWidth(cetakBoxLayout.getPrefWidth());
        
        TableColumn column1 = ControlTransformHelper.createColumn("ID Nota", "id", retTable, 0.2);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Siswa", "siswa_name", retTable, 0.4);
        TableColumn column3 = ControlTransformHelper.createColumn("Tanggal Transaksi", "created_at", retTable, 0.4);
        
        retTable.getColumns().clear();
        retTable.getColumns().addAll(column1, column2, column3);
        cetakBoxLayout.getChildren().add(retTable);
        
        return retTable;
    }
    
    private TableView addTunjanganSiswaTableView() {
        TableView retTable = new TableView();
        retTable.setPrefWidth(cetakBoxLayout.getPrefWidth());
        
        TableColumn column1 = ControlTransformHelper.createColumn("Nama Siswa", "nama_siswa", retTable, 0.25);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Tunjangan", "nama_tunjangan", retTable, 0.25);
        TableColumn column3 = ControlTransformHelper.createColumn("Tanggal Pengisuan", "issued_date", retTable, 0.25);
        TableColumn column4 = ControlTransformHelper.createColumn("Tanggal Tenggang", "grace_date", retTable, 0.25);
        
        retTable.getColumns().clear();
        retTable.getColumns().addAll(column1, column2, column3, column4);
        cetakBoxLayout.getChildren().add(retTable);
        
        retTable.setRowFactory(tv -> new TableRow<Tunjangan>() {
            @Override
            protected void updateItem(Tunjangan item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !item.grace_date.isEmpty()) {
                    LocalDate graceDate = UtilityMiscHelper.formatToDate(item.grace_date);
                    Nota buktiPemp = Nota.get("siswa_id = " + Siswa.get("nama" , item.nama_siswa).id + " AND tunjangan_id = '"  + item.id + "' AND created_at >= " + item.issued_date);

                    if (LocalDate.now().isAfter(graceDate) && buktiPemp == null) {
                        this.getStyleClass().add("table-row-red");
                    }
                }
            }
        });
        
        return retTable;
    }
    
    private void wrapTable(TableView table) {
        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.01)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
    }
    
    private String getDateAnotherFormat(String date) {
        var unparsedDate = new SimpleDateFormat("yyyy-MM-dd"); 
        java.util.Date parsedDate = null;
        try { parsedDate = unparsedDate.parse(date); } catch (Exception e) { e.printStackTrace(); }
        return new SimpleDateFormat("MMMM yyyy").format(parsedDate);
    }
    
    private Label getCaptionLabel(String caption) {
        Label label = new Label(caption);
        label.getStyleClass().add("bg-secondary"); label.getStyleClass().add("text-white");
        label.setFont(new Font(30));
        label.setPrefWidth(cetakBoxLayout.getPrefWidth());
        label.alignmentProperty().set(Pos.CENTER);
        
        return label;
    }
}
 