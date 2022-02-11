package me.gaestra.administrasi_sekolah;


import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import me.gaestra.administrasi_sekolah.helper.DialogAlertHelper;
import me.gaestra.administrasi_sekolah.model.Kelas;
import me.gaestra.administrasi_sekolah.model.Nota;
import me.gaestra.administrasi_sekolah.model.Siswa;
import me.gaestra.administrasi_sekolah.model.Staff;
import me.gaestra.administrasi_sekolah.model.Tunjangan;

/**
 * JavaFX App
 */
public class App extends Application {

    // Core Public Component
    public static Staff loggedUser;
    public static final String appName = "SAPSGAESTRA";
    public static final String appTitle = "Sistem Administrasi Pembayaran Sekolah - Gaestra";
    
    // Core Component
    private static Scene scene;
    private File file;
    private FileChannel channel;
    private FileLock lock;

    @Override
    public void start(Stage stage) throws IOException {
        if (this.isAppActive()) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, 
                    "Aplikasi telah berjalan!", "Aplikasi telah berjalan!", "Aplikasi sudah terbuka dan berjalan, mohon tutup aplikasi sebelumnya!");
            Platform.exit();
        }
        
        DatabaseManager.getInstance();
        setupDatabase();
        setupServiceWorker();
        stage.initStyle(StageStyle.UNDECORATED);
        Parent root = loadFXML("view/LoginForm");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    @Override
    public void stop() {
        DatabaseManager.getInstance().close();
        PeriodicalWorkerService.getInstance().stop();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static FXMLLoader getFXMLLoader(String fxml) {
        return new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    }
    
    public static Image getIconImage() {
        return new Image(App.class.getResourceAsStream("view/style/logo.png"));
    }

    public static void main(String[] args) {
        launch();
    }
    
    private static void setupDatabase() {
        Kelas.InitializeTable();
        Siswa.InitializeTable();
        Staff.InitializeTable();
        Nota.InitializeTable();
        Tunjangan.InitializeTable();
    }
    
    private static void setupServiceWorker()
    {
        PeriodicalWorkerService.getInstance().start();
    }
    
    public boolean isAppActive() {
        try {
            file = new File(System.getProperty("user.home"), appName + ".tmp");
            channel = new RandomAccessFile(file, "rw").getChannel();

            try {
                lock = channel.tryLock();
            }
            catch (OverlappingFileLockException e) {
                // Locked
                closeLock();
                return true;
            }

            if (lock == null) {
                closeLock();
                return true;
            }

            Runtime.getRuntime().addShutdownHook(new Thread() {
                    // Delete lock on exit
                    public void run() {
                        closeLock();
                        deleteFile();
                    }
                });
            return false;
        }
        catch (Exception e) {
            closeLock();
            return true;
        }
    }

    private void closeLock() {
        try { lock.release();  }
        catch (Exception e) {  }
        try { channel.close(); }
        catch (Exception e) {  }
    }

    private void deleteFile() {
        try { file.delete(); }
        catch (Exception e) { }
    }

}