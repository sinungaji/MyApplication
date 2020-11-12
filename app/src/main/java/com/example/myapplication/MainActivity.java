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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.example.myapplication.berkaspengumuman.BerkasTypeFileVideo;
import com.example.myapplication.helper.Config;
import com.example.myapplication.model.MenuPengumumanPribadi;
import com.example.myapplication.helper.SessionManager;
import com.example.myapplication.helper.WebView;
import com.example.myapplication.model.MenuPengumumanUmum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ArrayList<MenuPengumumanPribadi> MenuPengumumanPribadi;
    RecyclerView recyclerviewpengumumanpribadi;

    ArrayList<MenuPengumumanUmum> MenuPengumumanUmum;
    RecyclerView recycleviepengumumanumum;

    private ProgressDialog pDialog;
    private ImageView profileUser, searchView, imgMenu;

    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        searchView = findViewById(R.id.imgSearch);
        imgMenu = findViewById(R.id.img);

        recyclerviewpengumumanpribadi = findViewById(R.id.recycleviepengumumanpribadi);
        recycleviepengumumanumum = findViewById(R.id.recycleviepengumumanumum);

        MenuPengumumanPribadi = new ArrayList<>();
        LinearLayoutManager b = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerviewpengumumanpribadi.setHasFixedSize(true);
        recyclerviewpengumumanpribadi.setLayoutManager(b);
        recyclerviewpengumumanpribadi.setNestedScrollingEnabled(true);

        MenuPengumumanUmum = new ArrayList<>();
        LinearLayoutManager c = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recycleviepengumumanumum.setHasFixedSize(true);
        recycleviepengumumanumum.setLayoutManager(c);
        recycleviepengumumanumum.setNestedScrollingEnabled(true);

        actionButton();
        getDataPengumumanPribadi();
        getDataPengumumanUmum();
    }

    private void actionButton() {
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu optionmenu = new PopupMenu(MainActivity.this, view);
                optionmenu.setOnMenuItemClickListener(MainActivity.this);
                optionmenu.inflate(R.menu.optionmenu);
                optionmenu.show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
//        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.about:
                // do your code
//                Intent intent = new Intent(getApplicationContext(), BerkasTypeFileVideo.class);
//                startActivity(intent);
                return true;
            case R.id.setting:
                // do your code
                return true;
            case R.id.help:
                // do your code
                return true;
            case R.id.logout:
                // do your code
                session.logoutUser();
                finish();
                return true;
            default:
                return false;
        }
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
            holderB.cvPengumumanPribadi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, PengumumanPribadiActivity.class);
                    i.putExtra("id_pengumuman", menuPengumumanPribadi.getId_pengumuman());
                    startActivity(i);
                }
            });
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

    private void getDataPengumumanUmum() {
        pDialog.setMessage("Pengumuman Bawah ...");
        MenuPengumumanUmum.clear();
        showDialog();
        AndroidNetworking.post(Config.url+"pengumuman/list")
                .addBodyParameter("visibilitas", "0")
                .addBodyParameter("limit", "0")
                .addBodyParameter("offset", "5")
                .addBodyParameter("pencarian", "")
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
                                MenuPengumumanUmum bk = new MenuPengumumanUmum(
                                        responses.getString("id_pengumuman"),
                                        responses.getString("judul_pengumuman"),
                                        responses.getString("isi_pengumuman"),
                                        responses.getString("tgl_tayang_simple"),
                                        responses.getString("tgl_tayang_pengumuman"),
                                        responses.getString("tgl_berakhir_pengumuman"),
                                        responses.getString("Thumbnail"));

                                Log.d("hehe", responses.optString("id_pengumuman"));
                                MenuPengumumanUmum.add(bk);
                            }
                            MenuAdapter menu = new MenuAdapter(getApplicationContext(), MenuPengumumanUmum);
                            recycleviepengumumanumum.setAdapter(menu);
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

    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ProductViewHolder> {
        private Context mCtc;
        private List<MenuPengumumanUmum> MenuPengumumanUmum;

        MenuAdapter(Context mCtc, List<MenuPengumumanUmum> MenuPengumumanUmum) {
            this.mCtc = mCtc;
            this.MenuPengumumanUmum = MenuPengumumanUmum;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(mCtc);
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_pengumuman_umum, null);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int i) {
            final com.example.myapplication.model.MenuPengumumanUmum menu = MenuPengumumanUmum.get(i);
            holder.text_tanggal_tayang.setText(menu.getTgl_tayang_simple());
            Glide.with(MainActivity.this).load(menu.getThumbnail()).into(holder.tumbnailpengumumanumum);
            holder.text_judul_pengumuman.setText(menu.getJudul_pengumuman());
            holder.text_isi_pengumuman.setText(menu.getIsi_pengumuman());
            holder.text_tgl_tayang_pengumuman.setText(menu.getTgl_tayang_pengumuman());
            holder.text_tgl_berakhir_pengumuman.setText(menu.getTgl_berakhir_pengumuman());
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, PengumumanActivity.class);
                    i.putExtra("id_pengumuman", menu.getId_pengumuman());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return MenuPengumumanUmum.size();
        }

        class ProductViewHolder extends RecyclerView.ViewHolder {
            TextView text_tanggal_tayang, text_judul_pengumuman, text_isi_pengumuman, text_img,
                    text_tgl_tayang_pengumuman, text_tgl_berakhir_pengumuman;
            ImageView tumbnailpengumumanumum;
            CardView cv;

            ProductViewHolder(View itemView) {
                super(itemView);
                cv = itemView.findViewById(R.id.cvPengumumanumum);
                tumbnailpengumumanumum = itemView.findViewById(R.id.tumbnailpengumumanumum);
                text_tanggal_tayang = itemView.findViewById(R.id.tanggalTayangTV);
                text_judul_pengumuman = itemView.findViewById(R.id.judulpengumumanumumTV);
                text_isi_pengumuman = itemView.findViewById(R.id.isipengumumanumumTV);
                text_tgl_tayang_pengumuman = itemView.findViewById(R.id.tgltayangpengumumanumumTV);
                text_tgl_berakhir_pengumuman = itemView.findViewById(R.id.tglberakhirpengumumanumumTV);
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