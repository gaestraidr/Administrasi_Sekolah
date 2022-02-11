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
public class Nota extends Model {
    
    private static final String tableName = "Nota";
    private final String groupSeparator = ",";
    
    public int siswa_id;
    public int staff_id;
    public String tunjangan_id;
    public String created_at;
    public String updated_at;
    
    public static void InitializeTable() {
        DatabaseManager.getInstance().execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INTEGER " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "" : "PRIMARY KEY") + " " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "AUTO_INCREMENT" : "AUTOINCREMENT") + ", " +
                "siswa_id INTEGER, " +
                "staff_id INTEGER, " +
                "tunjangan_id VARCHAR(128), " +
                "created_at " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "TIMESTAMP" : "VARCHAR(60)") + " DEFAULT " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "NOW()" : "CURRENT_TIMESTAMP") + ", " +
                "updated_at " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "TIMESTAMP" : "VARCHAR(60)") + " DEFAULT " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "NOW()" : "CURRENT_TIMESTAMP") + "" +
                (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? ", PRIMARY KEY (id)" : "") + ")");
    }
    
    public Nota(int _id) {
        super.id = _id;
    }
    
    public Nota() {}
    
    public static LinkedList<Nota> all() {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName);
        LinkedList<Nota> array = new LinkedList();
        
        try {
            while (data.next()) {
                Nota model = new Nota(data.getInt("id"));
                model.siswa_id = data.getInt("siswa_id");
                model.staff_id = data.getInt("staff_id");
                model.tunjangan_id = data.getString("tunjangan_id");
                model.created_at = data.getString("created_at");
                model.updated_at = data.getString("updated_at");
                
                model.fromDatabase = true;
                array.add(model);
            }
        } catch (Exception e) {}
        
        return array;
    }
    
    public static Nota get(int id) {
        return Nota.get("id = " + id);
    }
    
    public static Nota get(String key, Object value) {
        return Nota.get(key + " = " + (value instanceof String ? "'" + value.toString() + "'" : value.toString()));
    }
    
    public static Nota get(String propertyQuery) {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName + " WHERE " + propertyQuery);
        Nota retData = null;
        
        try {
            if (data.next()) {
                retData = new Nota(data.getInt("id"));
                retData.siswa_id = data.getInt("siswa_id");
                retData.staff_id = data.getInt("staff_id");
                retData.tunjangan_id = data.getString("tunjangan_id");
                retData.created_at = data.getString("created_at");
                retData.updated_at = data.getString("updated_at");
                
                retData.fromDatabase = true;
            }
        } catch (Exception e) {}
        
        return retData;
    }
    
    @Override
    public void get() {
        Nota data = Nota.get("id = " + this.id);
        
        if (data != null) {
            this.id = data.id;
            this.siswa_id = data.siswa_id;
            this.staff_id = data.staff_id;
            this.tunjangan_id = data.tunjangan_id;
            this.created_at = data.created_at;
            this.updated_at = data.updated_at;
        }
    }
    
    @Override
    public void store() {
        if (!this.fromDatabase) {
            DatabaseManager.getInstance().execute("INSERT INTO " + tableName + " (siswa_id, staff_id, tunjangan_id) VALUES (" + this.buildInsertQuery() + ")");
        }
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
    
    public Siswa Siswa() {
        return Siswa.get(this.siswa_id);
    }
    
    public Staff Staff() {
        return Staff.get(this.staff_id);
    }
    
    public Tunjangan Tunjangan() {
        return Tunjangan.get(Integer.valueOf(this.tunjangan_id));
    }
    
    public Tunjangan[] TunjanganAll() {
        LinkedList<Tunjangan> data = new LinkedList();
        String[] tunjangan_list = this.tunjangan_id.split(this.groupSeparator);
        
        for (String tjid : tunjangan_list) {
            data.addFirst(Tunjangan.get(tjid));
        }
        
        return (Tunjangan[])data.toArray();
    }
    
    private String buildInsertQuery() {
        return String.format("%1$d, %2$d, '%3$s'", this.siswa_id, this.staff_id, this.tunjangan_id);
    }
    
    private String buildUpdateQuery() {
        return String.format("siswa_id = %1$d, staff_id = %2$d, tunjangan_id = '%3$s', updated_at = " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "NOW()" : "CURRENT_TIMESTAMP") + "", this.siswa_id, this.staff_id, this.tunjangan_id);
    }
    
    // Model Standard Property Caller
    // ==============================
    public String getId() { return "NOTA" + String.format("%04d", this.id); }
    public String getSiswa_name() { return this.Siswa().nama; }
    public String getCreated_at() { return this.created_at; }
}
