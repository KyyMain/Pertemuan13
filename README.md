<h1>Praktikum 8</h1>

| Keterangan | Data Diri                |
| ---------- | ------------------- |
| **Nama**   | Eky Fikri Yamansyah |
| **NIM**    | 312310572           |
| **Kelas**  | TI.23.A6            |

## Project OOP berbasis MVC
### Requirements :
- Text Editor
- Library mysql-connector
- Mysql
- XAMPP

### Code :
#### Buat database :
```sql
CREATE DATABASE akademik;

USE akademik;

CREATE TABLE mahasiswa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100),
    nim VARCHAR(20) UNIQUE,
    jurusan VARCHAR(50)
);
```
#### Tambah package classes yang berisi :

Class Database :
```java
package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/akademik";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```
Class RowMapper :
```java
package classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
    T mapRow(ResultSet rs) throws SQLException;
}
```
#### Package Controller :
Class MahasiswaController :
```java
package controller;

import java.sql.SQLException;
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
}
```
#### Package Model :
Class Mahasiswa :
```java
package model;

import classes.BaseModel;

public class Mahasiswa extends BaseModel {
    private int id;
    private String nama;
    private String nim;
    private String jurusan;
    private int angkatan;

    public Mahasiswa(int id, String nama, String nim, String jurusan, int angkatan) {
        this.id = id;
        this.nama = nama;
        this.nim = nim;
        this.jurusan = jurusan;
        this.angkatan = angkatan;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public String getJurusan() {
        return jurusan;
    }

    public int getAngkatan() {
        return angkatan;
    }
}
```
Class MahasiswaModel :
```java
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
```
### Package View
Class FormMahasiswa :
```java
package view;

import controller.MahasiswaController;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Mahasiswa;

public class FormMahasiswa extends JFrame {
    private MahasiswaController controller;
    private DefaultTableModel tableModel;
    private JTable mahasiswaTable;

    public FormMahasiswa() {
        controller = new MahasiswaController();

        setTitle("Manajemen Mahasiswa");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "NIM", "Jurusan", "Angkatan"}, 0);
        mahasiswaTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(mahasiswaTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2,10,8));
        JTextField namaField = new JTextField();
        JTextField nimField = new JTextField();
        JTextField jurusanField = new JTextField();
        JTextField angkatanField = new JTextField();

        inputPanel.setBorder(new EmptyBorder(20, 5, 5, 5));
        inputPanel.add(new JLabel("Nama:"));
        inputPanel.add(namaField);
        inputPanel.add(new JLabel("NIM:"));
        inputPanel.add(nimField);
        inputPanel.add(new JLabel("Jurusan:"));
        inputPanel.add(jurusanField);
        inputPanel.add(new JLabel("Angkatan:"));
        inputPanel.add(angkatanField);
        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton nilai = new JButton("Daftar Nilai");
        JButton addButton = new JButton("Tambah");
        JButton deleteButton = new JButton("Hapus");
        buttonPanel.add(nilai);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            try {
                controller.addMahasiswa(
                    namaField.getText(),
                    nimField.getText(),
                    jurusanField.getText(),
                    Integer.parseInt(angkatanField.getText())
                );
                refreshTable();
                JOptionPane.showMessageDialog(this, "Mahasiswa berhasil ditambahkan!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int selectedRow = mahasiswaTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    controller.deleteMahasiswa(id);
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting mahasiswa: " + ex.getMessage());
            }
        });


        refreshTable();
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Mahasiswa> mahasiswaList = controller.getAllMahasiswa();
            for (Mahasiswa m : mahasiswaList) {
                tableModel.addRow(new Object[]{
                    m.getId(), m.getNama(), m.getNim(), m.getJurusan(), m.getAngkatan()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading mahasiswa: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormMahasiswa().setVisible(true));
    }
}
```
## Tampilan :
![gambar](doc/app.png)
