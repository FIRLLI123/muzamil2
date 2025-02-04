package com.example.muzamil;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.*;
import com.example.muzamil.helper.Config;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


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

public class Data_absen_ortu extends AppCompatActivity implements View.OnClickListener {
    TextView nis, nama, profesi, jam, tanggal, kelas, id_siswa, nama_siswa, keterangan;

    TextView jam_masuk, jam_telat, jam_berakhir, jam_bayangan;

    Handler mHandler;
    private ProgressDialog pDialog;
    private Context context;

    private LinearLayout buttonScan;


    private IntentIntegrator intentIntegrator;



    private Button draggableButton;
    private LinearLayout mainLayout;
    private float dX, dY;
    private float originalX, originalY;
    private int screenWidth;

    TextView teks_absen;


    ListView listtest1;
    ArrayList<HashMap<String, String>> aruskas = new ArrayList<HashMap<String, String>>();


//    <!--Sama seperti Absensi, hanya ini memanggil data siswa untuk ortu absensikan-->



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_absen_ortu);

        this.mHandler = new Handler();
        m_Runnable.run();


        draggableButton = findViewById(R.id.draggableButton);
        mainLayout = findViewById(R.id.mainLayout);
        teks_absen = (TextView) findViewById(R.id.teks_absen);
        screenWidth = getResources().getDisplayMetrics().widthPixels;


        context = Data_absen_ortu.this;
        pDialog = new ProgressDialog(context);

        listtest1 = (ListView) findViewById(R.id.listtest);

        draggableButton = findViewById(R.id.draggableButton);

        nis = (TextView) findViewById(R.id.nis);
        nama = (TextView) findViewById(R.id.nama);
        profesi = (TextView) findViewById(R.id.profesi);
        jam = (TextView) findViewById(R.id.jam);
        tanggal = (TextView) findViewById(R.id.tanggal);
        kelas = (TextView) findViewById(R.id.kelas);
        id_siswa = (TextView) findViewById(R.id.id_siswa);
        nama_siswa = (TextView) findViewById(R.id.nama_siswa);
        keterangan = (TextView) findViewById(R.id.keterangan);
        jam_bayangan = (TextView) findViewById(R.id.jam_bayangan);


        jam_masuk = (TextView) findViewById(R.id.jam_masuk);
        jam_telat = (TextView) findViewById(R.id.jam_telat);
        jam_berakhir = (TextView) findViewById(R.id.jam_berakhir);


        buttonScan = (LinearLayout) findViewById(R.id.buttonScan);

        //buttonScan.setOnClickListener(this);

        tanggal.setText(getCurrentDate());
        jam.setText(getCurrentClock());
        jam_bayangan.setText(getCurrentClock());

        Intent i = getIntent();
        String kiriman = i.getStringExtra("nis");
        nis.setText(kiriman);
        String kiriman2 = i.getStringExtra("profesi");
        profesi.setText(kiriman2);
        String kiriman3 = i.getStringExtra("nama");
        nama.setText(kiriman3);
        String kiriman4 = i.getStringExtra("kelas");
        kelas.setText(kiriman4);
        String kiriman8 = i.getStringExtra("id_siswa");
        id_siswa.setText(kiriman8);
        String kiriman9 = i.getStringExtra("nama_siswa");
        nama_siswa.setText(kiriman9);





        draggableButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        originalX = view.getX();
                        originalY = view.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;

                        // Batasan agar tombol tetap di dalam layout
                        if (newX < 0) newX = 0;
                        if (newX + view.getWidth() > screenWidth)
                            newX = screenWidth - view.getWidth();
//                        if (newY < 0) newY = 0;
//
                        view.setX(newX);
