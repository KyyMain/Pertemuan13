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

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 8));
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
        JButton nilaiButton = new JButton("Daftar Nilai");
        JButton addButton = new JButton("Tambah");
        JButton deleteButton = new JButton("Hapus");
        buttonPanel.add(nilaiButton);
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

        nilaiButton.addActionListener(e -> {
            int selectedRow = mahasiswaTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu!");
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0); // ID mahasiswa
            String nama = (String) tableModel.getValueAt(selectedRow, 1); // Nama mahasiswa
            String nim = (String) tableModel.getValueAt(selectedRow, 2); // NIM mahasiswa

            JDialog nilaiDialog = new JDialog(this, "Input Nilai", true);
            nilaiDialog.setSize(400, 250); // Tambahkan ukuran agar cukup untuk field baru
            nilaiDialog.setLayout(new BorderLayout());
            nilaiDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            JPanel nilaiPanel = new JPanel(new GridLayout(4, 2, 10, 8));
            nilaiPanel.setBorder(new EmptyBorder(20, 5, 5, 5));

            // TextField untuk nama, nim, mata kuliah, dan nilai
            JTextField namaFieldDialog = new JTextField(nama);
            namaFieldDialog.setEditable(false); // Nama mahasiswa tidak boleh diedit
            JTextField nimFieldDialog = new JTextField(nim);
            nimFieldDialog.setEditable(false); // NIM mahasiswa tidak boleh diedit
            JTextField mataKuliahField = new JTextField();
            JTextField nilaiField = new JTextField();

            nilaiPanel.add(new JLabel("Nama Mahasiswa:"));
            nilaiPanel.add(namaFieldDialog);

            nilaiPanel.add(new JLabel("NIM Mahasiswa:"));
            nilaiPanel.add(nimFieldDialog);

            nilaiPanel.add(new JLabel("Mata Kuliah:"));
            nilaiPanel.add(mataKuliahField);

            nilaiPanel.add(new JLabel("Nilai:"));
            nilaiPanel.add(nilaiField);

            nilaiDialog.add(nilaiPanel, BorderLayout.CENTER);

            JPanel buttonDialogPanel = new JPanel();
            JButton viewNilaiButton = new JButton("Lihat Nilai");
            JButton saveButton = new JButton("Simpan");
            JButton closeButton = new JButton("Tutup");

            buttonDialogPanel.add(viewNilaiButton);
            buttonDialogPanel.add(saveButton);
            buttonDialogPanel.add(closeButton);

            viewNilaiButton.addActionListener(ev -> {
                try {
                    List<String> nilaiList = controller.getNilaiByMahasiswaId(id);
                    if (nilaiList.isEmpty()) {
                        JOptionPane.showMessageDialog(nilaiDialog, "Tidak ada nilai untuk mahasiswa ini.");
                        return;
                    }

                    // Menampilkan nilai-nilai dalam dialog baru
                    StringBuilder nilaiDisplay = new StringBuilder("Nilai Mahasiswa:\n");
                    for (String nilai : nilaiList) {
                        nilaiDisplay.append(nilai).append("\n");
                    }
                    JOptionPane.showMessageDialog(nilaiDialog, nilaiDisplay.toString(), "Daftar Nilai", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(nilaiDialog, "Error: " + ex.getMessage());
                }
            });

            saveButton.addActionListener(ev -> {
                try {
                    String mataKuliah = mataKuliahField.getText();
                    double nilai = Double.parseDouble(nilaiField.getText());
                    controller.addNilai(id, mataKuliah, nilai);

                    JOptionPane.showMessageDialog(nilaiDialog, "Nilai berhasil disimpan!");
                    nilaiDialog.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(nilaiDialog, "Error: " + ex.getMessage());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(nilaiDialog, "Nilai harus berupa angka!");
                }
            });

            closeButton.addActionListener(ev -> nilaiDialog.dispose());

            nilaiDialog.add(buttonDialogPanel, BorderLayout.SOUTH);
            nilaiDialog.setLocationRelativeTo(this);
            nilaiDialog.setVisible(true);
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
