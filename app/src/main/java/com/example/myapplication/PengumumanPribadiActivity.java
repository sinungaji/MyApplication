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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.example.myapplication.berkaspengumuman.BerkasTypeFileDefault;
import com.example.myapplication.berkaspengumuman.BerkasTypeFileFoto;
import com.example.myapplication.berkaspengumuman.BerkasTypeFilePdf;
import com.example.myapplication.berkaspengumuman.BerkasTypeFileVideo;
import com.example.myapplication.berkaspengumumanpribadi.BerkasPribadiTypeFileDefault;
import com.example.myapplication.berkaspengumumanpribadi.BerkasPribadiTypeFileFoto;
import com.example.myapplication.berkaspengumumanpribadi.BerkasPribadiTypeFilePdf;
import com.example.myapplication.berkaspengumumanpribadi.BerkasPribadiTypeFileVideo;
import com.example.myapplication.helper.Config;
import com.example.myapplication.model.ModelBerkasPengumuman;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PengumumanPribadiActivity extends AppCompatActivity {
    ArrayList<com.example.myapplication.model.ModelBerkasPengumuman> ModelBerkasPengumuman;
    RecyclerView recycleviewberkaspengumuman;

    private ImageView profileUser, imgBack, tumbnailpengumumanumum;
    private ProgressDialog pDialog;
    String id_pengumuman;

    private TextView judulpengumumanInput, isipengumumanInput, tgltayangpengumumanInput, tglberakhirpengumumanInput;
    private TextView judulpengumumanBerkas;
    private String judulpengumumanString, isipengumumanString, tgltayangpengumumanString, tglberakhirpengumumanString, tumbnailpengumumanumumString;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman_pribadi_activity);
        Bundle bundle = getIntent().getExtras();
        id_pengumuman = bundle.getString("id_pengumuman");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        recycleviewberkaspengumuman = findViewById(R.id.recycleviewberkaspengumuman);

        imgBack = findViewById(R.id.imgBack);
        tumbnailpengumumanumum = findViewById(R.id.tumbnailpengumumanumum);
        judulpengumumanInput = findViewById(R.id.inputJudulPengumuman);
        isipengumumanInput = findViewById(R.id.inputisipengumuman);
        tgltayangpengumumanInput = findViewById(R.id.inputtgltayangpengumuman);
        tglberakhirpengumumanInput = findViewById(R.id.inputtglberakhirpengumuman);

        judulpengumumanBerkas = findViewById(R.id.judulpengumumanBerkas);

        ModelBerkasPengumuman = new ArrayList<>();
        LinearLayoutManager x = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recycleviewberkaspengumuman.setHasFixedSize(true);
        recycleviewberkaspengumuman.setLayoutManager(x);
        recycleviewberkaspengumuman.setNestedScrollingEnabled(true);

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
                            getDataBerkas();


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
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PengumumanPribadiActivity.this);
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
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PengumumanPribadiActivity.this);
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
    private void getDataBerkas() {
        // Tag used to cancel the request
        pDialog.setMessage("Pengumuman Berkas ...");
        showDialog();
        AndroidNetworking.post(Config.url + "pengumuman/berkas")
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
                                Log.d("hehe", responses.optString("id_pengumuman"));
                                ModelBerkasPengumuman pp = new ModelBerkasPengumuman(
                                        responses.getString("id_pengumuman_berkas"),
                                        responses.getString("judul_file"),
                                        responses.getString("tgl_posting_file"),
                                        responses.getString("type_file"),
                                        responses.getString("file"),
                                        responses.getString("keterangan_file"));

                                ModelBerkasPengumuman.add(pp);
                            }
                            MenuAdapter menu = new MenuAdapter(getApplicationContext(), ModelBerkasPengumuman);
                            recycleviewberkaspengumuman.setAdapter(menu);
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
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PengumumanPribadiActivity.this);
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
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PengumumanPribadiActivity.this);
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
        private Context mCtx;
        private List<ModelBerkasPengumuman> ModelBerkasPengumuman;

        MenuAdapter(Context mCtx, List<ModelBerkasPengumuman> ModelBerkasPengumuman) {
            this.mCtx = mCtx;
            this.ModelBerkasPengumuman = ModelBerkasPengumuman;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_berkas_pengumuman_pribadi_rc, null);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int i) {
            final com.example.myapplication.model.ModelBerkasPengumuman menu = ModelBerkasPengumuman.get(i);
            holder.text_judul_berkas.setText(menu.getJudul_file());
            holder.text_tgl_posting_file.setText(menu.getTgl_posting_file());
            holder.text_keterangan_file.setText(menu.getKeterangan_file());
            holder.cvBerkas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (menu.getType_file()) {
                        case "1":
                            Intent a = new Intent(mCtx, BerkasPribadiTypeFilePdf.class);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            a.putExtra("file", menu.getFile());
                            mCtx.startActivity(a);
                            break;
                        case "2":
                            Intent b = new Intent(mCtx, BerkasPribadiTypeFileFoto.class);
                            b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            b.putExtra("file", menu.getFile());
                            mCtx.startActivity(b);
                            break;
                        case "3":
                            Intent c = new Intent(mCtx, BerkasPribadiTypeFileVideo.class);
                            c.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            c.putExtra("file", menu.getFile());
                            mCtx.startActivity(c);
                            break;
                        default:
                            Intent e = new Intent(mCtx, BerkasPribadiTypeFileDefault.class);
                            e.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mCtx.startActivity(e);
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return ModelBerkasPengumuman.size();
        }

        class ProductViewHolder extends RecyclerView.ViewHolder {
            TextView text_judul_berkas, text_tgl_posting_file, text_keterangan_file;
            CardView cvBerkas;

            ProductViewHolder(View itemView) {
                super(itemView);
                cvBerkas = itemView.findViewById(R.id.cvBerkas);
                text_judul_berkas = itemView.findViewById(R.id.inputJudulFile);
                text_tgl_posting_file = itemView.findViewById(R.id.inputtanggalposting);
                text_keterangan_file = itemView.findViewById(R.id.inputketeranganfile);
            }
        }
    }

    private void deklarasidata() {
        Glide.with(PengumumanPribadiActivity.this).load(tumbnailpengumumanumumString).into(tumbnailpengumumanumum);
        judulpengumumanInput.setText(judulpengumumanString);
        isipengumumanInput.setText(isipengumumanString);
        tgltayangpengumumanInput.setText(tgltayangpengumumanString);
        tglberakhirpengumumanInput.setText(tglberakhirpengumumanString);
        judulpengumumanBerkas.setText(judulpengumumanString);
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