// MahasiswaModel.java
package model;

import classes.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaModel {
    public List<Mahasiswa> getAllMahasiswa() throws SQLException {
        List<Mahasiswa> list = new ArrayList<>();
        String query = "SELECT * FROM mahasiswa";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Mahasiswa(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("nim"),
                    rs.getString("jurusan"),
                    rs.getInt("angkatan")
                ));
            }
        }
        return list;
    }
    public void addNilai(int idMahasiswa, String mataKuliah, double nilai) throws SQLException {
        String query = "INSERT INTO nilai (id_mahasiswa, mata_kuliah, nilai) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idMahasiswa);
            stmt.setString(2, mataKuliah);
            stmt.setDouble(3, nilai);
            stmt.executeUpdate();
        }
    }
public List<String> getNilaiByMahasiswaId(int mahasiswaId) throws SQLException {
    List<String> nilaiList = new ArrayList<>();
    String query = "SELECT mata_kuliah, nilai FROM nilai WHERE id_mahasiswa = ?";
    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, mahasiswaId);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String mataKuliah = rs.getString("mata_kuliah");
                double nilai = rs.getDouble("nilai");
                nilaiList.add(mataKuliah + ": " + nilai);
            }
        }
    }
    return nilaiList;
}

    public void addMahasiswa(String nama, String nim, String jurusan, int angkatan) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM mahasiswa WHERE nim = ?";
        String insertQuery = "INSERT INTO mahasiswa (nama, nim, jurusan, angkatan) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setString(1, nim);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Mahasiswa dengan NIM ini sudah ada.");
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, nama);
                insertStmt.setString(2, nim);
                insertStmt.setString(3, jurusan);
                insertStmt.setInt(4, angkatan);
                insertStmt.executeUpdate();
            }
        }
    }


    public void deleteMahasiswa(int id) throws SQLException {
        String deleteQuery = "DELETE FROM mahasiswa WHERE id = ?";
        String resetAutoIncrementQuery = "ALTER TABLE mahasiswa AUTO_INCREMENT = 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();

            try (Statement resetStmt = conn.createStatement()) {
                resetStmt.executeUpdate(resetAutoIncrementQuery);
            }
        }
    }

}
