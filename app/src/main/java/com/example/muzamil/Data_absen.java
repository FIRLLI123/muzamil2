package com.example.muzamil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import okhttp3.*;
import com.example.muzamil.helper.Config;


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

public class Data_absen extends AppCompatActivity {
    ListView listtest1;
    TextView id_siswa, nama_siswa, tanggal;
    Button caritanggal;
    public static String LINK, idlist, tanggallist, jamlist, kecamatanlist, absenlist, keteranganlist, statuslist, pendinglist;
    ArrayList<HashMap<String, String>> aruskas = new ArrayList<HashMap<String, String>>();

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_absen);

        listtest1 = (ListView) findViewById(R.id.listtest);

        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        id_siswa = (TextView) findViewById(R.id.id_siswa);
        nama_siswa = (TextView) findViewById(R.id.nama_siswa);
        tanggal = (TextView) findViewById(R.id.tanggal);

        caritanggal = (Button) findViewById(R.id.caritanggal);

        // Mendapatkan data yang diteruskan melalui Intent
        Intent i = getIntent();
        String kiriman = i.getStringExtra("id_siswa");
        id_siswa.setText(kiriman);
        String kiriman2 = i.getStringExtra("nama_siswa");
        nama_siswa.setText(kiriman2);

        // Menampilkan tanggal saat ini pada TextView
        tanggal.setText(getCurrentDate());

        // Menangani klik pada button caritanggal
        caritanggal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Memanggil method list untuk menampilkan data
                list();
            }

        });

        // Menangani klik pada TextView tanggal untuk memilih tanggal
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog1(); // Menampilkan DatePickerDialog
            }
        });

        // Memanggil method list untuk menampilkan data saat pertama kali dibuka
        list();
    }

    private void showDateDialog1(){
        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Inisialisasi DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                tanggal.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    // Mengambil tanggal saat ini dan mengembalikannya dalam format yang diinginkan
    public String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("id", "ID")); // Format tanggal dalam bahasa Indonesia
        return dateFormat.format(c.getTime());
    }

    private void list() {
        // Bersihkan data sebelum mengisi kembali
        aruskas.clear();
        listtest1.setAdapter(null);

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("tanggal", tanggal.getText().toString());
            jsonBody.put("id_siswa", id_siswa.getText().toString());

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(Config.host + "list_absen_ortu.php")
                    .post(body)
                    .build();

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
                                }
                            }

                            runOnUiThread(() -> Adapter());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



//    // Helper Method untuk Execute Request
//    private void executePostRequest(String url, String idSiswa, Data_absen.ResponseHandler responseHandler) {
//        try {
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("id_siswa", idSiswa);
//            executePostRequest(url, jsonBody, responseHandler);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void executePostRequest(String url, JSONObject jsonBody, Data_absen.ResponseHandler responseHandler) {
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
        // Membuat CustomAdapter dan menghubungkannya dengan ListView
        Data_absen.CustomAdapter customAdapter = new Data_absen.CustomAdapter(this, aruskas, R.layout.list_absen_fix,
                new String[]{"id","nis", "nama", "profesi", "jam", "tanggal", "kelas", "id_siswa", "nama_siswa", "keterangan"},
                new int[]{R.id.id_list, R.id.nis_list, R.id.nama_list, R.id.profesi_list, R.id.jam_list, R.id.tanggal_list, R.id.kelas_list, R.id.id_siswa_list, R.id.nama_siswa_list, R.id.keterangan_list});

        listtest1.setAdapter(customAdapter);

        listtest1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Menangani klik pada item listview (bisa ditambahkan fungsionalitas lebih lanjut)
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

            // Mendapatkan data untuk absen dan jam dari aruskas
            String absenListAbsen = aruskas.get(position).get("absen");
            String jamListAbsen = aruskas.get(position).get("jam");

            // Bisa ditambahkan logika tambahan untuk memodifikasi tampilan berdasarkan data

            return view;
        }
    }
}



