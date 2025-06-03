package com.example.muzamil;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.muzamil.helper.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataKelasActivity extends AppCompatActivity {
    private ListView listViewKelas;
    private TextView textViewJadwal;
    private TextView textViewTanggal;
    private ProgressDialog pDialog;
    private Context context;
    private ArrayList<HashMap<String, String>> kelasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kelas);

        // Initialize views
        listViewKelas = findViewById(R.id.listViewKelas);
        textViewJadwal = findViewById(R.id.textViewJadwal);
        textViewTanggal = findViewById(R.id.textViewTanggal);

        // Initialize context and progress dialog
        context = this;
        pDialog = new ProgressDialog(context);

        // Get data from intent
        String jadwal = getIntent().getStringExtra("jadwal");
        String tanggal = getIntent().getStringExtra("tanggal");

        // Set text views
        textViewJadwal.setText(jadwal);
        textViewTanggal.setText(tanggal);

        // Load data
        loadKelasData(jadwal, tanggal);
    }

    private void loadKelasData(String jadwal, String tanggal) {
        showDialog();

        Log.d("DataKelas", "Mengirim data - jadwal: " + jadwal + ", tanggal: " + tanggal);

        RequestBody body = new FormBody.Builder()
                .add("jadwal", jadwal)
                .add("tanggal", tanggal)
                .build();

        Request request = new Request.Builder()
                .url(Config.host + "get_kelas.php")
                .post(body)
                .build();

        Log.d("DataKelas", "URL Request: " + request.url());

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
                    String responseBody = response.body().string();
                    Log.d("DataKelas", "Response: " + responseBody);
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray jsonArray = jsonResponse.optJSONArray("result");

                        kelasList.clear();
                        if (jsonArray != null) {
                            Log.d("DataKelas", "Jumlah data: " + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", obj.optString("id"));
                                map.put("jadwal", obj.optString("jadwal"));
                                map.put("jam_mulai", obj.optString("jam_mulai"));
                                map.put("jam_telat", obj.optString("jam_telat"));
                                map.put("jam_berakhir", obj.optString("jam_berakhir"));
                                map.put("pelajaran", obj.optString("pelajaran"));
                                kelasList.add(map);
                                Log.d("DataKelas", "Data ke-" + (i+1) + ": " + map.toString());
                            }
                        } else {
                            Log.d("DataKelas", "JSON Array null");
                        }

                        runOnUiThread(() -> {
                            hideDialog();
                            updateListView();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("DataKelas", "Error parsing JSON: " + e.getMessage());
                        runOnUiThread(() -> {
                            hideDialog();
                            Toast.makeText(context, "Error parsing data", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    Log.e("DataKelas", "Response not successful or body is null");
                }
            }
        });
    }

    private void updateListView() {
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                kelasList,
                R.layout.list_item_kelas,
                new String[]{"jadwal", "pelajaran", "jam_mulai", "jam_telat", "jam_berakhir"},
                new int[]{R.id.textViewJadwal, R.id.textViewPelajaran, R.id.textViewJamMulai, 
                         R.id.textViewJamTelat, R.id.textViewJamBerakhir}
        );
        listViewKelas.setAdapter(adapter);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
} 