/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.model;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import me.gaestra.administrasi_sekolah.DatabaseManager;
import me.gaestra.administrasi_sekolah.helper.UtilityMiscHelper;

/**
 *
 * @author Ganesha
 */
public class Tunjangan extends Model {
    
    private static final String tableName = "Tunjangan";
    
    public String nama_tunjangan;
    public int nominal_tunjangan;
    public int periodic_mode;
    public int specific_id;
    public String first_date;
    public String second_date;
    
    // Siswa Hook
    public String nama_siswa = "NO_NAME";
    public String issued_date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
    public String grace_date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
    public Boolean isExtendedPeriod = null;
    
    public static void InitializeTable() {
        DatabaseManager.getInstance().execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INTEGER " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "" : "PRIMARY KEY") + " " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "AUTO_INCREMENT" : "AUTOINCREMENT") + ", " +
                "nama_tunjangan VARCHAR(128), " +
                "nominal_tunjangan INTEGER, " +
                "periodic_mode INTEGER, " +
                "specific_id INTEGER, " +
                "first_date VARCHAR(60), " +
                "second_date VARCHAR(60)" +
                (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? ", PRIMARY KEY (id)" : "") + ")");
    }
    
    public Tunjangan(int _id) {
        super.id = _id;
    }
    
    public Tunjangan() { }
    
    public static LinkedList<Tunjangan> all() {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName);
        LinkedList<Tunjangan> array = new LinkedList();
        
        try {
            while (data.next()) {
                Tunjangan model = new Tunjangan(data.getInt("id"));
                model.nama_tunjangan = data.getString("nama_tunjangan");
                model.nominal_tunjangan = data.getInt("nominal_tunjangan");
                model.periodic_mode = data.getInt("periodic_mode");
                model.specific_id = data.getInt("specific_id");
                model.first_date = data.getString("first_date");
                model.second_date = data.getString("second_date");                
                
                model.fromDatabase = true;
                array.add(model);
            }
        } catch (Exception e) {}
        
        return array;
    }
    
    public static Tunjangan get(int id) {
        return Tunjangan.get("id = " + id);
    }
    
    public static Tunjangan get(String key, Object value) {
        return Tunjangan.get(key + " = " + (value instanceof String ? "'" + value.toString() + "'" : value.toString()));
    }
    
    public static Tunjangan get(String propertyQuery) {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName + " WHERE " + propertyQuery);
        Tunjangan retData = null;
        
        try {
            if (data.next()) {
                retData = new Tunjangan(data.getInt("id"));
                retData.nama_tunjangan = data.getString("nama_tunjangan");
                retData.nominal_tunjangan = data.getInt("nominal_tunjangan");
                retData.periodic_mode = data.getInt("periodic_mode");
                retData.specific_id = data.getInt("specific_id");
                retData.first_date = data.getString("first_date");
                retData.second_date = data.getString("second_date");
                
                retData.fromDatabase = true;
            }
        } catch (Exception e) {}
        
        return retData;
    }
    
    @Override
    public void get() {
        Tunjangan data = Tunjangan.get("id = " + this.id);
        
        if (data != null) {
            this.id = data.id;
            this.nama_tunjangan = data.nama_tunjangan;
            this.nominal_tunjangan = data.nominal_tunjangan;
            this.periodic_mode = data.periodic_mode;
            this.specific_id = data.specific_id;
            this.first_date = data.first_date;
            this.second_date = data.second_date;     
        }
    }
    
    @Override
    public void store() {
        if (!this.fromDatabase)
            DatabaseManager.getInstance().execute("INSERT INTO " + tableName + " (nama_tunjangan, nominal_tunjangan, periodic_mode, specific_id, first_date, second_date) VALUES (" + this.buildInsertQuery() + ")");
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
    
    public Model Specified() {
        // Only Kelas for now
        if (this.periodic_mode == PeriodicMode.MONTHLY_SPECIFIED 
                || this.periodic_mode == PeriodicMode.YEARLY_SPECIFIED
                || this.periodic_mode == PeriodicMode.ONDATE_SPECIFIED) {
            return Kelas.get(this.specific_id);
        }
        
        return null;
    }
    
    private String buildInsertQuery() {
        return String.format("'%1$s', %2$d, %3$d, %4$d, '%5$s', '%6$s'",
                this.nama_tunjangan, this.nominal_tunjangan, this.periodic_mode, this.specific_id, this.first_date, this.second_date);
    }
    
    private String buildUpdateQuery() {
        return String.format("nama_tunjangan = '%1$s', nominal_tunjangan = %2$d, periodic_mode = %3$d, specific_id = %4$d, first_date = '%5$s', second_date = '%6$s'",
                this.nama_tunjangan, this.nominal_tunjangan, this.periodic_mode, this.specific_id, this.first_date, this.second_date);
    }
    
    // Model Standard Property Caller
    // ==============================
    public String getId() { return String.valueOf(this.id); }
    public String getNama_tunjangan() { return this.nama_tunjangan; }
    public String getNominal_tunjangan() { return "Rp. " + UtilityMiscHelper.formatCurrency(String.valueOf(this.nominal_tunjangan)); }
    public String getPeriodic_mode() {
        switch(this.periodic_mode) {
            case PeriodicMode.MONTHLY:
                return "Perbulan - Semua";
            case PeriodicMode.YEARLY:
                return "Pertahun - Semua";
            case PeriodicMode.ONDATE:
                return "Per Tanggal - Semua";
            case PeriodicMode.MONTHLY_SPECIFIED:
                return "Perbulan - Kelas";
            case PeriodicMode.YEARLY_SPECIFIED:
                return "Pertahun - Kelas";
            case PeriodicMode.ONDATE_SPECIFIED:
                return "Per Tanggal - Kelas";
            case PeriodicMode.NONE:
            default:
                return "Tanpa Periode";
        }
    }
    public String getSpecific_id() { return (this.Specified() == null ? "-" : ((Kelas)this.Specified()).nama_kelas); }
    public String getFirst_date() { return this.first_date; }
    public String getSecond_date() { return this.second_date; }
    
    // Siswa Hook
    public String getNama_siswa() { return this.nama_siswa; }
    public String getIssued_date() { return this.issued_date; }
    public String getGrace_date() { return this.grace_date; }
    
    // Comparator Hook
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tunjangan && ((Tunjangan)obj).nama_tunjangan.equals(this.nama_tunjangan)) {
            return true; 
        }
        return super.equals(obj);
    }
    
    public interface PeriodicMode {
        public static final int NONE = 0;
        public static final int MONTHLY = 1;
        public static final int YEARLY = 2;
        public static final int ONDATE = 3;
        public static final int MONTHLY_SPECIFIED = 4;
        public static final int YEARLY_SPECIFIED = 5;
        public static final int ONDATE_SPECIFIED = 6;
    }
}
