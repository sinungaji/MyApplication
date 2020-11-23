package com.example.myapplication.berkaspengumumanpribadi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.example.myapplication.MainActivity;
import com.example.myapplication.PengumumanPribadiActivity;
import com.example.myapplication.R;
import com.example.myapplication.berkaspengumuman.BerkasTypeFileFoto;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;

public class BerkasPribadiTypeFileFoto extends AppCompatActivity {
    String data;
    LinearLayout lini;
    ProgressDialog dialog;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkas_pribadi_type_file_foto);
        Intent intent = getIntent();
        data = intent.getStringExtra("file");
        lini = findViewById(R.id.lini);

        getBitmapFromURL();
        actionButton();
    }
    private void actionButton() {
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), PengumumanPribadiActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void getBitmapFromURL() {
        dialog = ProgressDialog.show(BerkasPribadiTypeFileFoto.this, "",
                "Loading. Please wait...", true);
        AndroidNetworking.get(data)
                .setPriority(Priority.MEDIUM)
//                .setBitmapMaxHeight(100)
//                .setBitmapMaxWidth(100)
                .setBitmapConfig(Bitmap.Config.ARGB_8888)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        // do anything with bitmap
                        dialog.dismiss();
                        PhotoView photoView = findViewById(R.id.imageView);
                        photoView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        dialog.dismiss();
                        if (error.getErrorCode() != 0) {
                            Snackbar snackbar = Snackbar
                                    .make(lini, "Internet anda lemah . .", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    }
                });
    }


}