package com.example.muzamil;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//import com.androidnetworking.AndroidNetworking;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.JSONObjectRequestListener;


import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Field;


import com.example.muzamil.helper.Config;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import java.util.function.Consumer;

public class Absensi extends AppCompatActivity implements View.OnClickListener {
TextView nis, nama, profesi, jam, tanggal, kelas, id_siswa, nama_siswa, keterangan, mata_pelajaran, tanggalbayangan;

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

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.absensi); // Mengatur layout absensi sebagai tampilan aktivitas

        this.mHandler = new Handler();
        m_Runnable.run(); // Memulai runnable untuk tugas yang mungkin dijalankan secara berkala

        // Inisialisasi komponen layout
        draggableButton = findViewById(R.id.draggableButton);
        mainLayout = findViewById(R.id.mainLayout);
        teks_absen = (TextView) findViewById(R.id.teks_absen);
        screenWidth = getResources().getDisplayMetrics().widthPixels; // Mendapatkan lebar layar perangkat

        context = Absensi.this; // Menyimpan context aktivitas untuk penggunaan lainnya
        pDialog = new ProgressDialog(context); // Membuat dialog progres untuk menampilkan loading

        // Inisialisasi elemen ListView
        listtest1 = (ListView) findViewById(R.id.listtest);

        // Inisialisasi TextView dari layout
        nis = (TextView) findViewById(R.id.nis);
        nama = (TextView) findViewById(R.id.nama);
        profesi = (TextView) findViewById(R.id.profesi);
        jam = (TextView) findViewById(R.id.jam);
        tanggal = (TextView) findViewById(R.id.tanggal);
        kelas = (TextView) findViewById(R.id.kelas);
        id_siswa = (TextView) findViewById(R.id.id_siswa);
        nama_siswa = (TextView) findViewById(R.id.nama_siswa);
        keterangan = (TextView) findViewById(R.id.keterangan);
        mata_pelajaran = (TextView) findViewById(R.id.mata_pelajaran);
        jam_bayangan = (TextView) findViewById(R.id.jam_bayangan);

        // Inisialisasi elemen jam kerja
        jam_masuk = (TextView) findViewById(R.id.jam_masuk);
        jam_telat = (TextView) findViewById(R.id.jam_telat);
        jam_berakhir = (TextView) findViewById(R.id.jam_berakhir);
        tanggalbayangan = (TextView) findViewById(R.id.tanggalbayangan);

        // Inisialisasi tombol scan dan menambahkan listener klik
        buttonScan = (LinearLayout) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(this);

        // Mengatur tanggal dan jam saat ini ke TextView
        tanggal.setText(getCurrentDate());
        tanggalbayangan.setText(getCurrentDate2());
        jam.setText(getCurrentClock());
        jam_bayangan.setText(getCurrentClock());

        // Mengambil data dari intent dan mengisinya ke TextView
        Intent i = getIntent();
        String kiriman = i.getStringExtra("nis");
        nis.setText(kiriman); // Menampilkan NIS
        String kiriman2 = i.getStringExtra("profesi");
        profesi.setText(kiriman2); // Menampilkan profesi
        String kiriman3 = i.getStringExtra("nama");
        nama.setText(kiriman3); // Menampilkan nama
        String kiriman4 = i.getStringExtra("kelas");
        kelas.setText(kiriman4); // Menampilkan kelas

        // Mengatur waktu kerja ke TextView
        String kiriman5 = i.getStringExtra("jam_mulai");
        jam_masuk.setText(kiriman5); // Menampilkan jam mulai
        String kiriman6= i.getStringExtra("jam_telat");
        jam_telat.setText(kiriman6); // Menampilkan jam telat
        String kiriman7 = i.getStringExtra("jam_berakhir");
        jam_berakhir.setText(kiriman7); // Menampilkan jam berakhir
        String kiriman8 = i.getStringExtra("mata_pelajaran");
        mata_pelajaran.setText(kiriman8); // Menampilkan jam berakhir

        // Menambahkan fungsi drag ke tombol draggableButton
        draggableButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        originalX = view.getX();
                        originalY = view.getY(); // Menyimpan posisi awal tombol
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;

                        // Membatasi pergerakan tombol agar tetap dalam layar
                        if (newX < 0) newX = 0;
                        if (newX + view.getWidth() > screenWidth)
                            newX = screenWidth - view.getWidth();

                        view.setX(newX); // Memindahkan tombol secara horizontal
                        break;

                    case MotionEvent.ACTION_UP:
                        if (view.getX() + view.getWidth() >= screenWidth / 2
                                && view.getY() <= 0) { // Mengecek posisi tombol di bagian tertentu
                            if (nama_siswa.getText().toString().equals("Data Siswa")) {
                                // Menampilkan pesan jika data siswa belum dipilih
                                Toast.makeText(getApplicationContext(), "Silahkan pilih data siswa",
                                        Toast.LENGTH_LONG).show();

                                // Mengembalikan tombol ke posisi semula
                                ObjectAnimator.ofFloat(draggableButton, "x", originalX)
                                        .setDuration(300)
                                        .start();
                                ObjectAnimator.ofFloat(draggableButton, "y", originalY)
                                        .setDuration(300)
                                        .start();
                            } else {
                                absengak(); // Memanggil fungsi absengak jika valid
                            }
                        } else {
                            // Mengembalikan tombol ke posisi awal jika tidak memenuhi syarat
                            ObjectAnimator.ofFloat(view, "x", originalX)
                                    .setDuration(300)
                                    .start();
                            ObjectAnimator.ofFloat(view, "y", originalY)
                                    .setDuration(300)
                                    .start();
                        }
                        break;

                    default:
                        return false; // Tidak menangani aksi lainnya
                }
                return true; // Mengindikasikan event telah ditangani
            }
        });

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Call your list() method to refresh data
                list();
                // Stop the refresh animation when done
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        list(); // Memanggil fungsi list untuk mungkin mengisi data ListView
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

            keterangan();

            jam.setText(getCurrentClock());
            jam_bayangan.setText(getCurrentClock());

            Absensi.this.mHandler.postDelayed(m_Runnable, 1000);
        }

    };

    public void keterangan() {
        // Menggunakan format 24-jam atau sesuaikan dengan format waktu Anda
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try {
            // Mendapatkan teks dari TextView
            String jamText = jam.getText().toString();
            String jamTelatText = jam_telat.getText().toString();

            // Pastikan nilai jamText dan jamTelatText tidak kosong
            if (jamText.isEmpty() || jamTelatText.isEmpty()) {
                keterangan.setText("Error: Nilai jam atau jam_telat kosong");
                return;
            }

            // Debugging: Cek nilai jamText dan jamTelatText sebelum parsing
            Log.d("DEBUG", "Nilai jam: " + jamText);
            Log.d("DEBUG", "Nilai jam_telat: " + jamTelatText);

            // Parsing nilai jamText dan jamTelatText menjadi objek Date
            Date waktuMasuk = sdf.parse(jamText);
            Date waktuTelat = sdf.parse(jamTelatText);

            // Debugging: Print waktu yang telah diparse
            Log.d("DEBUG", "Waktu Masuk: " + waktuMasuk.toString());
            Log.d("DEBUG", "Waktu Telat: " + waktuTelat.toString());

            // Membandingkan waktu masuk dengan waktu telat
            if (waktuMasuk.after(waktuTelat)) {
                keterangan.setText("Telat");
            } else {
                keterangan.setText("Masuk");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            keterangan.setText("Error: Format waktu salah");
        }
    }





    public void absengak() {
        // Mengambil data dari TextView
        String gurualert = nama.getText().toString();
        String id_siswaalert = id_siswa.getText().toString();
        String nama_siswaalert = nama_siswa.getText().toString();
        String kelasalert = kelas.getText().toString();
        String jamalert = jam.getText().toString();
        // Membuat dialog untuk konfirmasi absensi
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false); // Dialog tidak dapat ditutup dengan menyentuh luar
        dialog.setCancelable(false); // Dialog tidak dapat ditutup dengan tombol back
        dialog.setTitle("ABSENSI");

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.titik) // Menampilkan ikon
                .setTitle(R.string.app_name) // Menampilkan judul dialog
                .setMessage("Hallo " + gurualert + ", anda akan melakukan absen untuk "
                        + nama_siswaalert + " ?\nDengan NIS : " + id_siswaalert
                        + "\nPada Pukul : " + jamalert
                        + "\nDi kelas : " + kelasalert) // Pesan konfirmasi
                .setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prosesdasboard1(); // Memanggil proses absensi

                        // Mengembalikan tombol ke posisi awal
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
                        // Mengembalikan tombol ke posisi awal jika dibatalkan
                        ObjectAnimator.ofFloat(draggableButton, "x", originalX)
                                .setDuration(300)
                                .start();
                        ObjectAnimator.ofFloat(draggableButton, "y", originalY)
                                .setDuration(300)
                                .start();
                        dialog.cancel(); // Menutup dialog
                    }
                })
                .show();

        alertDialog.setCanceledOnTouchOutside(false); // Tidak dapat ditutup dengan menyentuh luar
    }

    public void prosesdasboard1() {
        save(); // Menyimpan data
        list(); // Memperbarui data list

        // Menampilkan dialog loading selama 1 detik
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
                pDialog.setMessage("Loading..." + millisUntilFinished / 1000); // Pesan loading
                showDialog(); // Menampilkan dialog
                pDialog.setCanceledOnTouchOutside(false); // Tidak dapat ditutup dengan menyentuh luar
            }

            public void onFinish() {
                hideDialog(); // Menyembunyikan dialog setelah selesai
            }
        }.start();
    }

    public String getCurrentDate() {
        // Mendapatkan tanggal saat ini dalam format yyyy/MM/dd
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("id", "ID"));
        return dateFormat.format(c.getTime());
    }


    public String getCurrentDate2() {
        // Mendapatkan tanggal saat ini dalam format yyyy/MM/dd
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy", new Locale("id", "ID"));
        return dateFormat.format(c.getTime());
    }

    public String getCurrentClock() {
        // Mendapatkan waktu saat ini dalam format HH:mm
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        String strdate1 = sdf1.format(c1.getTime());
        return strdate1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Menangani hasil scan QR Code
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // Jika tidak ada data yang ditemukan
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // Mengonversi hasil scan ke JSONObject
                    JSONObject object = new JSONObject(result.getContents());
                    // Menampilkan data pada TextView
                    id_siswa.setText(object.getString("id_siswa"));
                    nama_siswa.setText(object.getString("nama_siswa"));

                    // Memanggil method absen() setelah data berhasil ditampilkan
                    absengak();
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Menampilkan hasil scan sebagai teks jika format tidak sesuai
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data); // Memanggil fungsi bawaan jika tidak ada hasil
        }
    }



    @Override
    public void onClick(View v) {
        // inisialisasi IntentIntegrator(scanQR)
        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }




    private void save() {
        FormBody formBody = new FormBody.Builder()
                .add("nis", nis.getText().toString())
                .add("nama", nama.getText().toString())
                .add("profesi", profesi.getText().toString())
                .add("jam", jam.getText().toString())
                .add("tanggal", tanggal.getText().toString())
                .add("kelas", kelas.getText().toString())
                .add("id_siswa", id_siswa.getText().toString())
                .add("nama_siswa", nama_siswa.getText().toString())
                .add("keterangan", keterangan.getText().toString())
                .add("mata_pelajaran", mata_pelajaran.getText().toString())
                .build();

        executePostRequest(Config.host + "inputabsen.php", formBody, response -> {
            String result = response.optString("response");
            if ("success".equals(result)) {
                Toast.makeText(getApplicationContext(),
                        "Absen Berhasil",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "HARI INI KAMU SUDAH ABSEN",
                        Toast.LENGTH_LONG).show();
            }
        }, error -> {
            Toast.makeText(getApplicationContext(),
                    "Gagal mengirim data: " + error.getMessage(),
                    Toast.LENGTH_LONG).show();
        });
    }




    //show dialog
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
//hide dialog
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



