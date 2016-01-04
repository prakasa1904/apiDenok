package com.dka.sigmaipb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Deni Kurnia Aldian on 11/7/2015.
 */

public class KoneksiDB {
    private Connection connect;
    private String driverName = "org.postgresql.Driver"; // Driver Untuk Koneksi Ke PostgreSQL
    private String jdbc = "jdbc:postgresql://";
    private String host = "192.168.43.196:"; // Host ini Bisa Menggunakan IP Anda, Contoh : 192.168.100.100
    private String port = "5432/"; // Port Default PostgreSQL
    private String database = "ipb_gis"; // Ini Database yang akan digunakan
    private String url = jdbc + host + port + database;
    private String username = "usser"; //
    private String password = "public";
    public String getKoneksi() throws SQLException {
        if (connect == null) {
            try {
                Class.forName(driverName);
                //System.out.println("Class Driver Ditemukan");
                try {
                    connect = DriverManager.getConnection(url, username, password);
                    //System.out.println("Koneksi Database Sukses");

                    // plus Query
                    Statement stmt = connect.createStatement();
                    String sql;
                    sql = "SELECT * FROM tbl_lokasi LIMIT 2";
                    ResultSet rs = stmt.executeQuery(sql);

                    String kode_lokasi = "";
                    while(rs.next()){
                        if( rs.getString("kode_lokasi") != null )
                            kode_lokasi += "\nKode : " + rs.getString("kode_lokasi") + "\n";
                    }
                    // Display
                    //System.out.print(kode_lokasi);
                    return kode_lokasi;
                } catch (SQLException se) {
                    System.out.println("Koneksi Database Gagal : " + se);
                    System.exit(0);
                }
            } catch (ClassNotFoundException cnfe) {
                System.out.println("Class Driver Tidak Ditemukan, Terjadi Kesalahan Pada : " + cnfe);
                System.exit(0);
            }
        }
        //return connect;
        return null;
    }
}