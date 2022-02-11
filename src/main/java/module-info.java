module me.gaestra.administrasi_sekolah {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.jfoenix;
    requires org.xerial.sqlitejdbc;
    requires java.sql;

    opens me.gaestra.administrasi_sekolah to javafx.fxml;
    exports me.gaestra.administrasi_sekolah;
    exports me.gaestra.administrasi_sekolah.model;
    exports me.gaestra.administrasi_sekolah.controller;
    exports me.gaestra.administrasi_sekolah.helper;
}
