package com.dka.sigmaipb;

import java.sql.SQLException;

/**
 * Created by Deni Kurnia Aldian on 11/7/2015.
 */

public class TestDB {
    public static void main(String[] args) throws SQLException {
        KoneksiDB connection = new KoneksiDB();
        connection.getKoneksi();
    }
}