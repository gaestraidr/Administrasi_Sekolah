/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.model;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Map;
import me.gaestra.administrasi_sekolah.DatabaseManager;

/**
 *
 * @author Ganesha
 */
public class Siswa extends Model {
    
    private static final String tableName = "Siswa";
    
    public String nis;
    public String nama;
    public int kelas_id;
    public String agama;
    public String tmpt_tgl_lahir;
    public int jenis_kelamin;
    public int beasiswa;
    public String alamat;
    public LinkedList<Tunjangan> tunjangan = new LinkedList();
    
    public static void InitializeTable() {
        DatabaseManager.getInstance().execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INTEGER " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "" : "PRIMARY KEY") + " " + (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? "AUTO_INCREMENT" : "AUTOINCREMENT") + ", " +
                "nis VARCHAR(128), " +
                "nama VARCHAR(128), " +
                "kelas_id INTEGER, " +
                "agama VARCHAR(128), " +
                "tmpt_tgl_lahir VARCHAR(128), " +
                "jenis_kelamin INTEGER, " +
                "beasiswa INTEGER, " +
                "alamat TEXT(1000), " +
                "tunjangan TEXT(1000)" +
                (DatabaseManager.connMode == DatabaseManager.DatabaseMode.MYSQL ? ", PRIMARY KEY (id)" : "") + ")");
    }
    
    public Siswa() {}
    
    public Siswa(int _id) {
        super.id = _id;
    }
    
    public Siswa(String _nis, 
            String _nama, 
            int _kelas_id, 
            String _agama, 
            String _tmpt_tgl_lahir,
            int _jenis_kelamin, 
            int _beasiswa, 
            String _alamat) {
        nis = _nis;
        nama = _nama;
        kelas_id = _kelas_id;
        agama = _agama;
        tmpt_tgl_lahir = _tmpt_tgl_lahir;
        jenis_kelamin = _jenis_kelamin;
        beasiswa = _beasiswa;
        alamat = _alamat;
    }
    
    public static LinkedList<Siswa> all() {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName);
        LinkedList<Siswa> array = new LinkedList();
        
        try {
            while (data.next()) {
                Siswa model = new Siswa(data.getInt("id"));
                model.nis = data.getString("nis");
                model.nama = data.getString("nama");
                model.agama = data.getString("agama");
                model.kelas_id = data.getInt("kelas_id");
                model.tmpt_tgl_lahir = data.getString("tmpt_tgl_lahir");
                model.jenis_kelamin = data.getInt("jenis_kelamin");
                model.beasiswa = data.getInt("beasiswa");
                model.alamat = data.getString("alamat");
                model.parseTunjangan(data.getString("tunjangan"));
                
                model.fromDatabase = true;
                array.add(model);
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        return array;
    }
    
    public static Siswa get(int id) {
        return Siswa.get("id = " + id);
    }
    
    public static Siswa get(String key, Object value) {
        return Siswa.get(key + " = " + (value instanceof String ? "'" + value.toString() + "'" : value.toString()));
    }
    
    public static Siswa get(String propertyQuery) {
        ResultSet data = DatabaseManager.getInstance().execute("SELECT * FROM " + tableName + " WHERE " + propertyQuery);
        Siswa retData = null;
        
        try {
            if (data.next()) {
                retData = new Siswa(data.getInt("id"));
                retData.nis = data.getString("nis");
                retData.nama = data.getString("nama");
                retData.agama = data.getString("agama");
                retData.kelas_id = data.getInt("kelas_id");
                retData.tmpt_tgl_lahir = data.getString("tmpt_tgl_lahir");
                retData.jenis_kelamin = data.getInt("jenis_kelamin");
                retData.beasiswa = data.getInt("beasiswa");
                retData.alamat = data.getString("alamat");
                retData.parseTunjangan(data.getString("tunjangan"));
                
                retData.fromDatabase = true;
            }
        } catch (Exception e) {}
        
        return retData;
    }
    
    @Override
    public void get() {
        Siswa data = Siswa.get("id = " + this.id);
        
        if (data != null) {
            this.id = data.id;
            this.nis = data.nis;
            this.nama = data.nama;
            this.agama = data.agama;
            this.kelas_id = data.kelas_id;
            this.tmpt_tgl_lahir = data.tmpt_tgl_lahir;
            this.jenis_kelamin = data.jenis_kelamin;
            this.beasiswa = data.beasiswa;
            this.alamat = data.alamat;
            this.tunjangan = data.tunjangan;
        }
    }
    
    @Override
    public void store() {
        if (!this.fromDatabase)
            DatabaseManager.getInstance().execute("INSERT INTO " + tableName + " (nis, nama, kelas_id, agama, tmpt_tgl_lahir, jenis_kelamin, beasiswa, alamat) VALUES (" + this.buildInsertQuery() + ")");
    }
    
    @Override
    public void update() {
        if (this.fromDatabase)
            DatabaseManager.getInstance().execute("UPDATE " + tableName + " SET " + this.buildUpdateQuery() + " WHERE id = " + this.id);
    }
    
    public void updateTunjangan() {
        if (this.fromDatabase)
            DatabaseManager.getInstance().execute("UPDATE " + tableName + " SET tunjangan = '" + this.buildTunjanganQuery() + "' WHERE id = " + this.id);
    }
    
    @Override
    public void delete() {
        if (this.fromDatabase)
            DatabaseManager.getInstance().execute("DELETE FROM " + tableName + " WHERE id = " + this.id);
    }
    
    public boolean isFromDatabase() {
        return this.fromDatabase;
    }
    
    public Kelas Kelas() {
        return Kelas.get(this.kelas_id);
    }
    
    public void parseTunjangan(String str) {
        if (str == null || str.trim().isEmpty())
            return;
            
        String[] arr = str.split(",");
        this.tunjangan.clear();
        
        for (String data : arr) {
            String[] col = data.split("\\|");
            Tunjangan tuj = Tunjangan.get(Integer.valueOf(col[0]));
            if (tuj == null)
                continue;
            
            tuj.nama_siswa = this.nama;
            tuj.issued_date = col[1];
            tuj.grace_date = col[2];
            
            this.tunjangan.addFirst(tuj);
        }
    }
    
    private String buildInsertQuery() {
        return String.format("'%1$s', '%2$s', %3$d, '%4$s', '%5$s', %6$d, %7$d, '%8$s'", 
                this.nis, this.nama, this.kelas_id, this.agama, this.tmpt_tgl_lahir, this.jenis_kelamin, this.beasiswa, this.alamat);
    }
    
    private String buildUpdateQuery() {
        return String.format("nis = '%1$s', nama = '%2$s', kelas_id = %3$d, agama = '%4$s', tmpt_tgl_lahir = '%5$s', jenis_kelamin = %6$d, beasiswa = %7$d, alamat = '%8$s'", 
                this.nis, this.nama, this.kelas_id, this.agama, this.tmpt_tgl_lahir, this.jenis_kelamin, this.beasiswa, this.alamat);
    }
    
    private String buildTunjanganQuery() {
        String query = "";
        
        for (var data : tunjangan)
            query += (!query.isEmpty() ? "," : "") + data.id + "|" + data.issued_date + "|" + data.grace_date;
        
        return query;
    }
    
    // Model Standard Property Caller
    // ==============================
    public String getNis() { return this.nis; }
    public String getNama() { return this.nama; }
    public String getKelas() { return Kelas.get(this.kelas_id).nama_kelas; }
    public String getAgama() { return this.agama; }
    public String getTmpt_tgl_lahir() { return this.tmpt_tgl_lahir; }
    public String getJenis_kelamin() { return (this.jenis_kelamin == 0 ? "Laki-Laki" : "Perempuan"); }
    public String getBeasiswa() { return (this.beasiswa == 0 ? "Ya" : "Tidak"); }
    public String getAlamat() { return this.alamat; }
    
    // Comparator Hook
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Siswa && ((Siswa)obj).nama.equals(this.nama)) {
            return true; 
        }
        return super.equals(obj);
    }
}
