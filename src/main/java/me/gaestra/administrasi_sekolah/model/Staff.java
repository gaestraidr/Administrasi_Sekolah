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
public class Staff extends Model {
    
    private static final String tableName = "Staff";
    
    public String nama;
    public String jabatan;
    public String username;
    public String password;
    public String alamat;
    
    public static void InitializeTable() {
        DatabaseManager.getInstance().execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INTEGER " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "" : "PRIMARY KEY") + " " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "AUTO_INCREMENT" : "AUTOINCREMENT") + ", " +
                "nama VARCHAR(128), " +
                "jabatan VARCHAR(128), " +
                "username VARCHAR(128), " +
                "password VARCHAR(128), " +
                "alamat TEXT(1000)" +
                (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? ", PRIMARY KEY (id)" : "") + ")");
    }
    
    public Staff(int _id) {
        super.id = _id;
    }
    
    public Staff() {}
    
    public static LinkedList<Staff> all() {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName);
        LinkedList<Staff> array = new LinkedList();
        
        try {
            while (data.next()) {
                Staff model = new Staff(data.getInt("id"));
                model.nama = data.getString("nama");
                model.jabatan = data.getString("jabatan");
                model.username = data.getString("username");
                model.password = data.getString("password");
                model.alamat = data.getString("alamat");
                
                model.fromDatabase = true;
                array.add(model);
            }
        } catch (Exception e) {}
        
        return array;
    }
    
    public static Staff get(int id) {
        return Staff.get("id = " + id);
    }
    
    public static Staff get(String key, Object value) {
        return Staff.get(key + " = " + (value instanceof String ? "'" + value.toString() + "'" : value.toString()));
    }
    
    public static Staff get(String propertyQuery) {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName + " WHERE " + propertyQuery);
        Staff retData = null;
        
        try {
            if (data.next()) {
                retData = new Staff(data.getInt("id"));
                retData.nama = data.getString("nama");
                retData.jabatan = data.getString("jabatan");
                retData.username = data.getString("username");
                retData.password = data.getString("password");
                retData.alamat = data.getString("alamat");
                
                retData.fromDatabase = true;
            }
        } catch (Exception e) {}
        
        return retData;
    }
    
    public static int count() {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT COUNT(id) as count FROM " + tableName);
        int retInt = 0;
        try {
            retInt = data.getInt("count");
        } catch (Exception e) {}
        
        return retInt;
//        return Staff.all().size();
    }
    
    @Override
    public void get() {
        Staff data = Staff.get("id = " + this.id);
        
        if (data != null) {
            this.id = data.id;
            this.nama = data.nama;
            this.jabatan = data.jabatan;
            this.username = data.username;
            this.password = data.password;
            this.alamat = data.alamat;
        }
    }
    
    @Override
    public void store() {
        if (!this.fromDatabase)
            DatabaseManager.getInstance().execute("INSERT INTO " + tableName + " (nama, jabatan, username, password, alamat) VALUES (" + this.buildInsertQuery() + ")");
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
        return String.format("'%1$s', '%2$s', '%3$s', '%4$s', '%5$s'", this.nama, this.jabatan, this.username, this.password, this.alamat);
    }
    
    private String buildUpdateQuery() {
        return String.format("nama = '%1$s', jabatan = '%2$s', username = '%3$s', password = '%4$s', alamat = '%5$s'", this.nama, this.jabatan, this.username, this.password, this.alamat);
    }
    
    // Model Standard Property Caller
    // ==============================
    public String getNama() { return this.nama; }
    public String getJabatan() { return this.jabatan; }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public String getAlamat() { return this.alamat; }
    
    // Comparator Hook
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Staff && ((Staff)obj).nama.equals(this.nama)) {
            return true; 
        }
        return super.equals(obj);
    }
}
