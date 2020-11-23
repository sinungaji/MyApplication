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
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.example.myapplication.PengumumanActivity;
import com.example.myapplication.ProfilActivity;
import com.example.myapplication.R;
import com.example.myapplication.helper.Config;
import com.example.myapplication.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LihatProfil extends AppCompatActivity {
    ProgressDialog pDialog;
    SessionManager session;
    ImageView imgBack, fotoanggota;
    TextView namaAnggotaInput, jenisKelaminInput, tempatLahirInput, tanggalLahirInput, nikInput,
            alamatKtpInput, alamatSekarangInput, emailInput, npwpInput, noWaInput, noTelpInput, golonganDarahInput,
            pendidikanTerakhirInput, tanggalMasukKerjaInput, statusPernikahanInput, statusKepegawaianInput, jenjangInput;

    String namaAnggotaString, jenisKelaminString, tempatLahirString, tanggalLahirString, nikString,
            alamatKtpString, alamatSekarangString, emailString, npwpString, noWaString, noTelpString, golonganDarahString,
            pendidikanTerakhirString, tanggalMasukKerjaString, statusPernikahanString, statusKepegawaianString, jenjangString, fotoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_profil);
        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        imgBack = findViewById(R.id.imgBack);

        fotoanggota = findViewById(R.id.fotoanggota);

        namaAnggotaInput = findViewById(R.id.namaAnggotaET);
        jenisKelaminInput = findViewById(R.id.jenisKelaminET);
        tempatLahirInput = findViewById(R.id.tempatLahirET);
        tanggalLahirInput = findViewById(R.id.tanggalLahirET);
        nikInput = findViewById(R.id.nikET);
        alamatKtpInput = findViewById(R.id.alamatKtpET);
        alamatSekarangInput = findViewById(R.id.alamatSekarangET);
        emailInput = findViewById(R.id.emailET);
        npwpInput =findViewById(R.id.npwpET);
        noWaInput = findViewById(R.id.nomorWaET);
        noTelpInput = findViewById(R.id.nomorTelpET);
        golonganDarahInput = findViewById(R.id.golDarahET);
        pendidikanTerakhirInput = findViewById(R.id.pendidikanTerakhirET);
        tanggalMasukKerjaInput = findViewById(R.id.tanggalMasukKerjaET);
        statusPernikahanInput = findViewById(R.id.statusPernikahanET);
        statusKepegawaianInput = findViewById(R.id.statusKepegawaianET);
        jenjangInput = findViewById(R.id.jenjangET);

        getDataInformasiAkun();
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
    }

    private void getDataInformasiAkun() {
        // Tag used to cancel the request
        pDialog.setMessage("Informasi Akun...");
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
                                jenisKelaminString = responses.optString("jenis_kelamin");
                                tempatLahirString = responses.optString("tempat_lahir");
                                tanggalLahirString = responses.optString("tanggal_lahir");
                                nikString = responses.optString("nik");
                                alamatKtpString = responses.optString("alamat_ktp");
                                alamatSekarangString = responses.optString("alamat_sekarang");
                                emailString = responses.optString("email");
                                npwpString = responses.optString("npwp");
                                noWaString = responses.optString("no_wa");
                                noTelpString = responses.optString("no_telp");
                                golonganDarahString = responses.optString("golongan_darah");
                                pendidikanTerakhirString = responses.optString("pendidikan_terakhir");
                                tanggalMasukKerjaString = responses.optString("tgl_masuk_kerja");
                                statusPernikahanString = responses.optString("status_pernikahan");
                                statusKepegawaianString = responses.optString("status_kepegawaian");
                                jenjangString = responses.optString("jenjang");
                                fotoString = responses.optString("foto_anggota");

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
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(LihatProfil.this);
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
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LihatProfil.this);
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
        Glide.with(LihatProfil.this).load(fotoString).into(fotoanggota);
        namaAnggotaInput.setText(namaAnggotaString);
        jenisKelaminInput.setText(jenisKelaminString);
        tempatLahirInput.setText(tempatLahirString);
        tanggalLahirInput.setText(tanggalLahirString);
        nikInput.setText(nikString);
        alamatKtpInput.setText(alamatKtpString);
        alamatSekarangInput.setText(alamatSekarangString);
        emailInput.setText(emailString);
        npwpInput.setText(npwpString);
        noWaInput.setText(noWaString);
        noTelpInput.setText(noTelpString);
        golonganDarahInput.setText(golonganDarahString);
        pendidikanTerakhirInput.setText(pendidikanTerakhirString);
        tanggalMasukKerjaInput.setText(tanggalMasukKerjaString);
        statusPernikahanInput.setText(statusPernikahanString);
        statusKepegawaianInput.setText(statusKepegawaianString);
        jenjangInput.setText(jenjangString);
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