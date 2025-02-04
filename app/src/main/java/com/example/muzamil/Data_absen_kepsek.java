package com.example.muzamil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.function.Consumer;

public class Data_absen_kepsek extends AppCompatActivity {
    ListView listtest1;
    TextView id_siswa, nama_siswa, tanggal;
    Button caritanggal;
    public static String LINK, idlist, tanggallist, jamlist, kecamatanlist, absenlist, keteranganlist, statuslist, pendinglist;
    ArrayList<HashMap<String, String>> aruskas = new ArrayList<HashMap<String, String>>();

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;


    //Sama Seperti Data_absen cuma beda mengambil data Kepsek



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_absen_kepsek);

        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        listtest1 = (ListView) findViewById(R.id.listtest);

        id_siswa = (TextView) findViewById(R.id.id_siswa);
        nama_siswa = (TextView) findViewById(R.id.nama_siswa);
        tanggal = (TextView) findViewById(R.id.tanggal);

        caritanggal = (Button) findViewById(R.id.caritanggal);


        Intent i = getIntent();
        String kiriman = i.getStringExtra("id_siswa");
        id_siswa.setText(kiriman);
        String kiriman2 = i.getStringExtra("nama_siswa");
        nama_siswa.setText(kiriman2);

        tanggal.setText(getCurrentDate());

        caritanggal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                list();

            }

        });

        tanggal.setText(getCurrentDate());

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog1();
            }
        });


        list();



    }

    private void showDateDialog1(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
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


    public String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", new Locale("id", "ID")); // Format tanggal dalam bahasa Indonesia
        return dateFormat.format(c.getTime());
    }

    private void list() {
        // Bersihkan data sebelum mengisi kembali
        aruskas.clear();
        listtest1.setAdapter(null);

        FormBody formBody = new FormBody.Builder()
                .add("tanggal", tanggal.getText().toString())
                .build();


        executePostRequest(Config.host + "list_absen_kepsek.php", formBody, response -> {
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
        }, error -> {
            Toast.makeText(getApplicationContext(),
                    "Gagal mengambil data: " + error.getMessage(),
                    Toast.LENGTH_LONG).show();
        });

    }




    private void Adapter(){

        Data_absen_kepsek.CustomAdapter customAdapter = new Data_absen_kepsek.CustomAdapter(this, aruskas, R.layout.list_absen_fix,
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

            // Check conditions and set text color accordingly
//            TextView absenListAbsenTextView = view.findViewById(R.id.absenlistabsen);
//            TextView jamListAbsenTextView = view.findViewById(R.id.jamlistabsen);
//            if (absenListAbsen.equals("DATANG")) {
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//                try {
//                    Date jamAbsen = sdf.parse(jamListAbsen);
//                    Date thresholdTime = sdf.parse("08:00:00");
//
//                    if (jamAbsen.after(thresholdTime)) {
//                        jamListAbsenTextView.setTextColor(Color.RED);
//                    } else {
//                        jamListAbsenTextView.setTextColor(Color.BLACK);
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                // Set default text color for other values of absenListAbsen
//                jamListAbsenTextView.setTextColor(Color.BLACK);
//            }

            return view;
        }
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


}