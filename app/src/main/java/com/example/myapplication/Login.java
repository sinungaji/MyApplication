package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.myapplication.helper.Config;
import com.example.myapplication.helper.Controler;
import com.example.myapplication.helper.SessionManager;
import com.example.myapplication.helper.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Login extends AppCompatActivity {
    private String divace, IMEINumber;
    private static final int REQUEST_CODE = 101;
    private ProgressDialog pDialog;
    private SessionManager session;
    private EditText inputkatasandi, inputAbsen;

    @Override
    protected void onStop() {
        super.onStop();
        hideDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // check user is already logged in
        if (session.isLoggedIn()) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        inputAbsen = findViewById(R.id.inputAbsen);
        inputkatasandi = findViewById(R.id.inputkatasandi);


        controlData();
        actionData();

    }

    private void controlData() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            return;
        }
//try {
//    IMEINumber = telephonyManager.getDeviceId();
//}catch (IOException e){
//
//}
        divace = Controler.getDeviceName();
    }

    private void actionData() {
        // Login button Click Event
        findViewById(R.id.regist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.setMessage("Memuat Tampilan . .");
                showDialog();
                Intent i = new Intent(Login.this, WebView.class);
                i.putExtra("url", "http://andiglobalsoft.com/");
                startActivity(i);
            }
        });

        findViewById(R.id.btnlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = inputAbsen.getText().toString().trim();
                String pass_login = inputkatasandi.getText().toString().trim();

                if (username.isEmpty()) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                    builder1.setMessage("Id anggota tidak boleh kosong!\nklik 'Oke' untuk menutup pesan ini");
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
                    inputAbsen.setError("Id anggota tidak boleh kosong!");

                } else if (pass_login.isEmpty()) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                    builder1.setMessage("Kata sandi tidak boleh kosong!\nklik 'Oke' untuk menutup pesan ini");
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
                    inputkatasandi.setError("Kata sandi tidak boleh kosong!");

                } else {

                    saveData(username, pass_login);

                }
            }
        });
    }

    private void saveData(final String username, final String pass_login) {
        // Tag used to cancel the request
        pDialog.setMessage("Logging in ...");
        showDialog();
        AndroidNetworking.post(Config.url + "login.php")
                .addBodyParameter("id_anggota", username)
                .addBodyParameter("password", pass_login)
                .addBodyParameter("imei", "0")
                .addBodyParameter("perangkat", divace)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hideDialog();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject responses = response.getJSONObject(i);
                                Log.d("data1", responses.optString("id_anggota"));
                                Log.d("data1", responses.optString("kode_anggota"));

                                loginProcess(responses.optString("id_anggota"), responses.optString("kode_anggota"));
                            }

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
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
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
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
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

    private void loginProcess(final String id_anggota, final String absen) {
        // Tag used to cancel the request
        pDialog.setMessage("Memuat tampilan");
        showDialog();

        session.createLoginSession(id_anggota, absen);
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();


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