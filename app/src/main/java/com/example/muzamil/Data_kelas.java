package com.example.muzamil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import okhttp3.*;
import com.example.muzamil.helper.Config;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


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

public class Data_kelas extends AppCompatActivity {

    TextView idkelas, nis, profesi, nama, jadwal, jam_mulai, jam_telat, jam_berakhir, tanggal, jam, mata_pelajaran;


    Button kunjungi;

    TextView namasalesbackup2, jam2, tanggal2;

    LinearLayout caridatabarang;

    LinearLayout blank_gambar;


    ListView listdataoutlet1;

    public static String idlist, nislist, namagurulist, jadwallist, jam_mulailist, jam_telatlist, jam_berakhirlist, tanggallist, mata_pelajaranlist;
    ArrayList<HashMap<String, String>> aruskas = new ArrayList<HashMap<String, String>>();


    private ProgressDialog pDialog;
    private Context context;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_kelas);

        // Inisialisasi context dan ProgressDialog
        context = Data_kelas.this;
        pDialog = new ProgressDialog(context);

        // Inisialisasi variabel yang digunakan untuk menyimpan data
        idlist = "";
        nislist = "";
        namagurulist = "";
        jadwallist = "";
        jam_mulailist = "";
        jam_telatlist = "";
        jam_berakhirlist = "";
        tanggallist = "";
        mata_pelajaranlist = "";

        // Inisialisasi tampilan layout
        blank_gambar = (LinearLayout) findViewById(R.id.blank_gambar);

        // Inisialisasi TextView untuk menampilkan data terkait kelas
        idkelas = (TextView) findViewById(R.id.idkelas);
        nis = (TextView) findViewById(R.id.nis);
        nama = (TextView) findViewById(R.id.nama);
        profesi = (TextView) findViewById(R.id.profesi);
        jadwal = (TextView) findViewById(R.id.jadwal);
        jam_mulai = (TextView) findViewById(R.id.jam_mulai);
        jam_telat = (TextView) findViewById(R.id.jam_telat);
        jam_berakhir = (TextView) findViewById(R.id.jam_berakhir);
        tanggal = (TextView) findViewById(R.id.tanggal);
        jam = (TextView) findViewById(R.id.jam);
        mata_pelajaran = (TextView) findViewById(R.id.mata_pelajaran);

        // Inisialisasi ListView untuk menampilkan data outlet
        listdataoutlet1 = (ListView) findViewById(R.id.listdataoutlet);

        // Inisialisasi LinearLayout untuk tombol pencarian data barang
        caridatabarang = (LinearLayout) findViewById(R.id.caridatabarang);

        // Menambahkan event listener untuk tombol pencarian data barang
        caridatabarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list(); // Memanggil method list() saat tombol diklik
            }
        });

        // Menambahkan TextWatcher pada TextView jadwal untuk memonitor perubahan teks
        jadwal.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    list(); // Memanggil method list() jika jadwal tidak kosong
            }
        });

        // Mengatur tanggal dan jam saat ini pada tampilan
        tanggal.setText(getCurrentDate());
        jam.setText(getCurrentClock());

        // Mengambil data yang diteruskan melalui Intent
        Intent i = getIntent();
        String kiriman = i.getStringExtra("nis");
        nis.setText(kiriman);
        String kiriman2 = i.getStringExtra("profesi");
        profesi.setText(kiriman2);
        String kiriman3 = i.getStringExtra("nama");
        nama.setText(kiriman3);

        // Memanggil method list() untuk menampilkan data saat pertama kali dibuka
        list();
    }



    // Method untuk mendapatkan tanggal saat ini dalam format "yyyy/MM/dd" (dengan locale Bahasa Indonesia)
    public String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("id", "ID")); // Format tanggal dalam bahasa Indonesia
        return dateFormat.format(c.getTime()); // Mengembalikan tanggal yang diformat sebagai string
    }

    // Method untuk mendapatkan waktu saat ini dalam format "hh:mm:ss"
    public String getCurrentClock(){
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss"); // Format waktu
        String strdate1 = sdf1.format(c1.getTime()); // Memformat waktu saat ini
        return strdate1; // Mengembalikan waktu yang diformat sebagai string
    }

    // Method untuk memuat data dan menampilkannya di ListView
    private void list() {
        // Bersihkan data sebelum mengisi kembali
        aruskas.clear();
        listdataoutlet1.setAdapter(null);

        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("nis", nis.getText().toString())
                .add("tanggal", tanggal.getText().toString())
                .add("jam", jam.getText().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "data_kelas.php")
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
                                map.put("jadwal", responses.optString("jadwal"));
                                map.put("jam_mulai", responses.optString("jam_mulai"));
                                map.put("jam_telat", responses.optString("jam_telat"));
                                map.put("jam_berakhir", responses.optString("jam_berakhir"));
                                map.put("pelajaran", responses.optString("pelajaran"));
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


//    // Helper Method untuk Execute Request
//    private void executePostRequest(String url, String idSiswa, Data_kelas.ResponseHandler responseHandler) {
//        try {
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("id_siswa", idSiswa);
//            executePostRequest(url, jsonBody, responseHandler);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void executePostRequest(String url, JSONObject jsonBody, Data_kelas.ResponseHandler responseHandler) {
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

    // Method untuk mengatur data ke dalam ListView menggunakan SimpleAdapter
    private void Adapter(){
        if (aruskas.isEmpty()) {
            // Jika aruskas kosong, tampilkan blank_gambar
            blank_gambar.setVisibility(View.VISIBLE);
            listdataoutlet1.setVisibility(View.GONE); // Menyembunyikan ListView jika data kosong
        } else {
            blank_gambar.setVisibility(View.GONE); // Sembunyikan blank_gambar jika ada data
            listdataoutlet1.setVisibility(View.VISIBLE); // Menampilkan ListView jika data ada

            // Membuat SimpleAdapter untuk menampilkan data ke dalam ListView
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskas, R.layout.list_kelas,
                    new String[] {"id","jadwal","jam_mulai","jam_telat","jam_berakhir","pelajaran"},
                    new int[] {R.id.idlistt,R.id.jadwallist,R.id.jammulailist,R.id.jamtelatlist, R.id.jamberakhirlist, R.id.mata_pelajaranlist});

            listdataoutlet1.setAdapter(simpleAdapter); // Menetapkan adapter ke ListView

            // Menangani item click pada ListView
            listdataoutlet1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Mengambil data dari item yang diklik
                    idlist = ((TextView) view.findViewById(R.id.idlistt)).getText().toString();
                    jadwallist = ((TextView) view.findViewById(R.id.jadwallist)).getText().toString();
                    jam_mulailist = ((TextView) view.findViewById(R.id.jammulailist)).getText().toString();
                    jam_telatlist = ((TextView) view.findViewById(R.id.jamtelatlist)).getText().toString();
                    jam_berakhirlist = ((TextView) view.findViewById(R.id.jamberakhirlist)).getText().toString();
                    mata_pelajaranlist = ((TextView) view.findViewById(R.id.mata_pelajaranlist)).getText().toString();

                    // Menetapkan nilai yang dipilih ke dalam TextView
                    idkelas.setText(idlist);
                    jadwal.setText(String.valueOf(jadwallist));
                    jam_mulai.setText(String.valueOf(jam_mulailist));
                    jam_telat.setText(jam_telatlist);
                    jam_berakhir.setText(jam_berakhirlist);
                    mata_pelajaran.setText(mata_pelajaranlist);

                    // Mengambil data lain untuk dikirimkan ke Absensi Activity
                    String b = nama.getText().toString();
                    String c = nis.getText().toString();
                    String d = profesi.getText().toString();
                    String e = jadwal.getText().toString();
                    String f = jam_mulai.getText().toString();
                    String g = jam_telat.getText().toString();
                    String h = jam_berakhir.getText().toString();
                    String j = mata_pelajaran.getText().toString();

                    // Membuat intent untuk berpindah ke Absensi Activity dan mengirim data
                    Intent i = new Intent(getApplicationContext(), Absensi.class);
                    i.putExtra("nama", b);
                    i.putExtra("nis", c);
                    i.putExtra("profesi", d);
                    i.putExtra("kelas", e);
                    i.putExtra("jam_mulai", f);
                    i.putExtra("jam_telat", g);
                    i.putExtra("jam_berakhir", h);
                    i.putExtra("mata_pelajaran", j);
                    startActivity(i); // Menjalankan activity Absensi
                }
            });
        }
    }




}