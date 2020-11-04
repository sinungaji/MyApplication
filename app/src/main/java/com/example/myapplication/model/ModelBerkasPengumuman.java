package com.example.myapplication.model;

public class ModelBerkasPengumuman {
    private String id_pengumuman_berkas;
    private String judul_file;
    private String tgl_posting_file;
    private String type_file;
    private String file;
    private String keterangan_file;

    public ModelBerkasPengumuman(String id_pengumuman_berkas, String judul_file, String tgl_posting_file, String type_file, String file, String keterangan_file) {
        this.id_pengumuman_berkas = id_pengumuman_berkas;
        this.judul_file = judul_file;
        this.tgl_posting_file = tgl_posting_file;
        this.type_file = type_file;
        this.file = file;
        this.keterangan_file = keterangan_file;
    }

    public String getId_pengumuman_berkas() {
        return id_pengumuman_berkas;
    }

    public String getJudul_file() {
        return judul_file;
    }

    public String getTgl_posting_file() {
        return tgl_posting_file;
    }

    public String getType_file() {
        return type_file;
    }

    public String getFile() {
        return file;
    }

    public String getKeterangan_file() {
        return keterangan_file;
    }
}