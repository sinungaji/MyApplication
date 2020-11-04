package com.example.myapplication.model;

public class MenuPengumumanUmum { private String id_pengumuman;
    private String judul_pengumuman;
    private String isi_pengumuman;
    private String tgl_tayang_simple;
    private String tgl_tayang_pengumuman;
    private String tgl_berakhir_pengumuman;
    private String Thumbnail;

    public MenuPengumumanUmum(String id_pengumuman, String judul_pengumuman, String isi_pengumuman, String tgl_tayang_simple, String tgl_tayang_pengumuman, String tgl_berakhir_pengumuman, String thumbnail) {
        this.id_pengumuman = id_pengumuman;
        this.judul_pengumuman = judul_pengumuman;
        this.isi_pengumuman = isi_pengumuman;
        this.tgl_tayang_simple = tgl_tayang_simple;
        this.tgl_tayang_pengumuman = tgl_tayang_pengumuman;
        this.tgl_berakhir_pengumuman = tgl_berakhir_pengumuman;
        this.Thumbnail = thumbnail;
    }

    public String getId_pengumuman() {
        return id_pengumuman;
    }

    public String getJudul_pengumuman() {
        return judul_pengumuman;
    }

    public String getIsi_pengumuman() {
        return isi_pengumuman;
    }

    public String getTgl_tayang_simple() {
        return tgl_tayang_simple;
    }

    public String getTgl_tayang_pengumuman() {
        return tgl_tayang_pengumuman;
    }

    public String getTgl_berakhir_pengumuman() {
        return tgl_berakhir_pengumuman;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

}
