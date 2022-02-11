/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import me.gaestra.administrasi_sekolah.helper.DialogAlertHelper;
import me.gaestra.administrasi_sekolah.helper.Logger;

/**
 *
 * @author Ganesha
 */
public class DatabaseManager {
    
    // Database Name
    public static final String databaseName = "administrasi_sekolah";
    
    // Core Property
    public static final int connMode = DatabaseMode.MYSQL;
    
    public static final String sqLiteUrl = "jdbc:sqlite:" + databaseName + ".db";
    public static final String mySqlUrl = "jdbc:mysql://localhost:3306/" + databaseName + "?useSSL=false";
    
    public static final String mySqlDatabaseCreationSyntax = "CREATE DATABASE " + databaseName + " CHARACTER SET utf8 COLLATE utf8_general_ci;";
    
    // Instance
    private static DatabaseManager instance;
    
    // Instance Property
    private Connection conn;
    
    // Guard Clause Constructor
    private DatabaseManager() {}
    
    public static DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        
        instance.open();
        return instance;
    }
    
    public void open() {
        try {
            if (conn == null || conn.isClosed())
                switch(connMode) {
                    case DatabaseMode.MYSQL:
                        conn = DriverManager.getConnection(mySqlUrl, "root", "");
                        break;
                    case DatabaseMode.SQLITE:
                    default:
                        conn = DriverManager.getConnection(sqLiteUrl);
                        break;
                }
        } catch (SQLException e) {
            Logger.error("DatabaseManager.open() Error: " + e.getMessage());
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Connecting Database", "Error saat mengkoneksikan database.", 
                        "Terjadi kesalahan saat mengkoneksikan database, mohon cek log file yang terdapat di folder aplikasi.");
        }
    }
    
    public ResultSet execute(String sqlQuery) {
        ResultSet data = null;
        try {
            if (sqlQuery.contains("DELETE") || sqlQuery.contains("UPDATE") || sqlQuery.contains("CREATE") || sqlQuery.contains("INSERT INTO"))
                conn.createStatement().execute(sqlQuery + ";");
            else data = conn.createStatement().executeQuery(sqlQuery + ";");
        } catch (SQLException e) {
            Logger.error("DatabaseManager.execute() Error: " + e.getMessage());
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Database Query Execution", "Error saat eksekusi query database.", 
                        "Terjadi kesalahan saat eksekusi query database, mohon cek log file yang terdapat di folder aplikasi.");
        }
        
        return data;
    }
    
    public void close() {
        try {
            if (!conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            Logger.error("DatabaseManager.close() Error: " + e.getMessage());
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Closing Database", "Error saat menutup database.", 
                        "Terjadi kesalahan saat menutup database, mohon cek log file yang terdapat di folder aplikasi.");
        }
    }
    
    public interface DatabaseMode {
        public static final int SQLITE = 1;
        public static final int MYSQL = 2;
    }
}
