package com.example.myapplication.berkaspengumumanpribadi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.PengumumanPribadiActivity;
import com.example.myapplication.R;

public class BerkasPribadiTypeFileVideo extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkas_pribadi_type_file_video);

        //Inisialisasi VideoView, MediaController dan Button
        videoView = findViewById(R.id.video);

        mediaController = new MediaController(this);
        Intent intent = getIntent();
        final String path = intent.getStringExtra("file");
        Uri uri = Uri.parse(path);

        videoView.setVideoURI(uri);

        //Memasang MediaController untuk menampilkan tombol play, pause, dsb
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        //Menjalankan Video
        videoView.start();
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
}