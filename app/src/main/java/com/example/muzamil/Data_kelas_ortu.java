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
import android.widget.Toast;

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

public class Data_kelas_ortu extends AppCompatActivity {

    TextView idkelas,kelas, nis, profesi, nama, jadwal, jam_mulai, jam_telat, jam_berakhir, tanggal, jam, id_siswa, nama_siswa;


    Button kunjungi;

    TextView namasalesbackup2, jam2, tanggal2;

    LinearLayout caridatabarang, lewati;

    LinearLayout blank_gambar;


    ListView listdataoutlet1;

    public static String idlist, nislist, namagurulist, jadwallist, jam_mulailist, jam_telatlist, jam_berakhirlist, tanggallist;
    ArrayList<HashMap<String, String>> aruskas = new ArrayList<HashMap<String, String>>();


    private ProgressDialog pDialog;
    private Context context;



//<!--Sama seperti Data_kelas tetapi ini untuk orang tua memilih kelas untu absensikan anak nya-->


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_kelas_ortu);

        context = Data_kelas_ortu.this;
        pDialog = new ProgressDialog(context);

        idlist = "";
        nislist = "";
        namagurulist = "";
        jadwallist = "";
        jam_mulailist = "";
        jam_telatlist = "";
        jam_berakhirlist = "";
        tanggallist = "";


        blank_gambar = (LinearLayout) findViewById(R.id.blank_gambar);

        idkelas = (TextView) findViewById(R.id.idkelas);
        kelas = (TextView) findViewById(R.id.kelas);
        nis = (TextView) findViewById(R.id.nis);
        nama = (TextView) findViewById(R.id.nama);
        profesi = (TextView) findViewById(R.id.profesi);
        jadwal = (TextView) findViewById(R.id.jadwal);
        jam_mulai = (TextView) findViewById(R.id.jam_mulai);
        jam_telat = (TextView) findViewById(R.id.jam_telat);
        jam_berakhir = (TextView) findViewById(R.id.jam_berakhir);
        tanggal = (TextView) findViewById(R.id.tanggal);
        jam = (TextView) findViewById(R.id.jam);
        id_siswa = (TextView) findViewById(R.id.id_siswa);
        nama_siswa = (TextView) findViewById(R.id.nama_siswa);



        listdataoutlet1 = (ListView) findViewById(R.id.listdataoutlet);

        caridatabarang = (LinearLayout) findViewById(R.id.caridatabarang);

        lewati = (LinearLayout) findViewById(R.id.lewati);


        caridatabarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list();
            }
        });

        lewati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String a = idkelas.getText().toString();
                String b = nama.getText().toString();
                String c = nis.getText().toString();
                String d = profesi.getText().toString();
                String e = kelas.getText().toString();

                String f = jam_mulai.getText().toString();
                String g = jam_telat.getText().toString();
                String h = jam_berakhir.getText().toString();

                String j = id_siswa.getText().toString();
                String k = nama_siswa.getText().toString();

                Intent i = new Intent(getApplicationContext(), Data_absen_ortu.class);
                i.putExtra("nama",""+b+"");
                i.putExtra("nis",""+c+"");
                i.putExtra("profesi",""+d+"");
                i.putExtra("kelas",""+e+"");

//                i.putExtra("jam_mulai",""+f+"");
//                i.putExtra("jam_telat",""+g+"");
//                i.putExtra("jam_berakhir",""+h+"");

                i.putExtra("id_siswa",""+j+"");
                i.putExtra("nama_siswa",""+k+"");
                startActivity(i);
            }
        });


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

                    list();

            }
        });

        tanggal.setText(getCurrentDate());
        jam.setText(getCurrentClock());

        Intent i = getIntent();
        String kiriman = i.getStringExtra("nis");
        nis.setText(kiriman);
        String kiriman2 = i.getStringExtra("profesi");
        profesi.setText(kiriman2);
        String kiriman3 = i.getStringExtra("nama");
        nama.setText(kiriman3);
        String kiriman4 = i.getStringExtra("id_siswa");
        id_siswa.setText(kiriman4);
        String kiriman5 = i.getStringExtra("nama_siswa");
        nama_siswa.setText(kiriman5);
