/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.model;

import java.sql.ResultSet;
import java.util.LinkedList;
import me.gaestra.administrasi_sekolah.DatabaseManager;

/**
 *
 * @author Ganesha
 */
public class Kelas extends Model {
    
    private static final String tableName = "Kelas";
    
    public String nama_kelas;
    
    public static void InitializeTable() {
        DatabaseManager.getInstance().execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INTEGER " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "" : "PRIMARY KEY") + " " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "AUTO_INCREMENT" : "AUTOINCREMENT") + ", " +
                "nama_kelas VARCHAR(128)" +
                (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? ", PRIMARY KEY (id)" : "") + ")");
    }
    
    public Kelas(int _id) {
        super.id = _id;
    }
    
    public Kelas(String _nama_kelas) {
        nama_kelas = _nama_kelas;
    }
    
    public static LinkedList<Kelas> all() {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName);
        LinkedList<Kelas> array = new LinkedList();
        
        try {
            while (data.next()) {
                Kelas model = new Kelas(data.getInt("id"));
                model.nama_kelas = data.getString("nama_kelas");
                
                model.fromDatabase = true;
                array.add(model);
            }
        } catch (Exception e) {}
        
        return array;
    }
    
    public static Kelas get(int id) {
        return Kelas.get("id = " + id);
    }
    
    public static Kelas get(String key, Object value) {
        return Kelas.get(key + " = " + (value instanceof String ? "'" + value.toString() + "'" : value.toString()));
    }
    
    public static Kelas get(String propertyQuery) {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName + " WHERE " + propertyQuery);
        Kelas retData = null;
        
        try {
            if (data.next()) {
                retData = new Kelas(data.getInt("id"));
                retData.nama_kelas = data.getString("nama_kelas");
                
                retData.fromDatabase = true;
            }
        } catch (Exception e) {}
        
        return retData;
    }
    
    @Override
    public void get() {
        Kelas data = Kelas.get("id = " + this.id);
        
        if (data != null) {
            this.id = data.id;
            this.nama_kelas = data.nama_kelas;
        }
    }
    
    @Override
    public void store() {
        if (!this.fromDatabase)
            DatabaseManager.getInstance().execute("INSERT INTO " + tableName + " (nama_kelas) VALUES (" + this.buildInsertQuery() + ")");
    }
    
    @Override
    public void update() {
        if (this.fromDatabase)
            DatabaseManager.getInstance().execute("UPDATE " + tableName + " SET " + this.buildUpdateQuery() + " WHERE id = " + this.id);
    }
    
    @Override
    public void delete() {
        if (this.fromDatabase)
            DatabaseManager.getInstance().execute("DELETE FROM " + tableName + " WHERE id = " + this.id);
    }
    
    private String buildInsertQuery() {
        return String.format("'%1$s'", this.nama_kelas);
    }
    
    private String buildUpdateQuery() {
        return String.format("nama_kelas = '%1$s'", this.nama_kelas);
    }
    
    // Model Standard Property Caller
    // ==============================
    public String getNama_kelas() { return nama_kelas; }
    public String getId() { return String.valueOf(id); }
}
