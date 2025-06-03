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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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
    TextView nis, nama, profesi, jam, tanggal, kelas, id_siswa, nama_siswa, keterangan, tanggalbayangan;

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

    private BottomSheetDialog bottomSheetDialog;
    private ListView listViewKelas;
    private ArrayList<HashMap<String, String>> kelasList = new ArrayList<>();

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
        tanggalbayangan = (TextView) findViewById(R.id.tanggalbayangan);

        jam_masuk = (TextView) findViewById(R.id.jam_masuk);
        jam_telat = (TextView) findViewById(R.id.jam_telat);
        jam_berakhir = (TextView) findViewById(R.id.jam_berakhir);

        buttonScan = (LinearLayout) findViewById(R.id.buttonScan);

        tanggal.setText(getCurrentDate());
        tanggalbayangan.setText(getCurrentDate2());
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

        // Initialize BottomSheet
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_kelas, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        listViewKelas = bottomSheetView.findViewById(R.id.listViewKelas);
        TextView textViewJadwal = bottomSheetView.findViewById(R.id.textViewJadwal);
        TextView textViewTanggal = bottomSheetView.findViewById(R.id.textViewTanggal);

        // Set initial values
        textViewJadwal.setText(kelas.getText().toString());
        textViewTanggal.setText(tanggal.getText().toString());

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

                        if (newX < 0) newX = 0;
                        if (newX + view.getWidth() > screenWidth)
                            newX = screenWidth - view.getWidth();

                        view.setX(newX);
                        break;

                    case MotionEvent.ACTION_UP:
                        if (view.getX() + view.getWidth() >= screenWidth / 2
                                && view.getY() <= 0) {
                            showKelasBottomSheet();
                        }

                        // Animasi mengembalikan tombol ke posisi awal
                        ObjectAnimator.ofFloat(draggableButton, "x", originalX)
                                .setDuration(300)
                                .start();
                        ObjectAnimator.ofFloat(draggableButton, "y", originalY)
                                .setDuration(300)
                                .start();
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

            jam.setText(getCurrentClock());
            jam_bayangan.setText(getCurrentClock());

            Data_absen_ortu.this.mHandler.postDelayed(m_Runnable, 1000);
        }
    };

    public void absengak() {
        String gurualert = nama.getText().toString();
        String id_siswaalert = id_siswa.getText().toString();
        String nama_siswaalert = nama_siswa.getText().toString();
        String kelasalert = kelas.getText().toString();
        String jamalert = jam.getText().toString();

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
            }
            public void onFinish() {
                hideDialog();
            }
        }.start();
    }

    public String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("id", "ID"));
        return dateFormat.format(c.getTime());
    }

    public String getCurrentDate2() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy", new Locale("id", "ID"));
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
                try{
                    JSONObject object = new JSONObject(result.getContents());
                    id_siswa.setText(object.getString("id_siswa"));
                    nama_siswa.setText(object.getString("nama_siswa"));
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
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

        Request request = new Request.Builder()
                .url(Config.host + "inputabsen.php")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
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

    private void list() {
        aruskas.clear();
        listtest1.setAdapter(null);

        RequestBody body = new FormBody.Builder()
                .add("tanggal", tanggal.getText().toString())
                .add("kelas", kelas.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url(Config.host + "list_absen.php")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
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
    }

    private class CustomAdapter extends SimpleAdapter {
        public CustomAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            String absenListAbsen = aruskas.get(position).get("absen");
            String jamListAbsen = aruskas.get(position).get("jam");

            return view;
        }
    }

    private void showKelasBottomSheet() {
        if (nama_siswa.getText().toString().equals("Data Siswa")) {
            Toast.makeText(getApplicationContext(), "Silahkan pilih data siswa",
                    Toast.LENGTH_LONG).show();
            return;
        }

        loadKelasData(kelas.getText().toString(), tanggal.getText().toString());
        bottomSheetDialog.show();
    }

    private void loadKelasData(String jadwal, String tanggal) {
        showDialog();

        RequestBody body = new FormBody.Builder()
                .add("jadwal", jadwal)
                .add("tanggal", tanggal)
                .build();

        Request request = new Request.Builder()
                .url(Config.host + "get_kelas.php")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    hideDialog();
                    Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonResponse.optJSONArray("result");

                        kelasList.clear();
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", obj.optString("id"));
                                map.put("nis", obj.optString("nis"));
                                map.put("namaguru", obj.optString("namaguru"));
                                map.put("jadwal", obj.optString("jadwal"));
                                map.put("jam_mulai", obj.optString("jam_mulai"));
                                map.put("jam_telat", obj.optString("jam_telat"));
                                map.put("jam_berakhir", obj.optString("jam_berakhir"));
                                map.put("pelajaran", obj.optString("pelajaran"));
                                kelasList.add(map);
                            }
                        }

                        runOnUiThread(() -> {
                            hideDialog();
                            updateKelasListView();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            hideDialog();
                            Toast.makeText(context, "Error parsing data", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        });
    }

    private void updateKelasListView() {
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                kelasList,
                R.layout.list_item_kelas,
                new String[]{"jadwal", "pelajaran", "nis", "namaguru", "jam_mulai", "jam_telat", "jam_berakhir"},
                new int[]{R.id.textViewJadwal, R.id.textViewPelajaran, R.id.textViewNIS, R.id.textViewNamaGuru, 
                         R.id.textViewJamMulai, R.id.textViewJamTelat, R.id.textViewJamBerakhir}
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                
                // Get the data for this position
                HashMap<String, String> data = kelasList.get(position);
                
                // Setup absen button
                Button btnAbsen = view.findViewById(R.id.btnAbsen);
                ProgressBar progressBar = view.findViewById(R.id.progressBar);
                ImageView imageViewCheck = view.findViewById(R.id.imageViewCheck);

                // Check if this item has been absen
                String key = data.get("id") + "_" + id_siswa.getText().toString();
                boolean isAbsen = getSharedPreferences("AbsenStatus", MODE_PRIVATE).getBoolean(key, false);
                
                if (isAbsen) {
                    // If already absen, show check mark
                    btnAbsen.setEnabled(false);
                    btnAbsen.setText("");
                    btnAbsen.setBackgroundResource(android.R.color.holo_green_light);
                    progressBar.setVisibility(View.GONE);
                    imageViewCheck.setVisibility(View.VISIBLE);
                } else {
                    // If not absen, show normal button
                    btnAbsen.setEnabled(true);
                    btnAbsen.setText("Absen");
                    btnAbsen.setBackgroundResource(android.R.color.holo_blue_light);
                    progressBar.setVisibility(View.GONE);
                    imageViewCheck.setVisibility(View.GONE);
                }
                
                // Set click listener
                btnAbsen.setOnClickListener(v -> {
                    // Show loading state
                    btnAbsen.setEnabled(false);
                    btnAbsen.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                    
                    // Add 1 second delay before sending request
                    new Handler().postDelayed(() -> {
                        saveKelas(data, btnAbsen, progressBar, imageViewCheck);
                    }, 1000);
                });
                
                return view;
            }
        };
        listViewKelas.setAdapter(adapter);
    }

    private void saveKelas(HashMap<String, String> data, Button btnAbsen, ProgressBar progressBar, ImageView imageViewCheck) {
        RequestBody body = new FormBody.Builder()
                .add("nis", data.get("nis"))
                .add("nama", data.get("namaguru"))
                .add("profesi", profesi.getText().toString())
                .add("kelas", kelas.getText().toString())
                .add("id_siswa", id_siswa.getText().toString())
                .add("nama_siswa", nama_siswa.getText().toString())
                .add("keterangan", "Izin")
                .add("mata_pelajaran", data.get("pelajaran"))
                .build();

        Request request = new Request.Builder()
                .url(Config.host + "inputabsen.php")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    // Reset button state
                    btnAbsen.setEnabled(true);
                    btnAbsen.setText("Absen");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String responseStatus = jsonResponse.optString("response");

                        runOnUiThread(() -> {
                            if ("success".equalsIgnoreCase(responseStatus)) {
                                // Show success state
                                progressBar.setVisibility(View.GONE);
                                imageViewCheck.setVisibility(View.VISIBLE);
                                btnAbsen.setBackgroundResource(android.R.color.holo_green_light);
                                
                                // Save absen status
                                String key = data.get("id") + "_" + id_siswa.getText().toString();
                                getSharedPreferences("AbsenStatus", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean(key, true)
                                    .apply();
                                
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Absen Izin Berhasil untuk mata pelajaran " + data.get("pelajaran"),
                                        Toast.LENGTH_LONG
                                ).show();
                            } else {
                                // Reset button state
                                btnAbsen.setEnabled(true);
                                btnAbsen.setText("Absen");
                                progressBar.setVisibility(View.GONE);
                                
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Gagal melakukan absen",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            // Reset button state
                            btnAbsen.setEnabled(true);
                            btnAbsen.setText("Absen");
                            progressBar.setVisibility(View.GONE);
                            
                            Toast.makeText(context, "Error parsing data", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        });
    }
}