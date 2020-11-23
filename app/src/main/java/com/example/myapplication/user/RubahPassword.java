package com.example.myapplication.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.myapplication.MainActivity;
import com.example.myapplication.ProfilActivity;
import com.example.myapplication.R;
import com.example.myapplication.helper.Config;
import com.example.myapplication.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class RubahPassword extends AppCompatActivity {
    private ProgressDialog pDialog;
    private SessionManager session;
    private ImageView imgBack;
    private EditText inputKataSandiSaatIniET, inputKataSandiBaruET, inputKonfirmasiKataSandiBaruET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubah_password);
        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        imgBack = findViewById(R.id.imgBack);
        inputKataSandiSaatIniET = findViewById(R.id.inputKataSandiSaatIniET);
        inputKataSandiBaruET = findViewById(R.id.inputKataSandiBaruET);
        inputKonfirmasiKataSandiBaruET = findViewById(R.id.inputKonfirmasiKataSandiBaru);

        actionData();

    }

    private void actionData() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kata_sandi_saat_ini = inputKataSandiSaatIniET.getText().toString().trim();
                String kata_sandi_baru = inputKataSandiBaruET.getText().toString().trim();
                String kata_sandi_baru_konfirmasi = inputKonfirmasiKataSandiBaruET.getText().toString().trim();
                if (kata_sandi_saat_ini.isEmpty()) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(RubahPassword.this);
                    builder1.setMessage("Kata sandi saat ini tidak boleh kosong!\nklik 'Oke' untuk menutup pesan ini");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "oke",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    inputKataSandiSaatIniET.setError("Kata sandi saat ini tidak boleh kosong!");

                } else if (kata_sandi_baru.isEmpty()) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(RubahPassword.this);
                    builder1.setMessage("Kata sandi baru tidak boleh kosong!\nklik 'Oke' untuk menutup pesan ini");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "oke",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    inputKataSandiBaruET.setError("Kata sandi baru tidak boleh kosong!");

                } else if (kata_sandi_baru_konfirmasi.isEmpty()) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(RubahPassword.this);
                    builder1.setMessage("Konfirmasi kata sandi baru tidak boleh kosong!\nklik 'Oke' untuk menutup pesan ini");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "oke",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    inputKataSandiBaruET.setError("Konfirmasi kata sandi baru tidak boleh kosong!");

                } else if (!kata_sandi_baru.equals(kata_sandi_baru_konfirmasi)) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(RubahPassword.this);
                    builder1.setMessage("Pastikan kata sandi yang anda masukan sama !\nKlik `OK` untuk menutup pesan ini");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "oke",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    inputKataSandiBaruET.setError("Pastikan kata sandi yang anda masukan sama!");
                    inputKonfirmasiKataSandiBaruET.setError("Pastikan kata sandi yang anda masukan sama!");

                } else {

                    saveData(kata_sandi_saat_ini, kata_sandi_baru, kata_sandi_baru_konfirmasi);

                }
            }
        });

    }

    private void saveData(final String kata_sandi_saat_ini, final String kata_sandi_baru, final String kata_sandi_baru_konfirmasi) {
        // Tag used to cancel the request
        pDialog.setMessage("Rubah Kata Sandi...");
        showDialog();
        AndroidNetworking.post(Config.url + "lupapassword.php")
                .addBodyParameter("id_anggota", "3006")
                .addBodyParameter("password_lama", kata_sandi_saat_ini)
                .addBodyParameter("password_baru", kata_sandi_baru)
                .addBodyParameter("password_ulangi", kata_sandi_baru_konfirmasi)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(RubahPassword.this);
                        builder1.setMessage(response.optString("pesan"));
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Oke",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();

                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();


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
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(RubahPassword.this);
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
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(RubahPassword.this);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}