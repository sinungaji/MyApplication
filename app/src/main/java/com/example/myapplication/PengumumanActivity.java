package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.example.myapplication.helper.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PengumumanActivity extends AppCompatActivity {
    private ImageView profileUser, imgBack, tumbnailpengumumanumum;
    private ProgressDialog pDialog;
    String id_pengumuman;

    private TextView judulpengumumanInput, isipengumumanInput, tgltayangpengumumanInput, tglberakhirpengumumanInput;
    private String judulpengumumanString, isipengumumanString, tgltayangpengumumanString, tglberakhirpengumumanString, tumbnailpengumumanumumString;

    private String judulpengumumanStringBerkas, isipengumumanStringBerkas, tgltayangpengumumanStringBerkas, tglberakhirpengumumanStringBerkas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman);
        Bundle bundle = getIntent().getExtras();
        id_pengumuman = bundle.getString("id_pengumuman");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        imgBack = findViewById(R.id.imgBack);
        tumbnailpengumumanumum = findViewById(R.id.tumbnailpengumumanumum);
        judulpengumumanInput = findViewById(R.id.inputJudulPengumuman);
        isipengumumanInput = findViewById(R.id.inputisipengumuman);
        tgltayangpengumumanInput = findViewById(R.id.inputtgltayangpengumuman);
        tglberakhirpengumumanInput = findViewById(R.id.inputtglberakhirpengumuman);

        actionButton();
        getDataPengumuman();
        Log.d("data1", id_pengumuman);
    }

    private void actionButton() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataPengumuman() {
        // Tag used to cancel the request
        pDialog.setMessage("Pengumuman Pribadi ...");
        showDialog();
        AndroidNetworking.post(Config.url + "pengumuman/detail")
                .addBodyParameter("id_pengumuman", id_pengumuman)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hideDialog();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject responses = response.getJSONObject(i);
                                tumbnailpengumumanumumString = responses.getString("Thumbnail");
                                judulpengumumanString = responses.getString("judul_pengumuman");
                                isipengumumanString = responses.getString("isi_pengumuman");
                                tgltayangpengumumanString = responses.getString("tgl_tayang_pengumuman");
                                tglberakhirpengumumanString = responses.getString("tgl_berakhir_pengumuman");

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
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PengumumanActivity.this);
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
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PengumumanActivity.this);
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
        Glide.with(PengumumanActivity.this).load(tumbnailpengumumanumumString).into(tumbnailpengumumanumum);
        judulpengumumanInput.setText(judulpengumumanString);
        isipengumumanInput.setText(isipengumumanString);
        tgltayangpengumumanInput.setText(tgltayangpengumumanString);
        tglberakhirpengumumanInput.setText(tglberakhirpengumumanString);
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