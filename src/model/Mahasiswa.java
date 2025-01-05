// Mahasiswa.java
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
