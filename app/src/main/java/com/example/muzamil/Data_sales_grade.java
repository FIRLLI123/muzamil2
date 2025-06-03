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
import android.widget.Button;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
    TextView id_siswa, kelas_siswa, nama_siswa, tanggal_dari, tanggal_sampai, mata_pelajaran;
    Spinner spinner_kelas;
    LinearLayout cari;
    Button btn_tampilkan;
    SwipeRefreshLayout swipeRefreshLayout;
    Calendar calendar;
    SimpleDateFormat dateFormat;

    ArrayList<HashMap<String, String>> aruskas = new ArrayList<HashMap<String, String>>();


    private ProgressDialog pDialog;
    private Context context;

    public static String idlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sales_grade);

        // Initialize date format
        dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        calendar = Calendar.getInstance();

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
        tanggal_dari = (TextView) findViewById(R.id.tanggal_dari);
        tanggal_sampai = (TextView) findViewById(R.id.tanggal_sampai);
        btn_tampilkan = (Button) findViewById(R.id.btn_tampilkan);
        mata_pelajaran = (TextView) findViewById(R.id.mata_pelajaran);

        // Set default dates
        tanggal_dari.setText(dateFormat.format(calendar.getTime()));
        tanggal_sampai.setText(dateFormat.format(calendar.getTime()));

        // Set click listeners for date fields
        tanggal_dari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tanggal_dari);
            }
        });

        tanggal_sampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tanggal_sampai);
            }
        });

        // Set click listener for tampilkan button
        btn_tampilkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validasi tanggal
                if (tanggal_dari.getText().toString().isEmpty() || tanggal_sampai.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Silakan pilih tanggal dari dan tanggal sampai", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tampilkan loading dialog
                pDialog.setMessage("Memuat data...");
                pDialog.setCancelable(false);
                pDialog.show();

                // Panggil fungsi list
                list();
            }
        });

        // Inisialisasi SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Inisialisasi variabel yang digunakan untuk menyimpan data
        idlist = "";

        Intent i = getIntent();
        String kiriman = i.getStringExtra("id");
        id_siswa.setText(kiriman);
        String kiriman2 = i.getStringExtra("nama");
        nama_siswa.setText(kiriman2);
        String kiriman3 = i.getStringExtra("kelas");
        kelas_siswa.setText(kiriman3);
        String kiriman4 = i.getStringExtra("pelajaran");
        mata_pelajaran.setText(kiriman4);

        cari.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                list();
            }
        });

        list();
    }

    private void showDatePickerDialog(final TextView textView) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                textView.setText(dateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void list() {
        // Bersihkan data sebelum mengisi kembali
        aruskas.clear();
        listdataoutlet1.setAdapter(null);

        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("kelas", kelas_siswa.getText().toString())
                .add("id_siswa", id_siswa.getText().toString())
                .add("tanggal_dari", tanggal_dari.getText().toString())
                .add("tanggal_sampai", tanggal_sampai.getText().toString())
                .add("pelajaran", mata_pelajaran.getText().toString())
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
                runOnUiThread(() -> {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Toast.makeText(context, "Gagal memuat data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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
                                map.put("nama_guru", responses.optString("nama_guru"));
                                map.put("nama_pelajaran", responses.optString("nama_pelajaran"));
                                map.put("nama_siswa", responses.optString("nama_siswa"));
                                map.put("total_kelas", responses.optString("total_kelas"));
                                map.put("jumlah_masuk", responses.optString("jumlah_masuk"));
                                map.put("hasil_masuk", responses.optString("hasil_masuk"));
                                map.put("jumlah_telat", responses.optString("jumlah_telat"));
                                map.put("hasil_telat", responses.optString("hasil_telat"));
                                map.put("jumlah_izin", responses.optString("jumlah_izin"));
                                map.put("hasil_izin", responses.optString("hasil_izin"));
                                map.put("jumlah_alpa", responses.optString("jumlah_alpa"));
                                map.put("hasil_alpa", responses.optString("hasil_alpa"));
                                map.put("total_bobot_kehadiran", responses.optString("total_bobot_kehadiran"));
                                map.put("persentase_kehadiran", responses.optString("persentase_kehadiran"));
                                map.put("grade", responses.optString("grade"));
                                aruskas.add(map);
                            }
                        }

                        // Perbarui UI di thread utama
                        runOnUiThread(() -> {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Adapter();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(context, "Gagal memproses data", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        });
    }

    private void Adapter(){
        // Membuat SimpleAdapter untuk menampilkan data ke dalam ListView
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskas, R.layout.list_siswa_grade,
                new String[] {
                    "nama_guru",
                    "nama_pelajaran",
                    "nama_siswa",
                    "total_kelas",
                    "jumlah_masuk",
                    "hasil_masuk",
                    "jumlah_telat",
                    "hasil_telat",
                    "jumlah_izin",
                    "hasil_izin",
                    "jumlah_alpa",
                    "hasil_alpa",
                    "total_bobot_kehadiran",
                    "persentase_kehadiran",
                    "grade"
                },
                new int[] {
                    R.id.nama_guru_list,
                    R.id.nama_pelajaran_list,
                    R.id.nama_siswa_list,
                    R.id.total_kelas_list,
                    R.id.jumlah_masuk_list,
                    R.id.hasil_masuk_list,
                    R.id.jumlah_telat_list,
                    R.id.hasil_telat_list,
                    R.id.jumlah_izin_list,
                    R.id.hasil_izin_list,
                    R.id.jumlah_alpa_list,
                    R.id.hasil_alpa_list,
                    R.id.total_bobot_kehadiran_list,
                    R.id.persentase_kehadiran_list,
                    R.id.grade_list
                });

        listdataoutlet1.setAdapter(simpleAdapter); // Menetapkan adapter ke ListView

        // Menangani item click pada ListView
        listdataoutlet1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
}
