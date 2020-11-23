package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.example.myapplication.helper.SessionManager;
import com.example.myapplication.user.LihatProfil;
import com.example.myapplication.user.Pemberitahuan;
import com.example.myapplication.user.RubahPassword;
import com.example.myapplication.user.Tentang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfilActivity extends AppCompatActivity {
    SessionManager session;
    ImageView imgBack, imgfotoprofilET;
    CardView cvLihatProfil, cvRubahPassword, cvTentang, cvPemberitahuan, cvKeluar;
    ProgressDialog pDialog;

    TextView namaAnggotaInput, noWaInput, emailInput, tempatLahirInput ;

    String namaAnggotaString, noWaString, emailString, tempatLahirString , fotoString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        session = new SessionManager(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        imgBack = findViewById(R.id.imgBack);
        cvLihatProfil = findViewById(R.id.cvLihatProfil);
        cvRubahPassword = findViewById(R.id.cvResetPassword);
        cvTentang = findViewById(R.id.cvTentang);
        cvPemberitahuan = findViewById(R.id.cvPemberitahuan);
        cvKeluar = findViewById(R.id.cvKeluar);

        imgfotoprofilET = findViewById(R.id.imgfotoprofilET);

        namaAnggotaInput = findViewById(R.id.nama_anggotaET);
        noWaInput = findViewById(R.id.no_waET);
        emailInput = findViewById(R.id.emailET);
        tempatLahirInput = findViewById(R.id.tempat_lahirET);

        getDataInformasiAkun();
        actionButton();
    }

    private void actionButton() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        cvLihatProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LihatProfil.class);
                startActivity(intent);
            }
        });

        cvRubahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RubahPassword.class);
                startActivity(intent);
            }
        });

        cvTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Tentang.class);
                startActivity(intent);
            }
        });

        cvPemberitahuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Pemberitahuan.class);
                startActivity(intent);
            }
        });

        cvKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
                finish();
            }
        });

    }

    private void getDataInformasiAkun() {
        // Tag used to cancel the request
        pDialog.setMessage("Pengguna...");
        showDialog();
        AndroidNetworking.post(Config.url + "profil.php")
                .addBodyParameter("id_anggota", "3006")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hideDialog();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject responses = response.getJSONObject(i);
                                namaAnggotaString = responses.optString("nama_anggota");
                                                    responses.optString("jenis_kelamin");
                                tempatLahirString = responses.optString("tempat_lahir");
                                                    responses.optString("tanggal_lahir");
                                                    responses.optString("nik");
                                                    responses.optString("alamat_ktp");
                                                    responses.optString("alamat_sekarang");
                                emailString      =  responses.optString("email");
                                                    responses.optString("npwp");
                                noWaString       =  responses.optString("no_wa");
                                                    responses.optString("no_telp");
                                                    responses.optString("golongan_darah");
                                                    responses.optString("pendidikan_terakhir");
                                                    responses.optString("tgl_masuk_kerja");
                                                    responses.optString("status_pernikahan");
                                                    responses.optString("status_kepegawaian");
                                                    responses.optString("jenjang");
                                fotoString       =  responses.optString("foto_anggota");

                            }
                            deklarasiData();

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
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfilActivity.this);
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
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfilActivity.this);
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

    private void deklarasiData() {
        Glide.with(ProfilActivity.this).load(fotoString).into(imgfotoprofilET);
        namaAnggotaInput.setText(namaAnggotaString);
        tempatLahirInput.setText(tempatLahirString);
        emailInput.setText(emailString);
        noWaInput.setText(noWaString);
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