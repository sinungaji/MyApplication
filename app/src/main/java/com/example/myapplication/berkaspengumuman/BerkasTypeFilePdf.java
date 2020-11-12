package com.example.myapplication.berkaspengumuman;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.myapplication.PengumumanActivity;
import com.example.myapplication.R;
import com.example.myapplication.helper.Config;
import com.example.myapplication.model.ModelBerkasPengumuman;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import com.example.myapplication.model.ModelBerkasPengumuman;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class BerkasTypeFilePdf extends AppCompatActivity implements OnLoadCompleteListener, OnPageErrorListener {
    private String idBerkasString, judulfileBerkasString,tglpostingfileBerkasString, typefileBerkasString, fileBerkasString, keteranganBerkasString;
    
    private ProgressDialog pDialog;
    String id_pengumuman, path;
    PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkas_type_file_pdf);
        pdfView = findViewById(R.id.pdfView);
        
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();
        path = bundle.getString("id_pengumuman_berkas");
        deklarasiData();
        
    }

    private void deklarasiData() {
        FileLoader.with(this)
                .load(path) //2nd parameter is optioal, pass true to force load from network
                .fromDirectory("My_PDFs", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        File pdfFile = response.getBody();
                        try {
                            pdfView.fromFile(pdfFile)
                                    .defaultPage(1)
                                    .enableAnnotationRendering(true)
                                    .onLoad(BerkasTypeFilePdf.this)
                                    .scrollHandle(new DefaultScrollHandle(BerkasTypeFilePdf.this))
                                    .spacing(10) // in dp
                                    .onPageError(BerkasTypeFilePdf.this)
                                    .load();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        Toast.makeText(BerkasTypeFilePdf.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText(BerkasTypeFilePdf.this, String.valueOf(nbPages), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onPageError(int page, Throwable t) {
        Toast.makeText(BerkasTypeFilePdf.this, t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}