//                        view.setY(newY);
                        break;

                    case MotionEvent.ACTION_UP:
                        if (view.getX() + view.getWidth() >= screenWidth / 2
                                && view.getY() <= 0) {
                            // Tombol mencapai ujung kiri atas
                            // Lakukan aksi pindah ke aktivitas lain di sini
                            if ( nama_siswa.getText().toString().equals("Data Siswa")){
                                //1
                                Toast.makeText(getApplicationContext(), "Silahkan pilih data siswa",
                                        Toast.LENGTH_LONG).show();
                                // Animasi mengembalikan tombol ke posisi awal
                                ObjectAnimator.ofFloat(draggableButton, "x", originalX)
                                        .setDuration(300)
                                        .start();
                                ObjectAnimator.ofFloat(draggableButton, "y", originalY)
                                        .setDuration(300)
                                        .start();

                                //Toast.makeText(getApplicationContext(), "Silahkan pilih terlebih dahulu", Toast.LENGTH_LONG).show();
                            }  else {


                                absengak();



                            }
                        } else {
                            // Animasi mengembalikan tombol ke posisi awal
                            ObjectAnimator.ofFloat(view, "x", originalX)
                                    .setDuration(300)
                                    .start();
                            ObjectAnimator.ofFloat(view, "y", originalY)
                                    .setDuration(300)
                                    .start();
                        }
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });

        list();

    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {


            nis = (TextView) findViewById(R.id.nis);
            nama = (TextView) findViewById(R.id.nama);
            profesi = (TextView) findViewById(R.id.profesi);
            jam = (TextView) findViewById(R.id.jam);
            tanggal = (TextView) findViewById(R.id.tanggal);
            kelas = (TextView) findViewById(R.id.kelas);
            id_siswa = (TextView) findViewById(R.id.id_siswa);
            nama_siswa = (TextView) findViewById(R.id.nama_siswa);
            keterangan = (TextView) findViewById(R.id.keterangan);
            jam_bayangan = (TextView) findViewById(R.id.jam_bayangan);


            jam_masuk = (TextView) findViewById(R.id.jam_masuk);
            jam_telat = (TextView) findViewById(R.id.jam_telat);
            jam_berakhir = (TextView) findViewById(R.id.jam_berakhir);

            //keterangan();

            jam.setText(getCurrentClock());
            jam_bayangan.setText(getCurrentClock());

            Data_absen_ortu.this.mHandler.postDelayed(m_Runnable, 1000);
        }

    };

