// MahasiswaController.java
package controller;

import model.Mahasiswa;
import model.MahasiswaModel;
import java.sql.SQLException;
import java.util.List;

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
}
