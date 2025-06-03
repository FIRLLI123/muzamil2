package com.example.muzamil;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.NotificationCompat;

import android.os.Bundle;
import android.content.Intent;

import androidx.core.widget.TextViewCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import android.app.ProgressDialog;
import android.content.Context;

import androidx.appcompat.widget.AppCompatButton;

import okhttp3.*;
import com.example.muzamil.helper.Config;
//import com.example.muzamil.helper.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


//method kehadiran
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;

public class MenuAbsen extends AppCompatActivity {
TextView nis, nama, profesi, id_siswa, nama_siswa;

TextView masuk,telat,izinn,tidakmasuk, mata_pelajaran, selengkapnya, tanggal, kelas;

LinearLayout kehadiran, izin, data_absen, logout;

    Handler mHandler;
    private ProgressDialog pDialog;
    private Context context;

    private AlertDialog alert;

    ListView listtest1;
    ArrayList<HashMap<String, String>> aruskas = new ArrayList<HashMap<String, String>>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_absen);

        //pembuatan variable tiap widget

        this.mHandler = new Handler();
        m_Runnable.run();

        context = MenuAbsen.this;
        pDialog = new ProgressDialog(context);

        listtest1 = (ListView) findViewById(R.id.listtest);

        nis = (TextView) findViewById(R.id.nis);
        nama = (TextView) findViewById(R.id.nama);
        profesi = (TextView) findViewById(R.id.profesi);
        id_siswa = (TextView) findViewById(R.id.id_siswa);
        nama_siswa = (TextView) findViewById(R.id.nama_siswa);
        mata_pelajaran = (TextView) findViewById(R.id.mata_pelajaran);
        kelas = (TextView) findViewById(R.id.kelas);


        masuk = (TextView) findViewById(R.id.masuk);
        telat = (TextView) findViewById(R.id.telat);
        izinn = (TextView) findViewById(R.id.izinn);
        tidakmasuk = (TextView) findViewById(R.id.tidakmasuk);

        tanggal = (TextView) findViewById(R.id.tanggal);

        selengkapnya = (TextView) findViewById(R.id.selengkapnya);



        kehadiran = (LinearLayout) findViewById(R.id.kehadiran);
        izin = (LinearLayout) findViewById(R.id.izin);
        data_absen = (LinearLayout) findViewById(R.id.data_absen);
        logout = (LinearLayout) findViewById(R.id.logout);


        Intent i = getIntent();
        String kiriman = i.getStringExtra("nis");
        nis.setText(kiriman);

        tanggal.setText(getCurrentDate());

