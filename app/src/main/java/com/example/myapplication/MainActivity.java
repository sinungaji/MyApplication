package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.example.myapplication.helper.Config;
import com.example.myapplication.model.MenuPengumumanPribadi;
import com.example.myapplication.helper.SessionManager;
import com.example.myapplication.helper.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<MenuPengumumanPribadi> MenuPengumumanPribadi;
    RecyclerView recyclerviewpengumumanpribadi;

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        recyclerviewpengumumanpribadi = findViewById(R.id.recycleviepengumumanpribadi);

        MenuPengumumanPribadi = new ArrayList<>();
        LinearLayoutManager b = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerviewpengumumanpribadi.setHasFixedSize(true);
        recyclerviewpengumumanpribadi.setLayoutManager(b);
        recyclerviewpengumumanpribadi.setNestedScrollingEnabled(true);

        actionButton();
        getDataPengumumanPribadi();

    }

    private void actionButton() {
    }

    private void getDataPengumumanPribadi() {
        pDialog.setMessage("Pengumuman Pribadi ...");
        MenuPengumumanPribadi.clear();
        showDialog();
        AndroidNetworking.post(Config.url+"pengumuman/list")
                .addBodyParameter("visibilitas", "1")
                .addBodyParameter("limit", "0")
                .addBodyParameter("offset", "5")
                .addBodyParameter("pencarian", "")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray responseB) {
                        hideDialog();
                        try {
                            for (int i = 0; i < responseB.length(); i++) {
                                JSONObject responsesB = responseB.getJSONObject(i);
                                Log.d("hehe", responsesB.optString("id_pengumuman"));
                                MenuPengumumanPribadi pp = new MenuPengumumanPribadi(
                                        responsesB.getString("id_pengumuman"),
                                        responsesB.getString("judul_pengumuman"),
                                        responsesB.getString("isi_pengumuman"),
                                        responsesB.getString("tgl_tayang_pengumuman"),
                                        responsesB.getString("tgl_berakhir_pengumuman"),
                                        responsesB.getString("Thumbnail"));

                                MenuPengumumanPribadi.add(pp);
                            }
                            MenuAdapterPengumumanPribadi menuAdapterPengumumanPribadi = new MenuAdapterPengumumanPribadi(getApplicationContext(), MenuPengumumanPribadi);
                            recyclerviewpengumumanpribadi.setAdapter(menuAdapterPengumumanPribadi);
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
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
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
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
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
    public class MenuAdapterPengumumanPribadi extends RecyclerView.Adapter<MenuAdapterPengumumanPribadi.ProductViewHolder> {
        private Context mCtb;
        private List<MenuPengumumanPribadi> MenuPengumumanPribadi;

        MenuAdapterPengumumanPribadi(Context mCtb, List<MenuPengumumanPribadi> MenuPegumumanPribadi) {
            this.mCtb = mCtb;
            this.MenuPengumumanPribadi = MenuPegumumanPribadi;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroupB, int b) {
            LayoutInflater inflaterB = LayoutInflater.from(mCtb);
            @SuppressLint("InflateParams") View viewB = inflaterB.inflate(R.layout.activity_pengumuman_pribadi, null);
            return new ProductViewHolder(viewB);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holderB, int b) {
            final com.example.myapplication.model.MenuPengumumanPribadi menuPengumumanPribadi = MenuPengumumanPribadi.get(b);
            Glide.with(MainActivity.this).load(menuPengumumanPribadi.getThumbnail()).into(holderB.tumbnailpengumumanpribadi);
            holderB.text_judul_pengumuman_pribadi.setText(menuPengumumanPribadi.getJudul_pengumuman());
            holderB.text_isi_pengumuman_pribadi.setText(menuPengumumanPribadi.getIsi_pengumuman());
            holderB.text_tgl_tayang_pengumuman_pribadi.setText(menuPengumumanPribadi.getTgl_tayang_pengumuman());
        }

        @Override
        public int getItemCount() {
            return MenuPengumumanPribadi.size();
        }

        class ProductViewHolder extends RecyclerView.ViewHolder {
            TextView text_judul_pengumuman_pribadi, text_isi_pengumuman_pribadi, text_tgl_tayang_pengumuman_pribadi;
            CardView cvPengumumanPribadi;
            ImageView tumbnailpengumumanpribadi;

            ProductViewHolder(View itemViewB) {
                super(itemViewB);
                cvPengumumanPribadi = itemViewB.findViewById(R.id.cvPengumumanPribadi);
                tumbnailpengumumanpribadi = itemView.findViewById(R.id.tumbnailpengumumanpribadi);
                text_judul_pengumuman_pribadi = itemViewB.findViewById(R.id.judulpengumumanPribadiTV);
                text_isi_pengumuman_pribadi = itemViewB.findViewById(R.id.isipengumumanPribadiTV);
                text_tgl_tayang_pengumuman_pribadi = itemViewB.findViewById(R.id.tgltayangpengumumanPribadiTV);

            }
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