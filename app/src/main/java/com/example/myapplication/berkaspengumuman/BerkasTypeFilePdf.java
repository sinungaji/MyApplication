package com.example.myapplication.berkaspengumuman;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.myapplication.MainActivity;
import com.example.myapplication.PengumumanActivity;
import com.example.myapplication.PengumumanPribadiActivity;
import com.example.myapplication.R;
import com.example.myapplication.helper.Config;
import com.example.myapplication.model.ModelBerkasPengumuman;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
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
    ProgressBar pdfViewProgressBar;
    public ProgressDialog pDialog;
    PDFView pdfView;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkas_type_file_pdf);
        pdfView= findViewById(R.id.pdfView);
        pdfViewProgressBar=findViewById(R.id.pdfViewProgressBar);
        pdfViewProgressBar.setVisibility(View.VISIBLE);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        loadData();
        actionButton();
    }

    private void actionButton() {
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), PengumumanActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void loadData() {
        pDialog.setMessage("Memuat Data . .");
        showDialog();
        //UNPACK OUR DATA FROM INTENT
        Intent i=this.getIntent();
        final String path=i.getExtras().getString("file");

        FileLoader.with(this)
                .load(path,false) //2nd parameter is optioal, pass true to force load from network
                .fromDirectory("My_PDFs", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        pdfViewProgressBar.setVisibility(View.GONE);
                        File pdfFile = response.getBody();
                        try {

                            pdfView.fromFile(pdfFile)
                                    .defaultPage(0)
                                    .swipeHorizontal(false)
                                    .enableAnnotationRendering(true)
                                    .onLoad(new OnLoadCompleteListener() {
                                        @Override
                                        public void loadComplete(int nbPages) {
                                            float pageWidth = pdfView.getOptimalPageWidth();
                                            float viewWidth = pdfView.getWidth();
                                            pdfView.zoomTo(viewWidth/pageWidth);
                                            pdfView.loadPages();
                                        }
                                    })
                                    .onRender(new OnRenderListener() {
                                        @Override
                                        public void onInitiallyRendered(int pages, float pageWidth,
                                                                        float pageHeight) {
                                            pdfView.fitToWidth(); // optionally pass page number
                                        }
                                    })
                                    .scrollHandle(new DefaultScrollHandle(BerkasTypeFilePdf.this))
                                    .spacing(10) // in dp
                                    .onPageError(BerkasTypeFilePdf.this)
                                    .load();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hideDialog();
                    }
                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        pdfViewProgressBar.setVisibility(View.GONE);
                        hideDialog();
                        Toast.makeText(BerkasTypeFilePdf.this, "KONEKSI ANDA BURUK !", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    @Override
    public void loadComplete(int nbPages) {
        pdfViewProgressBar.setVisibility(View.GONE);
        Toast.makeText(BerkasTypeFilePdf.this, String.valueOf(nbPages), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onPageError(int page, Throwable t) {
        pdfViewProgressBar.setVisibility(View.GONE);
        Toast.makeText(BerkasTypeFilePdf.this, t.getMessage(), Toast.LENGTH_LONG).show();
        hideDialog();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // do something on back.
            hideDialog();
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}