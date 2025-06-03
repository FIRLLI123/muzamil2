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

public class Data_sales extends AppCompatActivity {
    ListView listdataoutlet1;
    TextView id_siswa, kelas_siswa, nama_siswa, mata_pelajaran;
    Spinner spinner_kelas;
    LinearLayout cari;

    ArrayList<HashMap<String, String>> aruskas = new ArrayList<HashMap<String, String>>();


    private ProgressDialog pDialog;
    private Context context;

    public static String idlist, namalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sales);

        // Inisialisasi context dan ProgressDialog
        context = Data_sales.this;
        pDialog = new ProgressDialog(context);

        // Inisialisasi ListView untuk menampilkan data outlet
        listdataoutlet1 = (ListView) findViewById(R.id.listtest);
        spinner_kelas = (Spinner) findViewById(R.id.spinner_kelas);
        cari = (LinearLayout) findViewById(R.id.cari);
        kelas_siswa = (TextView) findViewById(R.id.kelas_siswa);
        nama_siswa = (TextView) findViewById(R.id.nama_siswa);
        id_siswa = (TextView) findViewById(R.id.id_siswa);
        mata_pelajaran = (TextView) findViewById(R.id.mata_pelajaran);

        // Inisialisasi variabel yang digunakan untuk menyimpan data
        idlist = "";
        namalist = "";


        Intent i = getIntent();
        String kiriman = i.getStringExtra("pelajaran");
        mata_pelajaran.setText(kiriman);



        cari.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                list();

            }
        });


        spinner_kelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Ambil nilai yang dipilih
                String selectedKelas = parent.getItemAtPosition(position).toString();

                // Set nilai ke TextView
                kelas_siswa.setText(selectedKelas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Tidak melakukan apa-apa jika tidak ada yang dipilih
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
                .url(Config.host + "list_siswa.php")
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
                                map.put("id_siswa", responses.optString("id_siswa"));
//                                map.put("nis", responses.optString("nis"));
                                map.put("nama", responses.optString("nama"));
                                map.put("nama_wali_kelas", responses.optString("nama_wali_kelas"));
                                map.put("nama_wali_ortu", responses.optString("nama_wali_ortu"));
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
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskas, R.layout.list_siswa,
                    new String[] {"id_siswa","nama", "nama_wali_kelas","nama_wali_ortu"},
                    new int[] {R.id.id_siswa_list,R.id.nama_list,R.id.nama_walikelas_list,R.id.nama_waliortu_list});

            listdataoutlet1.setAdapter(simpleAdapter); // Menetapkan adapter ke ListView

            // Menangani item click pada ListView
            listdataoutlet1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Mengambil data dari item yang diklik
                    idlist = ((TextView) view.findViewById(R.id.id_siswa_list)).getText().toString();
                    namalist = ((TextView) view.findViewById(R.id.nama_list)).getText().toString();


                    // Menetapkan nilai yang dipilih ke dalam TextView
                    id_siswa.setText(idlist);
                    nama_siswa.setText(namalist);


                    // Mengambil data lain untuk dikirimkan ke Absensi Activity
                    String b = id_siswa.getText().toString();
                    String c = nama_siswa.getText().toString();
                    String d = kelas_siswa.getText().toString();
                    String e = mata_pelajaran.getText().toString();

                    // Membuat intent untuk berpindah ke Absensi Activity dan mengirim data
                    Intent i = new Intent(getApplicationContext(), Data_sales_grade.class);
                    i.putExtra("id", b);
                    i.putExtra("nama", c);
                    i.putExtra("kelas", d);
                    i.putExtra("pelajaran", e);
                    startActivity(i); // Menjalankan activity Absensi
                }
            });
        }
    }
