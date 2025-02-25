package com.example.muzamil;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.*;
import com.example.muzamil.helper.Config;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class Data_sales_grade extends AppCompatActivity {
    ListView listdataoutlet1;
    TextView id_siswa, kelas_siswa, nama_siswa;
    Spinner spinner_kelas;
    LinearLayout cari;

    ArrayList<HashMap<String, String>> aruskas = new ArrayList<HashMap<String, String>>();


    private ProgressDialog pDialog;
    private Context context;

    public static String idlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sales_grade);

        // Inisialisasi context dan ProgressDialog
        context = Data_sales_grade.this;
        pDialog = new ProgressDialog(context);

        // Inisialisasi ListView untuk menampilkan data outlet
        listdataoutlet1 = (ListView) findViewById(R.id.listtest);
        spinner_kelas = (Spinner) findViewById(R.id.spinner_kelas);
        cari = (LinearLayout) findViewById(R.id.cari);
        kelas_siswa = (TextView) findViewById(R.id.kelas_siswa);
        nama_siswa = (TextView) findViewById(R.id.nama_siswa);
        id_siswa = (TextView) findViewById(R.id.id_siswa);

        // Inisialisasi variabel yang digunakan untuk menyimpan data
        idlist = "";

        Intent i = getIntent();
        String kiriman = i.getStringExtra("id");
        id_siswa.setText(kiriman);
        String kiriman2 = i.getStringExtra("nama");
        nama_siswa.setText(kiriman2);

        cari.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                list();

            }
        });

        list();
    }


    private void list() {
        // Bersihkan data sebelum mengisi kembali
        aruskas.clear();
        listdataoutlet1.setAdapter(null);


        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("kelas", spinner_kelas.getSelectedItem().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "list_siswa_grade.php")
                .post(body)
                .build();


        // Melakukan permintaan asynchronous
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Tangani kesalahan jika diperlukan
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonResponse.optJSONArray("result");

                        // Proses data absen jika ada
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject responses = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
//                                map.put("id", responses.optString("id"));
//                                map.put("nis", responses.optString("nis"));
                                map.put("nama_guru", responses.optString("nama_guru"));
                                map.put("total_kelas", responses.optString("total_kelas"));
                                map.put("total_absen", responses.optString("total_absen"));
                                map.put("hasil", responses.optString("hasil"));
                                map.put("grade", responses.optString("grade"));
                                aruskas.add(map);
                            }
                        }

                        // Perbarui UI di thread utama
                        runOnUiThread(() -> Adapter());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }






    private void Adapter(){
        // Membuat SimpleAdapter untuk menampilkan data ke dalam ListView
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskas, R.layout.list_siswa_grade,
                new String[] {"nama_guru","total_kelas", "total_absen","hasil","grade"},
                new int[] {R.id.nama_guru_list,R.id.total_kelas_list,R.id.total_absen_list,R.id.hasil_list,R.id.grade_list});

        listdataoutlet1.setAdapter(simpleAdapter); // Menetapkan adapter ke ListView

        // Menangani item click pada ListView
        listdataoutlet1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
