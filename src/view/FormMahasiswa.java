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

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "NIM", "Jurusan", "Angkatan"}, 0);
        mahasiswaTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(mahasiswaTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input fields
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
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Tambah");
        JButton deleteButton = new JButton("Hapus");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add button action
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


        // Delete button action
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

        // Initial table data
        refreshTable();
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0); // Clear table
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