//    public void keterangan() {
//        // Menggunakan format 24-jam atau sesuaikan dengan format waktu Anda
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//
//        try {
//            // Mendapatkan teks dari TextView
//            String jamText = jam.getText().toString();
//            String jamTelatText = jam_telat.getText().toString();
//
//            // Pastikan nilai jamText dan jamTelatText tidak kosong
//            if (jamText.isEmpty() || jamTelatText.isEmpty()) {
//                keterangan.setText("Error: Nilai jam atau jam_telat kosong");
//                return;
//            }
//
//            // Debugging: Cek nilai jamText dan jamTelatText sebelum parsing
//            Log.d("DEBUG", "Nilai jam: " + jamText);
//            Log.d("DEBUG", "Nilai jam_telat: " + jamTelatText);
//
//            // Parsing nilai jamText dan jamTelatText menjadi objek Date
//            Date waktuMasuk = sdf.parse(jamText);
//            Date waktuTelat = sdf.parse(jamTelatText);
//
//            // Debugging: Print waktu yang telah diparse
//            Log.d("DEBUG", "Waktu Masuk: " + waktuMasuk.toString());
//            Log.d("DEBUG", "Waktu Telat: " + waktuTelat.toString());
//
//            // Membandingkan waktu masuk dengan waktu telat
//            if (waktuMasuk.after(waktuTelat)) {
//                keterangan.setText("Telat");
//            } else {
//                keterangan.setText("Masuk");
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            keterangan.setText("Error: Format waktu salah");
//        }
//    }





    public void absengak() {

        String gurualert = nama.getText().toString();
        String id_siswaalert = id_siswa.getText().toString();
        String nama_siswaalert = nama_siswa.getText().toString();
        String kelasalert = kelas.getText().toString();
        String jamalert = jam.getText().toString();



        //String a = validasib1.getText().toString();
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setTitle("ABSENSI");
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this)

                .setIcon(R.drawable.titik)
                .setTitle(R.string.app_name)
                .setMessage("Hallo "+gurualert+", anda akan melakukan izin untuk "+nama_siswaalert+" ?\ndengan NIS : "+id_siswaalert+"\nPada Pukul : "+jamalert+"\nDi kelas : "+kelasalert)
                .setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        prosesdasboard1();


                        // Animasi mengembalikan tombol ke posisi awal
                        ObjectAnimator.ofFloat(draggableButton, "x", originalX)
                                .setDuration(300)
                                .start();
                        ObjectAnimator.ofFloat(draggableButton, "y", originalY)
                                .setDuration(300)
                                .start();



                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Animasi mengembalikan tombol ke posisi awal
                        ObjectAnimator.ofFloat(draggableButton, "x", originalX)
                                .setDuration(300)
                                .start();
                        ObjectAnimator.ofFloat(draggableButton, "y", originalY)
                                .setDuration(300)
                                .start();

                        dialog.cancel();
                    }
                })
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
    }


    public void prosesdasboard1(){
        save();
        list();
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                pDialog.setMessage("Loading..."+ millisUntilFinished / 1000);
                showDialog();
                pDialog.setCanceledOnTouchOutside(false);

//                in();
//                out();
                //checkTimeAndUpdateView();
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                hideDialog();


            }
        }.start();

    }


    public String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("id", "ID")); // Format tanggal dalam bahasa Indonesia
        return dateFormat.format(c.getTime());
    }


    public String getCurrentClock(){
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        String strdate1 = sdf1.format(c1.getTime());
        return strdate1;


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else{
                // jika qrcode berisi data
                try{
                    // converting the data json
                    JSONObject object = new JSONObject(result.getContents());
                    // atur nilai ke textviews
                    id_siswa.setText(object.getString("id_siswa"));
                    nama_siswa.setText(object.getString("nama_siswa"));

//                    imgdatakunjungan.setVisibility(View.GONE);
//                    datakunjungan.setVisibility(View.VISIBLE);
                    //kunjunganterakhir();

                }catch (JSONException e){
                    e.printStackTrace();
                    // jika format encoded tidak sesuai maka hasil
                    // ditampilkan ke toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }


            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        // inisialisasi IntentIntegrator(scanQR)
        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void save() {

        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("nis", nis.getText().toString())
                .add("nama", nama.getText().toString())
                .add("profesi", profesi.getText().toString())
                .add("jam", jam.getText().toString())
                .add("tanggal", tanggal.getText().toString())
                .add("kelas", kelas.getText().toString())
                .add("id_siswa", id_siswa.getText().toString())
                .add("nama_siswa", nama_siswa.getText().toString())
                .add("keterangan", keterangan.getText().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "inputabsen.php")
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
                        String responseStatus = jsonResponse.optString("response");

                        runOnUiThread(() -> {
                            if ("success".equalsIgnoreCase(responseStatus)) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Absen Berhasil, Data Anda sedang diteruskan ke Manager Anda, untuk dilakukan verifikasi terlebih dahulu",
                                        Toast.LENGTH_LONG
                                ).show();
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "HARI INI KAMU SUDAH ABSEN",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }





    // Helper Method untuk Execute Request
//    private void executePostRequest(String url, String idSiswa, Data_absen_ortu.ResponseHandler responseHandler) {
//        try {
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("id_siswa", idSiswa);
//            executePostRequest(url, jsonBody, responseHandler);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void executePostRequest(String url, JSONObject jsonBody, Data_absen_ortu.ResponseHandler responseHandler) {
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



    private void list() {
        // Bersihkan data sebelum mengisi kembali
        aruskas.clear();
        listtest1.setAdapter(null);


        // Membuat body permintaan POST
        RequestBody body = new FormBody.Builder()
                .add("tanggal", tanggal.getText().toString())
                .add("kelas", kelas.getText().toString())
                .build();

        // Membuat permintaan POST
        Request request = new Request.Builder()
                .url(Config.host + "list_absen.php")
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

        CustomAdapter customAdapter = new CustomAdapter(this, aruskas, R.layout.list_absen_fix,
                new String[]{"id","nis", "nama", "profesi", "jam", "tanggal", "kelas", "id_siswa", "nama_siswa", "keterangan"},
                new int[]{R.id.id_list, R.id.nis_list, R.id.nama_list, R.id.profesi_list, R.id.jam_list, R.id.tanggal_list, R.id.kelas_list, R.id.id_siswa_list, R.id.nama_siswa_list, R.id.keterangan_list});

        listtest1.setAdapter(customAdapter);

        listtest1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        // Rest of your code...
    }

    private class CustomAdapter extends SimpleAdapter {

        public CustomAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            // Get the values from the data
            String absenListAbsen = aruskas.get(position).get("absen");
            String jamListAbsen = aruskas.get(position).get("jam");


            return view;



        }
    }



}