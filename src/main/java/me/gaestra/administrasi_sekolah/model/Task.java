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
public class Task extends Model {
    
    private static final String tableName = "Task";
    
    public int tunjangan_id;
    public String executed_at;
    
    public static void InitializeTable() {
        DatabaseManager.getInstance().execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INTEGER " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "" : "PRIMARY KEY") + " " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "AUTO_INCREMENT" : "AUTOINCREMENT") + ", " +
                "tunjangan_id INTEGER," +
                "executed_at VARCHAR(60)" +
                (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? ", PRIMARY KEY (id)" : "") + ")");
    }
    
    public Task() {}
    
    public Task(int _id) {
        super.id = _id;
    }
    
    public static LinkedList<Task> all() {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName);
        LinkedList<Task> array = new LinkedList();
        
        try {
            while (data.next()) {
                Task model = new Task(data.getInt("id"));
                model.tunjangan_id = data.getInt("tunjangan_id");
                model.executed_at = data.getString("executed_at");
                
                model.fromDatabase = true;
                array.add(model);
            }
        } catch (Exception e) {}
        
        return array;
    }
    
    public static Task get(int id) {
        return Task.get("id = " + id);
    }
    
    public static Task get(String key, Object value) {
        return Task.get(key + " = " + (value instanceof String ? "'" + value.toString() + "'" : value.toString()));
    }
    
    public static Task get(String propertyQuery) {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName + " WHERE " + propertyQuery);
        Task retData = null;
        
        try {
            if (data.next()) {
                retData = new Task(data.getInt("id"));
                retData.tunjangan_id = data.getInt("tunjangan_id");
                retData.executed_at = data.getString("executed_at");
                
                retData.fromDatabase = true;
            }
        } catch (Exception e) {}
        
        return retData;
    }
    
    @Override
    public void get() {
        Task data = Task.get("id = " + this.id);
        
        if (data != null) {
            this.id = data.id;
            this.tunjangan_id = data.tunjangan_id;
            this.executed_at = data.executed_at;
        }
    }
    
    public void save() {
        if (!this.fromDatabase)
            this.store();
        else this.update();
    }
    
    @Override
    public void store() {
        if (!this.fromDatabase)
            DatabaseManager.getInstance().execute("INSERT INTO " + tableName + " (tunjangan_id, executed_at) VALUES (" + this.buildInsertQuery() + ")");
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
        return String.format("%1$d, '%2$s'", this.tunjangan_id, this.executed_at);
    }
    
    private String buildUpdateQuery() {
        return String.format("tunjangan_id = %1$d, executed_at = '%2$s'", this.tunjangan_id, this.executed_at);
    }
}
