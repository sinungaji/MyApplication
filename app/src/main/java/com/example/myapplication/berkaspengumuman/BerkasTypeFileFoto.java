package com.example.myapplication.berkaspengumuman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.example.myapplication.PengumumanActivity;
import com.example.myapplication.R;
import com.example.myapplication.helper.Config;
import com.example.myapplication.model.ModelBerkasPengumuman;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BerkasTypeFileFoto extends AppCompatActivity {
    private ImageView profileUser, imgBack, tumbnailBerkas;
    private ProgressDialog pDialog;

    private String tumbnailBerkasString;

    private String id_pengumuman_berkas;

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkas_type_file_foto);

        Bundle bundle = getIntent().getExtras();
        id_pengumuman_berkas = bundle.getString("id_pengumuman_berkas");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        imgBack = findViewById(R.id.imgBack);
        tumbnailBerkas = findViewById(R.id.tumbnailBerkas);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        getDataBerkas();

    }

    private void getDataBerkas() {
        // Tag used to cancel the request
        pDialog.setMessage("Pengumuman Berkas ...");
        showDialog();
        AndroidNetworking.post(Config.url + "pengumuman/berkas")
                .addBodyParameter("id_pengumuman_berkas", id_pengumuman_berkas)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hideDialog();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject responses = response.getJSONObject(i);
                                Log.d("hehe", responses.optString("id_pengumuman"));
                                responses.getString("id_pengumuman_berkas");
                                responses.getString("judul_file");
                                responses.getString("tgl_posting_file");
                                responses.getString("type_file");
                                tumbnailBerkasString = responses.getString("file");
                                responses.getString("keterangan_file");

                            }

                            deklarasidata();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        hideDialog();
                        Log.d("data1", "onError errorCode : " + error.getErrorCode());
                        Log.d("data1", "onError errorBody : " + error.getErrorBody());
                        Log.d("data1", "onError errorDetail : " + error.getErrorDetail());

                        if (error.getErrorCode() == 400) {
                            try {
                                JSONObject body = new JSONObject(error.getErrorBody());
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(BerkasTypeFileFoto.this);
                                builder1.setMessage(body.optString("pesan"));
                                builder1.setCancelable(false);

                                builder1.setPositiveButton(
                                        "Oke",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } catch (JSONException ignored) {

                            }

                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(BerkasTypeFileFoto.this);
                            builder1.setMessage("Jaringan sedang sibuk. Klik 'Ok', untuk menutup pesan ini!");
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(
                                    "Oke",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }

                    }
                });

    }

    private void deklarasidata() {
        Glide.with(BerkasTypeFileFoto.this).load(tumbnailBerkasString).into(tumbnailBerkas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            tumbnailBerkas.setScaleX(mScaleFactor);
            tumbnailBerkas.setScaleY(mScaleFactor);
            return true;
        }
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