package controller;

import classes.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Mahasiswa;
import model.MahasiswaModel;

public class MahasiswaController {
    private MahasiswaModel model;

    public MahasiswaController() {
        this.model = new MahasiswaModel();
    }

    public List<Mahasiswa> getAllMahasiswa() throws SQLException {
        return model.getAllMahasiswa();
    }

    public void addMahasiswa(String nama, String nim, String jurusan, int angkatan) throws SQLException {
        model.addMahasiswa(nama, nim, jurusan, angkatan);
    }

    public void deleteMahasiswa(int id) throws SQLException {
        model.deleteMahasiswa(id);
    }

    public void addNilai(int idMahasiswa, String mataKuliah, double nilai) throws SQLException {
        model.addNilai(idMahasiswa, mataKuliah, nilai);
    }
    public List<String> getNilaiByMahasiswaId(int mahasiswaId) throws SQLException {
    String query = "SELECT mata_kuliah, nilai FROM nilai WHERE id_mahasiswa = ?";
    List<String> nilaiList = new ArrayList<>();

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, mahasiswaId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String mataKuliah = rs.getString("mata_kuliah");
            double nilai = rs.getDouble("nilai");
            nilaiList.add(mataKuliah + ": " + nilai);
        }
    }

    return nilaiList;
}


}