//        String kiriman6 = i.getStringExtra("kelas");
//        kelas.setText(kiriman6);


        //prosesdasboard1();

        list_kelas();
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

    public String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("id", "ID")); // Format tanggal dalam bahasa Indonesia
        return dateFormat.format(c.getTime());
    }


    public String getCurrentClock(){
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss");
        String strdate1 = sdf1.format(c1.getTime());
        return strdate1;


    }

    private void list() {
        // Bersihkan data sebelum mengisi kembali
        aruskas.clear();
        listdataoutlet1.setAdapter(null);

        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("kelas", kelas.getText().toString())
                .add("tanggal", tanggal.getText().toString())
                .add("jam", jam.getText().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "data_kelas_ortu.php")
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
//    private void executePostRequest(String url, String idSiswa, Data_kelas_ortu.ResponseHandler responseHandler) {
//        try {
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("id_siswa", idSiswa);
//            executePostRequest(url, jsonBody, responseHandler);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void executePostRequest(String url, JSONObject jsonBody, Data_kelas_ortu.ResponseHandler responseHandler) {
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

    private void Adapter(){

        if (aruskas.isEmpty()) {
            // Jika aruskas kosong, tampilkan blank_gambar
            // Misalnya:
            blank_gambar.setVisibility(View.VISIBLE);
            listdataoutlet1.setVisibility(View.GONE);
        } else {
            blank_gambar.setVisibility(View.GONE);
            listdataoutlet1.setVisibility(View.VISIBLE);

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskas, R.layout.list_kelas,
                    new String[] {"id","jadwal","jam_mulai","jam_telat","jam_berakhir"},
                    new int[] {R.id.idlistt,R.id.jadwallist,R.id.jammulailist,R.id.jamtelatlist, R.id.jamberakhirlist});

            listdataoutlet1.setAdapter(simpleAdapter);

            listdataoutlet1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    idlist = ((TextView) view.findViewById(R.id.idlistt)).getText().toString();
                    jadwallist = ((TextView) view.findViewById(R.id.jadwallist)).getText().toString();
                    jam_mulailist = ((TextView) view.findViewById(R.id.jammulailist)).getText().toString();
                    jam_telatlist = ((TextView) view.findViewById(R.id.jamtelatlist)).getText().toString();
                    jam_berakhirlist = ((TextView) view.findViewById(R.id.jamberakhirlist)).getText().toString();



                    // Set the parsed values to the appropriate TextViews
                    idkelas.setText(idlist);
//                    nama.setText(namagurulist);
//                    nis.setText(String.valueOf(nislist)); // Set as plain numeric value
                    jadwal.setText(String.valueOf(jadwallist)); // Set as plain numeric value
                    jam_mulai.setText(String.valueOf(jam_mulailist)); // Set as plain numeric value
                    jam_telat.setText(jam_telatlist);
                    jam_berakhir.setText(jam_berakhirlist);


                    //String a = idkelas.getText().toString();
                    String b = nama.getText().toString();
                    String c = nis.getText().toString();
                    String d = profesi.getText().toString();
                    String e = jadwal.getText().toString();

                    String f = jam_mulai.getText().toString();
                    String g = jam_telat.getText().toString();
                    String h = jam_berakhir.getText().toString();

                    String j = id_siswa.getText().toString();
                    String k = nama_siswa.getText().toString();

                    Intent i = new Intent(getApplicationContext(), Data_absen_ortu.class);
                    i.putExtra("nama",""+b+"");
                    i.putExtra("nis",""+c+"");
                    i.putExtra("profesi",""+d+"");
                    i.putExtra("kelas",""+e+"");

//                i.putExtra("jam_mulai",""+f+"");
//                i.putExtra("jam_telat",""+g+"");
//                i.putExtra("jam_berakhir",""+h+"");

                    i.putExtra("id_siswa",""+j+"");
                    i.putExtra("nama_siswa",""+k+"");
                    startActivity(i);



//                        Intent resultIntent = new Intent();
//                        resultIntent.putExtra("nama",""+b+"");
//                        resultIntent.putExtra("nis",""+c+"");
//                        resultIntent.putExtra("profesi",""+d+"");
//                        resultIntent.putExtra("jadwal",""+e+"");
//
//                        //resultIntent.putExtra("selectedBarang", selectedBarang);
//                        setResult(RESULT_OK, resultIntent);
//                        finish();



                }
            });
        }
    }



}