//----------------------------------------------------------




        kehadiran.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Cek apakah profesi adalah GURU
                if (profesi.getText().toString().equals("GURU")) {
                    String a = nis.getText().toString();
                    String b = nama.getText().toString();
                    String c = profesi.getText().toString();

                    // Berpindah ke Data_kelas
                    Intent i = new Intent(getApplicationContext(), Data_kelas.class);
                    i.putExtra("nis", "" + a + "");
                    i.putExtra("nama", "" + b + "");
                    i.putExtra("profesi", "" + c + "");

                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else if (profesi.getText().toString().equals("IT")) {
                    String a = nis.getText().toString();
                    String b = nama.getText().toString();
                    String c = profesi.getText().toString();

                    // Berpindah ke Data_kelas
                    Intent i = new Intent(getApplicationContext(), Data_kelas.class);
                    i.putExtra("nis", "" + a + "");
                    i.putExtra("nama", "" + b + "");
                    i.putExtra("profesi", "" + c + "");
                } else if (profesi.getText().toString().equals("null")) {
                    // Tampilkan pesan jika koneksi lemah
                    Toast.makeText(getApplicationContext(), "KONEKSI LEMAH TUNGGU SEBENTAR", Toast.LENGTH_LONG).show();
                } else {
                    // Tampilkan pesan jika akses tidak valid
                    Toast.makeText(getApplicationContext(), "TIDAK MEMILIKI AKSES", Toast.LENGTH_LONG).show();
                }
            }
        });

        izin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Cek apakah profesi adalah ortu
                if (profesi.getText().toString().equals("ORTU")) {
                    String a = nis.getText().toString();
                    String b = nama.getText().toString();
                    String c = profesi.getText().toString();
                    String d = nama_siswa.getText().toString();
                    String e = id_siswa.getText().toString();
//                    String f = kelas.getText().toString();

                    // Berpindah ke Data_kelas_ortu
                    Intent i = new Intent(getApplicationContext(), Data_kelas_ortu.class);
                    i.putExtra("nis", "" + a + "");
                    i.putExtra("nama", "" + b + "");
                    i.putExtra("profesi", "" + c + "");
                    i.putExtra("nama_siswa", "" + d + "");
                    i.putExtra("id_siswa", "" + e + "");
//                    i.putExtra("kelas", "" + f + "");

                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else if (profesi.getText().toString().equals("IT")) {
                    String a = nis.getText().toString();
                    String b = nama.getText().toString();
                    String c = profesi.getText().toString();
                    String d = nama_siswa.getText().toString();
                    String e = id_siswa.getText().toString();

                    // Berpindah ke Data_kelas_ortu
                    Intent i = new Intent(getApplicationContext(), Data_kelas_ortu.class);
                    i.putExtra("nis", "" + a + "");
                    i.putExtra("nama", "" + b + "");
                    i.putExtra("profesi", "" + c + "");
                    i.putExtra("nama_siswa", "" + d + "");
                    i.putExtra("id_siswa", "" + e + "");
                } else if (profesi.getText().toString().equals("null")) {
                    // Tampilkan pesan jika koneksi lemah
                    Toast.makeText(getApplicationContext(), "KONEKSI LEMAH TUNGGU SEBENTAR", Toast.LENGTH_LONG).show();
                } else {
                    // Tampilkan pesan jika akses tidak valid
                    Toast.makeText(getApplicationContext(), "TIDAK MEMILIKI AKSES", Toast.LENGTH_LONG).show();
                }
            }
        });

        data_absen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String profesiValue = profesi.getText().toString();
                Log.d("Profesi Value", profesiValue); // Log untuk mengecek nilai profesi

                if (profesiValue.equals("ORTU")) {
                    String a = id_siswa.getText().toString();
                    String b = nama_siswa.getText().toString();

                    // Berpindah ke Data_absen
                    Intent i = new Intent(getApplicationContext(), Data_absen.class);
                    i.putExtra("id_siswa", a);
                    i.putExtra("nama_siswa", b);

                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                } else if (profesiValue.equals("BK")) {
                    Log.d("Profesi Check", "BK detected"); // Log untuk memastikan blok ini dijalankan

                    String a = nis.getText().toString();
                    String b = nama.getText().toString();

                    // Berpindah ke Data_absen_kepsek
                    Intent i = new Intent(getApplicationContext(), Data_absen_kepsek.class);
                    i.putExtra("nis", a);
                    i.putExtra("nama", b);
                    i.putExtra("profesi", profesiValue);

                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                } else if (profesiValue.equals("null")) {
                    // Tampilkan pesan jika koneksi lemah
                    Toast.makeText(getApplicationContext(), "KONEKSI LEMAH TUNGGU SEBENTAR", Toast.LENGTH_LONG).show();
                } else {
                    // Tampilkan pesan jika akses tidak valid
                    Toast.makeText(getApplicationContext(), "TIDAK MEMILIKI AKSES", Toast.LENGTH_LONG).show();
                }
            }
        });

        selengkapnya.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (profesi.getText().toString().equals("BK")) {
                    String a = id_siswa.getText().toString();
                    String b = nama_siswa.getText().toString();
                    String c = mata_pelajaran.getText().toString();

                    // Berpindah ke Data_absen
                    Intent i = new Intent(getApplicationContext(), Data_sales.class);
                    i.putExtra("id_siswa", "" + a + "");
                    i.putExtra("nama_siswa", "" + b + "");
                    i.putExtra("pelajaran", "" + c + "");
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else if (profesi.getText().toString().equals("ORTU")) {
                    String a = id_siswa.getText().toString();
                    String b = nama_siswa.getText().toString();
                    String c = mata_pelajaran.getText().toString();
                    String d = kelas.getText().toString();

                    // Berpindah ke Data_absen
                    Intent i = new Intent(getApplicationContext(), Data_sales_grade.class);
                    i.putExtra("id", "" + a + "");
                    i.putExtra("nama", "" + b + "");
                    i.putExtra("pelajaran", "" + c + "");
                    i.putExtra("kelas", "" + d + "");
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }else if (profesi.getText().toString().equals("null")) {
                    // Tampilkan pesan jika koneksi lemah
                    Toast.makeText(getApplicationContext(), "KONEKSI LEMAH TUNGGU SEBENTAR", Toast.LENGTH_LONG).show();
                } else {
                    // Tampilkan pesan jika akses tidak valid
                    Toast.makeText(getApplicationContext(), "TIDAK MEMILIKI AKSES", Toast.LENGTH_LONG).show();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menampilkan dialog konfirmasi logout
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuAbsen.this);
                builder.setMessage("Apakah Anda ingin logout?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetSharedPreferences(); // Reset data login

                                // Kembali ke MainActivity
                                Intent intent = new Intent(MenuAbsen.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Menutup dialog
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

// Fungsi tambahan
        user(); // Memuat data pengguna
        absenhadir(); // Memuat data kehadiran
        list(); // Memuat data lainnya
absentelat();
        list_kelas();



    }


    //simpan data user
    private void resetSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Hapus semua data yang tersimpan
        editor.apply();
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {


            // Inisialisasi TextView
            nis = (TextView) findViewById(R.id.nis);
            nama = (TextView) findViewById(R.id.nama);
            profesi = (TextView) findViewById(R.id.profesi);
            id_siswa = (TextView) findViewById(R.id.id_siswa);
            nama_siswa = (TextView) findViewById(R.id.nama_siswa);

            // Panggil fungsi-fungsi lainnya
            user();
            absenhadir();
//            absentelat();
            absenizin();
            absentidakmasuk();

            // Mengecek apakah alert belum ditampilkan
            if (alert == null) {
                // Membuat dan menampilkan AlertDialog untuk loading
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuAbsen.this);
                builder.setMessage("Loading...")
                        .setCancelable(false); // Agar tidak bisa ditutup oleh user
                alert = builder.create();
                alert.show(); // Menampilkan alert
            }

            // Mengecek profesi setelah delay
            if (profesi.equals("null")) {
                // Tampilkan pesan koneksi lemah setelah 10 detik
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (profesi == null) {
                            alert.setMessage("Koneksi mu lemah, aplikasi akan keluar");

                            // Menunggu 2 detik lalu keluar
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish(); // Menutup aplikasi
                                }
                            }, 2000); // Delay 2 detik untuk menampilkan pesan "koneksi lemah"
                        }
                    }
                }, 10000); // 10 detik delay
            } else {
                // Jika profesi tidak null, tutup alert dan tampilkan Toast
                if (alert != null && alert.isShowing()) {
                    alert.dismiss();
                    Toast.makeText(MenuAbsen.this, "Aplikasi siap digunakan", Toast.LENGTH_SHORT).show();
                }
            }

            MenuAbsen.this.mHandler.postDelayed(m_Runnable, 2000);
        }

    };






    private void absenhadir() {
        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("id_siswa", id_siswa.getText().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "count_absenhadir.php")
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
                        String id = jsonResponse.optString("id", "0");

                        // Perbarui UI di thread utama
                        runOnUiThread(() -> {
                            masuk.setText(id.equals("null") ? "0" : id);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }




    private void absentelat() {
        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("id_siswa", id_siswa.getText().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "count_absentelat.php")
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
                        String id = jsonResponse.optString("id", "0");

                        // Perbarui UI di thread utama
                        runOnUiThread(() -> {
                            telat.setText(id.equals("null") ? "0" : id);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    // Method Izin
    private void absenizin() {
        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("id_siswa", id_siswa.getText().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "count_absenizin.php")
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
                        String id = jsonResponse.optString("id", "0");

                        // Perbarui UI di thread utama
                        runOnUiThread(() -> {
                            telat.setText(id.equals("null") ? "0" : id);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Method Tidak Hadir
    private void absentidakmasuk() {

        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("id_siswa", id_siswa.getText().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "count_absentidakmasuk.php")
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
                        String id = jsonResponse.optString("id", "0");

                        // Perbarui UI di thread utama
                        runOnUiThread(() -> {
                            telat.setText(id.equals("null") ? "0" : id);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void user() {
        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("nis", nis.getText().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "user_depan.php")
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

                        // Mengambil data dari respons
                        String namaValue = jsonResponse.optString("nama", "");
                        String profesiValue = jsonResponse.optString("profesi", "");
                        String idSiswaValue = jsonResponse.optString("id_siswa", "");
                        String namaSiswaValue = jsonResponse.optString("nama_siswa", "");
                        String mataPelajaranValue = jsonResponse.optString("mata_pelajaran", "");

                        // Perbarui UI di thread utama
                        runOnUiThread(() -> {
                            nama.setText(namaValue);
                            profesi.setText(profesiValue);
                            id_siswa.setText(idSiswaValue);
                            nama_siswa.setText(namaSiswaValue);
                            mata_pelajaran.setText(mataPelajaranValue);
                            list(); // Memanggil metode list()
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    public String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("id", "ID")); // Format tanggal dalam bahasa Indonesia
        return dateFormat.format(c.getTime());
    }

    // Method untuk menampilkan data list absen orang tua
    private void list() {
        // Bersihkan data sebelum mengisi kembali
        aruskas.clear();
        listtest1.setAdapter(null);

        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("tanggal", tanggal.getText().toString())
                .add("id_siswa", id_siswa.getText().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "list_absen_ortu.php")
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
                                map.put("id", responses.optString("id"));
                                map.put("nis", responses.optString("nis"));
                                map.put("nama", responses.optString("nama"));
                                map.put("profesi", responses.optString("profesi"));
                                map.put("jam", responses.optString("jam"));
                                map.put("tanggal", responses.optString("tanggal"));
                                map.put("kelas", responses.optString("kelas"));
                                map.put("id_siswa", responses.optString("id_siswa"));
                                map.put("nama_siswa", responses.optString("nama_siswa"));
                                map.put("keterangan", responses.optString("keterangan"));
                                aruskas.add(map);

                                // Kondisi untuk notifikasi jika profesi adalah "ortu" dan keterangan adalah "telat"
                                String profesi = responses.optString("profesi");
                                String keterangan = responses.optString("keterangan");
                                String namaSiswa = responses.optString("nama_siswa");

                                if ("ORTU".equalsIgnoreCase(profesi) && "telat".equalsIgnoreCase(keterangan)) {
                                    showNotification("Perhatian", namaSiswa + " terlambat hari ini.");
                                }
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



    private void list_kelas() {
        // Ambil ID siswa dari TextView
        String studentId = id_siswa.getText().toString();

        // Tampilkan dialog loading
        pDialog.setMessage("Memuat data...");
        pDialog.show();

        // Buat request body
        RequestBody body = new FormBody.Builder()
                .add("id_siswa", studentId)
                .build();

        // Buat request
        Request request = new Request.Builder()
                .url(Config.host + "get_kelas_siswa.php")
                .post(body)
                .build();

        // Eksekusi request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    pDialog.dismiss();
                    Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        if (jsonResponse.getString("status").equals("success")) {
                            JSONArray jsonArray = jsonResponse.getJSONArray("result");
                            if (jsonArray.length() > 0) {
                                JSONObject data = jsonArray.getJSONObject(0);

                                // Update UI dengan data kelas
                                runOnUiThread(() -> {
                                    kelas.setText(data.optString("kelas"));
                                    pDialog.dismiss();
                                });
                            }
                        } else {
                            // Tangani error
                            String errorMessage = jsonResponse.optString("message", "Terjadi kesalahan");
                            runOnUiThread(() -> {
                                pDialog.dismiss();
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            pDialog.dismiss();
                            Toast.makeText(context, "Gagal memproses data", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        });
    }


//    // Helper Method untuk Execute Request
//    private void executePostRequest(String url, String idSiswa, ResponseHandler responseHandler) {
//        try {
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("id_siswa", idSiswa);
//            executePostRequest(url, jsonBody, responseHandler);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void executePostRequest(String url, JSONObject jsonBody, ResponseHandler responseHandler) {
//        try {
//            TrustManager[] trustAllCerts = new TrustManager[]{
//                    new X509TrustManager() {
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
//                        }
//
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
//                        }
//
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return new java.security.cert.X509Certificate[]{};
//                        }
//                    }
//            };
//
//            SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
//                    .hostnameVerifier((hostname, session) -> true)
//                    .build();
//
//            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json; charset=utf-8"));
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response.body().string());
//                            responseHandler.onResponse(jsonResponse);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Interface untuk menangani response
//    private interface ResponseHandler {
//        void onResponse(JSONObject response) throws JSONException;
//    }


    private void Adapter() {
        MenuAbsen.CustomAdapter customAdapter = new MenuAbsen.CustomAdapter(this, aruskas, R.layout.list_absen_fix,
                new String[]{"id", "nis", "nama", "profesi", "jam", "tanggal", "kelas", "id_siswa", "nama_siswa", "keterangan"},
                new int[]{R.id.id_list, R.id.nis_list, R.id.nama_list, R.id.profesi_list, R.id.jam_list, R.id.tanggal_list, R.id.kelas_list, R.id.id_siswa_list, R.id.nama_siswa_list, R.id.keterangan_list});

        listtest1.setAdapter(customAdapter);

        listtest1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Item click handling
            }
        });
    }

    private class CustomAdapter extends SimpleAdapter {

        public CustomAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);



            return view;
        }
    }


    //notifikasi yang di atur oleh notificationManager
    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, MenuAbsen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = getString(R.string.notification_channel_id);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo1)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }



}