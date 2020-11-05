package com.example.myapplication.berkaspengumuman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.FileLoader;
import com.example.myapplication.PengumumanActivity;
import com.example.myapplication.R;
import com.example.myapplication.helper.Config;
import com.example.myapplication.model.ModelBerkasPengumuman;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class BerkasTypeFilePdf extends AppCompatActivity implements OnLoadCompleteListener, OnPageErrorListener {
    private ProgressDialog pDialog;
    String id_pengumuman;
    PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkas_type_file_pdf);
        pdfView = findViewById(R.id.pdfView);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();
        id_pengumuman = bundle.getString("id_pengumuman");

        pdfView = findViewById(R.id.pdfView);
        FileLoader.with(this)
                .load("https://upload.wikimedia.org/wikipedia/commons/3/3c/Enrique_Simonet_-_Marina_veneciana_6MB.jpg")
                .checkFileintegrity(true)
                .fromDirectory("test4", FileLoader.DIR_EXTERNAL_PUBLIC)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        Glide.with(BerkasTypeFilePdf.this).load(response.getBody()).into(iv);
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        Log.d(TAG, "onError: " + t.getMessage());
                    }
                });


    }
    
    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

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