package com.example.myapplication.berkaspengumuman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.PengumumanActivity;
import com.example.myapplication.PengumumanPribadiActivity;
import com.example.myapplication.R;

public class BerkasTypeFileVideo extends AppCompatActivity {

    //create class reference
    //Deklarasi Variable
    private VideoView videoView;
    private MediaController mediaController;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkas_type_file_video);

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
//                Intent intent = new Intent(getApplicationContext(), PengumumanActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}