//listview untuk tampil data berdasarkan tanggal dan kelas
private void list() {
    // Show refresh animation
    swipeRefreshLayout.setRefreshing(true);
    
    // Bersihkan data sebelum mengisi kembali
    aruskas.clear();
    listtest1.setAdapter(null);

    FormBody formBody = new FormBody.Builder()
            .add("tanggal", tanggal.getText().toString())
            .add("kelas", kelas.getText().toString())
            .add("nis", nis.getText().toString())

            .build();

    executePostRequest(Config.host + "list_absen.php", formBody, response -> {
        JSONArray jsonArray = response.optJSONArray("result");
        if (jsonArray != null) {
            try {
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
                Adapter();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),
                        "Error parsing response: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
        
        // Stop refresh animation when data is loaded
        swipeRefreshLayout.setRefreshing(false);
    }, error -> {
        Toast.makeText(getApplicationContext(),
                "Gagal mengambil data: " + error.getMessage(),
                Toast.LENGTH_LONG).show();
        // Stop refresh animation on error
        swipeRefreshLayout.setRefreshing(false);
    });
}


    private void executePostRequest(String url, FormBody formBody, Consumer<JSONObject> onResponse, Consumer<Exception> onError) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    runOnUiThread(() -> onError.accept(e));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            runOnUiThread(() -> onResponse.accept(jsonResponse));
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            runOnUiThread(() -> onError.accept(new Exception("Server error: " + response.code())));
                        }
                    }
                } catch (Exception e) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        runOnUiThread(() -> onError.accept(e));
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