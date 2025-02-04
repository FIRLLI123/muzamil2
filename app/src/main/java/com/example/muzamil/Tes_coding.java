package com.example.muzamil;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.*;
import com.example.muzamil.helper.Config;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Tes_coding extends AppCompatActivity {

    private static final String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
    public Spinner sp;

    private ProgressDialog pDialog;
    private Context context;

    ListView listtest1;

    TextView idabsen;

    TextView namasales, jam, lokasilengkap, longitude, latitude, tanggal, kecamatan, keterangan, status, absen,posisi;

    TextView tanggalbayangan, jambayangan;
    TextView in,out;

    TextView tanggaldari,tanggalsampai;

    Button btnabsen, caritanggal;
    FusedLocationProviderClient fusedLocationProviderClient;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;



    public static String LINK, idlist, tanggallist, jamlist, kecamatanlist, absenlist, keteranganlist, statuslist, pendinglist;
    ArrayList<HashMap<String, String>> aruskas = new ArrayList<HashMap<String, String>>();

    Handler mHandler;



    private Button draggableButton;
    private LinearLayout mainLayout;
    private float dX, dY;
    private float originalX, originalY;
    private int screenWidth;

    TextView teks_absen;

    private Handler handler = new Handler();
    private Runnable runnable;

    public static final String PREFS_NAME = "MyPrefsFile";

    private BroadcastReceiver dataRefreshedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Refresh UI Activity ketika data telah diperbarui
            // Misalnya, memperbarui tampilan data di Activity
            // Di sini Anda bisa memanggil metode refreshUI() untuk memperbarui tampilan
            refreshUI();
        }
    };

    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tes_coding);


//        // Daftarkan BroadcastReceiver untuk menerima pembaruan data dari Service
//        registerReceiver(dataRefreshedReceiver, new IntentFilter("DATA_REFRESHED"));
//// Memulai Service untuk proses refresh
//        startService(new Intent(this, RefreshService.class));






        this.mHandler = new Handler();
        m_Runnable.run();


        LINK = Config.host + "history.php";
        jamlist = "";
        tanggallist = "";
        kecamatanlist = "";
        absenlist = "";
        keteranganlist = "";
        statuslist = "";
        pendinglist = "";
        idlist = "";


        draggableButton = findViewById(R.id.draggableButton);
        mainLayout = findViewById(R.id.mainLayout);
        teks_absen = (TextView) findViewById(R.id.teks_absen);
        screenWidth = getResources().getDisplayMetrics().widthPixels;


        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);


        sp = (Spinner)findViewById(R.id.spinner);
        idabsen = (TextView) findViewById(R.id.idabsen);
        namasales = (TextView) findViewById(R.id.namasales);
        jam = (TextView) findViewById(R.id.jam);
        lokasilengkap = (TextView) findViewById(R.id.lokasilengkap);
        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        tanggal = (TextView) findViewById(R.id.tanggal);
        kecamatan = (TextView) findViewById(R.id.kecamatan);
        keterangan = (TextView) findViewById(R.id.keterangan);
        status = (TextView) findViewById(R.id.status);
        absen = (TextView) findViewById(R.id.absen);

        posisi = (TextView) findViewById(R.id.posisi);

        jambayangan = (TextView) findViewById(R.id.jambayangan);
        tanggalbayangan = (TextView) findViewById(R.id.tanggalbayangan);

        caritanggal = (Button) findViewById(R.id.caritanggal);

        tanggaldari = (TextView) findViewById(R.id.tanggaldari);
        tanggalsampai = (TextView) findViewById(R.id.tanggalsampai);

        in = (TextView) findViewById(R.id.in);
        out = (TextView) findViewById(R.id.out);

        listtest1 = (ListView) findViewById(R.id.listtest);



        context = Tes_coding.this;
        pDialog = new ProgressDialog(context);

//        Intent kolomlogin = getIntent();
//        String kiriman1 = kolomlogin.getStringExtra("nama");
//        namasales.setText(kiriman1);
//
//        String kiriman2 = kolomlogin.getStringExtra("posisi");
//        posisi.setText(kiriman2);


        // Menerima data dari Intent saat aktivitas pertama kali dibuka
        Intent kolomlogin = getIntent();
        String kiriman1 = kolomlogin.getStringExtra("nama");
        String kiriman2 = kolomlogin.getStringExtra("posisi");

        // Menyimpan data ke SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("nama", kiriman1);
        editor.putString("posisi", kiriman2);
        editor.apply();

        // Menampilkan data di TextView
        namasales.setText(kiriman1);
        posisi.setText(kiriman2);


        btnabsen =(Button) findViewById(R.id.btnabsen);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        tanggal.setText(getCurrentDate());
        jam.setText(jamotomatis());

        tanggalbayangan.setText(getCurrentDateBayangan());
        jambayangan.setText(jamotomatisBayangan());



        tanggaldari.setText(getCurrentDate());
        tanggalsampai.setText(getCurrentDate());

        tanggaldari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog1();
            }
        });



        tanggalsampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog2();
            }
        });


        //getLastLocation();


        caritanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listtangal();
            }
        });

        btnabsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                if ( kecamatan.getText().toString().equals("BELUM TERDETEKSI")){
                    //1
                    Toast.makeText(getApplicationContext(), "LOKASI BELUM TERDETEKSI",
                            Toast.LENGTH_LONG).show();

                    //Toast.makeText(getApplicationContext(), "Silahkan pilih terlebih dahulu", Toast.LENGTH_LONG).show();
                }else if ( out.getText().toString().length() == 8){
                    //1
                    Toast.makeText(getApplicationContext(), "ANDA SUDAH ABSEN",
                            Toast.LENGTH_LONG).show();

                    //Toast.makeText(getApplicationContext(), "Silahkan pilih terlebih dahulu", Toast.LENGTH_LONG).show();
                }else if (keterangan.getText().toString().length() == 0) {        //2

                    Toast.makeText(getApplicationContext(), "Silahkan isi terlebih dahulu", Toast.LENGTH_LONG).show();


                }else if (status.getText().toString().equals("DITOLAK")) {        //2

                    Toast.makeText(getApplicationContext(), "GAGAL, HAPUS ABSEN MU TERLEBIH DAHULU KARENA STATUS DI TOLAK", Toast.LENGTH_LONG).show();


                }  else {


                    absengak();



                }

            }
        });


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


       // list();
        inouttimer();

//        Calendar calendar = Calendar.getInstance();
//        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//
//        String waktuAbsen = in.getText().toString();
//        if (waktuAbsen.equals("--:--")&& (hourOfDay > 04 || (hourOfDay == 04 && minute >= 0))) {
//            // Jika waktu absen adalah "--:--", munculkan notifikasi
//            showNotification("Waktunya Absen", "Saatnya untuk absen sekarang!");
//        }else{
//
//        }


        List<String> item = new ArrayList<>();
        item.add("TAP");
        item.add("EVENT");
        item.add("MEETING");
        item.add("IZIN TELAT");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkTimeAndUpdateView();
                if (sp.getSelectedItem().toString().trim().equals("IZIN TELAT")) {
                    keterangan.setVisibility(View.VISIBLE);
                    keterangan.setText("");
                    keterangan.setHint("Silahkan isi jika datang/pulang tidak sesuai jam kerja");
                } else {
                    keterangan.setVisibility(View.GONE);
                    keterangan.setText(sp.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Tidak melakukan apapun
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                checkTimeAndUpdateView();
                handler.postDelayed(this, 1000); // Refresh setiap 60 detik (1 menit)
            }
        };
        handler.post(runnable);




    }


    private void checkTimeAndUpdateView() {
        // Mendapatkan waktu saat ini
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String absenStr = teks_absen.getText().toString().trim();
//        String absenstr = absen.getText().toString();
//        String absenstr2 = teks_absen.getText().toString();

        // Kondisi untuk pilihan Spinner berdasarkan waktu dan nilai absen
        List<String> item = new ArrayList<>();
        if (absenStr.equals("Absen Masuk") && (hour > 8 || (hour == 8 && minute > 0))) {
            // Jika absen = DATANG dan lewat jam 08:10, pilihan Spinner hanya "IZIN TELAT"
            item.add("IZIN TELAT");
        } else {
            // Jika absen = PULANG atau belum lewat jam 08:10, pilihan Spinner normal
            item.add("TAP");
            item.add("EVENT");
            item.add("MEETING");
            item.add("IZIN TELAT");

            keterangan.setVisibility(View.GONE);
            keterangan.setText(sp.getSelectedItem().toString());
        }
        adapter.clear();
        adapter.addAll(item);
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void onDestroy() {
        // Unregister BroadcastReceiver ketika Activity dihancurkan
        unregisterReceiver(dataRefreshedReceiver);
        super.onDestroy();
    }

    // Metode untuk memperbarui tampilan UI Activity
    private void refreshUI() {
        // Tambahkan kode untuk memperbarui tampilan sesuai dengan data baru
        // Misalnya, memperbarui ListView atau RecyclerView dengan data baru
        // Anda juga bisa menampilkan pesan toast atau melakukan tindakan lainnya

        //list();
        prosesdasboard1();

        Toast.makeText(this, "Data diperbarui", Toast.LENGTH_SHORT).show();
    }



    private void refreshView(ArrayList<HashMap<String, String>> updatedData) {
        // Lakukan pembaruan tampilan di sini

        // Mengambil data dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String namaSales = sharedPreferences.getString("nama", ""); // Mengambil nama sales, defaultnya kosong
        String posisiSales = sharedPreferences.getString("posisi", ""); // Mengambil posisi sales, defaultnya kosong

        // Menampilkan data di TextView
        namasales.setText(namaSales);
        posisi.setText(posisiSales);

       // list();




    }




    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            //Toast.makeText(AbsendanIzin.this, "", Toast.LENGTH_SHORT).show();


            sp = (Spinner)findViewById(R.id.spinner);
            idabsen = (TextView) findViewById(R.id.idabsen);
            namasales = (TextView) findViewById(R.id.namasales);
            jam = (TextView) findViewById(R.id.jam);
            lokasilengkap = (TextView) findViewById(R.id.lokasilengkap);
            longitude = (TextView) findViewById(R.id.longitude);
            latitude = (TextView) findViewById(R.id.latitude);
            tanggal = (TextView) findViewById(R.id.tanggal);
            kecamatan = (TextView) findViewById(R.id.kecamatan);
            keterangan = (TextView) findViewById(R.id.keterangan);
            status = (TextView) findViewById(R.id.status);
            absen = (TextView) findViewById(R.id.absen);

            posisi = (TextView) findViewById(R.id.posisi);

            jambayangan = (TextView) findViewById(R.id.jambayangan);
            tanggalbayangan = (TextView) findViewById(R.id.tanggalbayangan);

            caritanggal = (Button) findViewById(R.id.caritanggal);

            tanggaldari = (TextView) findViewById(R.id.tanggaldari);
            tanggalsampai = (TextView) findViewById(R.id.tanggalsampai);
            listtest1 = (ListView) findViewById(R.id.listtest);

            in = (TextView) findViewById(R.id.in);
            out = (TextView) findViewById(R.id.out);

            //checkTimeAndUpdateView();

            //in();
//            out();
            //list();
            //getLastLocation();

            Tes_coding.this.mHandler.postDelayed(m_Runnable, 1000);
        }

    };


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
                tanggaldari.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }


    private void showDateDialog2(){

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
                tanggalsampai.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }



//    private void list() {
//        // Bersihkan data sebelum mengisi kembali
//        aruskas.clear();
//        listtest1.setAdapter(null);
//
//        AndroidNetworking.post(Config.host + "listabsen_fix.php")
//                .addBodyParameter("namasales", namasales.getText().toString())
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray jsonArray = response.optJSONArray("result");
//
////                            String nama = namasales.getText().toString();
////                            if (!hasTodayAbsen(jsonArray) && isAfter4_30PM()) {
////                                showNotification("Hallo " +nama, "Anda belum melakukan absen hari ini di Jarvis. Silakan lakukan absen.");
////                            }
//
//                            // Mengirim data absen ke layanan
////                            if (jsonArray != null) {
////                                Intent intent = new Intent(Absen_fix.this, AbsenNotificationService.class);
////                                intent.putExtra("absenData", jsonArray.toString());
////                                startService(intent);
////                            }
//
//                            // Proses data absen jika ada
//                            if (jsonArray != null) {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject responses = jsonArray.getJSONObject(i);
//                                    //Data_BayarEX item = new Data_BayarEX();
//                                    HashMap<String, String> map = new HashMap<String, String>();
//                                    map.put("id", responses.optString("id"));
//                                    map.put("tanggal", responses.optString("tanggal"));
//                                    map.put("jam", responses.optString("jam"));
//                                    map.put("kecamatan", responses.optString("kecamatan"));
//                                    map.put("absen", responses.optString("absen"));
//                                    map.put("keterangan", responses.optString("keterangan"));
//                                    map.put("status", responses.optString("status"));
//                                    aruskas.add(map);
//                                }
//                            }
//
//                            Adapter();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    // Method untuk memeriksa apakah ada data absen untuk tanggal hari ini
//                    private boolean hasTodayAbsen(JSONArray jsonArray) throws JSONException {
//                        // Mendapatkan tanggal hari ini
//                        Calendar calendar = Calendar.getInstance();
//                        int year = calendar.get(Calendar.YEAR);
//                        int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so add 1
//                        int day = calendar.get(Calendar.DAY_OF_MONTH);
//                        final String todayDate = year + "/" + String.format("%02d", month) + "/" + String.format("%02d", day);
//
//                        // Memeriksa setiap entri absen untuk tanggal hari ini
//                        if (jsonArray != null) {
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject responses = jsonArray.getJSONObject(i);
//                                String tanggalAbsen = responses.optString("tanggal");
//                                // Jika ada entri absen untuk tanggal hari ini, kembalikan true
//                                if (tanggalAbsen.equals(todayDate)) {
//                                    return true;
//                                }
//                            }
//                        }
//                        // Jika tidak ada entri absen untuk tanggal hari ini, kembalikan false
//                        return false;
//                    }
//
//                    // Method untuk memeriksa apakah sudah setelah jam 16:30
//                    private boolean isAfter4_30PM() {
//                        Calendar calendar = Calendar.getInstance();
//                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                        int minute = calendar.get(Calendar.MINUTE);
//                        // Cek apakah jam saat ini setelah jam 16:30
//                        return hour >= 8 || (hour == 8 && minute >= 00);
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        // Handle error
//                    }
//                });
//    }
//
//
//
//
//
//
//    private void Adapter(){
//
//        CustomAdapter customAdapter = new CustomAdapter(this, aruskas, R.layout.list_absen_fix,
//                new String[]{"id","tanggal", "jam", "kecamatan", "absen", "keterangan", "status"},
//                new int[]{R.id.idlistabsen, R.id.tanggallistabsen, R.id.jamlistabsen, R.id.kecamatanlistabsen, R.id.absenlistabsen, R.id.keteranganlistabsen, R.id.statuslistabsen});
//
//        listtest1.setAdapter(customAdapter);
//
//        listtest1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //no    = ((TextView) view.findViewById(R.id.no)).getText().toString();
//                idlist = ((TextView) view.findViewById(R.id.idlistabsen)).getText().toString();
//                statuslist = ((TextView) view.findViewById(R.id.statuslistabsen)).getText().toString();
//
//                idabsen.setText(idlist);
//                //tes1.setText(tanggallist);
//
//
//
//                String a = idabsen.getText().toString();
//                //String b = namasaleslist1.getText().toString();
//                //String c = namasalesinputperdana1.getText().toString();
//                Intent i = new Intent(getApplicationContext(), Detail_Absen.class);
//                i.putExtra("id",""+a+"");
//                startActivity(i);
//
//            }
//        });
//        // Rest of your code...
//    }

//    private class CustomAdapter extends SimpleAdapter {
//
//        public CustomAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
//            super(context, data, resource, from, to);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View view = super.getView(position, convertView, parent);
//
//            // Get the values from the data
//            String absenListAbsen = aruskas.get(position).get("absen");
//            String jamListAbsen = aruskas.get(position).get("jam");
//
//            // Check conditions and set text color accordingly
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
//
//            return view;
//        }
//    }
//
//
//    private void showNotification(String title, String message) {
//        Intent intent = new Intent(this, Awal3.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String channelId = getString(R.string.notification_channel_id);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.date2)
//                        .setContentTitle(title)
//                        .setContentText(message)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent);
//
//        // Since Android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }


//    private void listtangal(){
//
//        //swipe_refresh.setRefreshing(true);
//        aruskas.clear();
//        listtest1.setAdapter(null);
//
//        //Log.d("link", LINK );
//        AndroidNetworking.post( Config.host + "listabsen_fix_tanggal.php" )
//                .addBodyParameter("namasales", namasales.getText().toString())
//                .addBodyParameter("tanggaldari", tanggaldari.getText().toString())
//                .addBodyParameter("tanggalsampai", tanggalsampai.getText().toString())
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//
//
//
//                        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
///*
//                        text_masuk.setText(
//                                rupiahFormat.format(response.optDouble("yes")));
//                        text_keluar.setText(
//                                rupiahFormat.format( response.optDouble("oke") ));
//                        text_total.setText(
//                                rupiahFormat.format( response.optDouble("saldo") ));
//**/
//
//                        try {
//                            JSONArray jsonArray = response.optJSONArray("result");
//                            for (int i = 0; i < jsonArray.length(); i++) {
////                                Data_BayarEX item = new Data_BayarEX();
//                                JSONObject responses    = jsonArray.getJSONObject(i);
//                                HashMap<String, String> map = new HashMap<String, String>();
//                                //map.put("no",         responses.optString("no"));
//                                map.put("id",         responses.optString("id"));
//                                map.put("tanggal",         responses.optString("tanggal"));
//                                map.put("jam",       responses.optString("jam"));
//                                map.put("kecamatan",       responses.optString("kecamatan"));
//                                map.put("absen",       responses.optString("absen"));
//                                map.put("keterangan",       responses.optString("keterangan"));
//                                map.put("status",       responses.optString("status"));
//
//
//
//                                //total += Integer.parseInt(responses.getString("harga"))* Integer.parseInt(responses.getString("qty"));
//                                //map.put("tanggal",      responses.optString("tanggal"));
//
//                                aruskas.add(map);
//                                //bayarList.add(item);
//                            }
//
//                            Adapter2();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
////                        ttl.setText("Total : Rp "+formatter.format(total));
////                        total = 0;
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });
//    }
//
//
//
//
//
//    private void Adapter2() {
//        CustomAdapter2 customAdapter = new CustomAdapter2(this, aruskas, R.layout.list_absen_fix,
//                new String[]{"id","tanggal", "jam", "kecamatan", "absen", "keterangan", "status"},
//                new int[]{R.id.idlistabsen, R.id.tanggallistabsen, R.id.jamlistabsen, R.id.kecamatanlistabsen, R.id.absenlistabsen, R.id.keteranganlistabsen, R.id.statuslistabsen});
//
//        listtest1.setAdapter(customAdapter);
//
//        listtest1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //no    = ((TextView) view.findViewById(R.id.no)).getText().toString();
//                idlist = ((TextView) view.findViewById(R.id.idlistabsen)).getText().toString();
//                statuslist = ((TextView) view.findViewById(R.id.statuslistabsen)).getText().toString();
//
//                idabsen.setText(idlist);
//                //tes1.setText(tanggallist);
//
//
//
//                String a = idabsen.getText().toString();
//                //String b = namasaleslist1.getText().toString();
//                //String c = namasalesinputperdana1.getText().toString();
//                Intent i = new Intent(getApplicationContext(), Detail_Absen.class);
//                i.putExtra("id",""+a+"");
//                startActivity(i);
//
//            }
//        });
//
//
//        // Rest of your code...
//    }
//
//    private class CustomAdapter2 extends SimpleAdapter {
//
//        public CustomAdapter2(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
//            super(context, data, resource, from, to);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View view = super.getView(position, convertView, parent);
//
//            // Get the values from the data
//            String absenListAbsen = aruskas.get(position).get("absen");
//            String jamListAbsen = aruskas.get(position).get("jam");
//
//            // Check conditions and set text color accordingly
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
//
//            return view;
//        }
//    }


//    private void Adapter2() {
//
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskas, R.layout.list_absen_fix,
//                new String[]{"id","tanggal", "jam", "kecamatan", "absen", "keterangan", "status"},
//                new int[]{R.id.idlistabsen,R.id.tanggallistabsen, R.id.jamlistabsen, R.id.kecamatanlistabsen, R.id.absenlistabsen, R.id.keteranganlistabsen, R.id.statuslistabsen});
//
//        listtest1.setAdapter(simpleAdapter);
//
//        listtest1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //no    = ((TextView) view.findViewById(R.id.no)).getText().toString();
//                idlist = ((TextView) view.findViewById(R.id.idlistabsen)).getText().toString();
//                statuslist = ((TextView) view.findViewById(R.id.statuslistabsen)).getText().toString();
//
//                idabsen.setText(idlist);
//                //tes1.setText(tanggallist);
//
//
//
//                String a = idabsen.getText().toString();
//                //String b = namasaleslist1.getText().toString();
//                //String c = namasalesinputperdana1.getText().toString();
//                Intent i = new Intent(getApplicationContext(), Detail_Absen.class);
//                i.putExtra("id",""+a+"");
//                startActivity(i);
//
//            }
//        });
//    }



    public void absengak() {

        String namasalesalert = namasales.getText().toString();
        String absenalert = absen.getText().toString();
        String lokasialert = kecamatan.getText().toString();
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
                .setMessage("Hallo "+namasalesalert+", anda akan melakukan absen "+absenalert+" ?\nDi Lokasi : "+lokasialert+"\nPada Pukul : "+jamalert)
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
//        save();
        //list();
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                pDialog.setMessage("Loading..."+ millisUntilFinished / 1000);
                showDialog();
                pDialog.setCanceledOnTouchOutside(false);

                //in();
//                out();
                checkTimeAndUpdateView();
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                hideDialog();



            }
        }.start();

    }


    public void inouttimer(){

        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                pDialog.setMessage("Loading..."+ millisUntilFinished / 1000);
                showDialog();
                pDialog.setCanceledOnTouchOutside(false);
                //in();
//                out();
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                hideDialog();


            }
        }.start();

    }



    public String getCurrentDateBayangan() {
        //SimpleDateFormat contoh1 = new SimpleDateFormat("yyyy/MM/dd");
        //String hariotomatis = contoh1.format(c.getTime());
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat contoh1 = new SimpleDateFormat("EEEE, dd-MMMM-yyyy", Locale.getDefault());
//        int year, month, day;
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DATE);
        //SimpleDateFormat contoh1 = new SimpleDateFormat("EEEE, yyyy/MM/dd");

        String hariotomatis = contoh1.format(c.getTime());
        //hari.setText(hariotomatis);
        return hariotomatis;
        //return day +"/" + (month+1) + "/" + year;
        //return (month+1) +"/" + day + "/" + year;
        //return year + "/" + (month + 1) + "/" + day;

    }

    public String jamotomatisBayangan(){
        Calendar c1 = Calendar.getInstance();
        //SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy h:m:s a");
        //SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
        String strdate1 = sdf1.format(c1.getTime());

//        TimeZone tzInAmerica = TimeZone.getTimeZone("America/New_York");
//        sdf1.setTimeZone(tzInAmerica);
//        String strdate1 = sdf1.format(c1.getTime());
        return strdate1;


    }


    public String getCurrentDate() {
        //SimpleDateFormat contoh1 = new SimpleDateFormat("yyyy/MM/dd");
        //String hariotomatis = contoh1.format(c.getTime());
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat contoh1 = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
//        int year, month, day;
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DATE);
        //SimpleDateFormat contoh1 = new SimpleDateFormat("EEEE, yyyy/MM/dd");

        String hariotomatis = contoh1.format(c.getTime());
        //hari.setText(hariotomatis);
        return hariotomatis;
        //return day +"/" + (month+1) + "/" + year;
        //return (month+1) +"/" + day + "/" + year;
        //return year + "/" + (month + 1) + "/" + day;

    }

    public String jamotomatis(){
        Calendar c1 = Calendar.getInstance();
        //SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy h:m:s a");
        //SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        String strdate1 = sdf1.format(c1.getTime());
        return strdate1;


    }



//    private void getLastLocation(){
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//
//
//            fusedLocationProviderClient.getLastLocation()
//                    .addOnSuccessListener(new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//
//                            if (location != null){
//
//                                try {
//                                    Geocoder geocoder = new Geocoder(Absen_fix.this, Locale.getDefault());
//                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                                    latitude.setText(""+addresses.get(0).getLatitude());
//                                    longitude.setText(""+addresses.get(0).getLongitude());
//                                    lokasilengkap.setText(""+addresses.get(0).getAddressLine(0));
//                                    kecamatan.setText(""+addresses.get(0).getLocality());
////                                    country.setText("Country: "+addresses.get(0).getCountryName());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//
//                        }
//                    });
//
//
//        }else {
//
//            askPermission();
//
//
//        }
//
//
//    }
//
//    private void askPermission() {
//
//        ActivityCompat.requestPermissions(Absen_fix.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
//
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
//
//        if (requestCode == REQUEST_CODE){
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//
//
//                getLastLocation();
//
//            }else {
//
//
//                Toast.makeText(Absen_fix.this,"Please provide the required permission",Toast.LENGTH_SHORT).show();
//
//            }
//
//
//
//        }
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }



//    private void save() {
//        //pDialog.setMessage("Login Process...");
//        //showDialog();
//        AndroidNetworking.post(Config.host + "inputabsen.php")
//                .addBodyParameter("namasales",  namasales.getText().toString())
//                .addBodyParameter("jam", jam.getText().toString())
//                .addBodyParameter("lokasilengkap", lokasilengkap.getText().toString())
//                .addBodyParameter("longitude", longitude.getText().toString())
//                .addBodyParameter("latitude", latitude.getText().toString())
//                .addBodyParameter("tanggal", tanggal.getText().toString())
//                .addBodyParameter("kecamatan", kecamatan.getText().toString())
//                .addBodyParameter("keterangan", keterangan.getText().toString())
//                .addBodyParameter("status", status.getText().toString())
//                .addBodyParameter("absen", absen.getText().toString())
//                .addBodyParameter("posisi", posisi.getText().toString())
//
//
//
//                .setPriority(Priority.MEDIUM)
//                .build()
//
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//
//                        Log.d("response", response.toString());
//
//                        if (response.optString("response").toString().equals("success")) {
//                            //hideDialog();
//                            //gotoCourseActivity();
//                            Toast.makeText(getApplicationContext(), "Absen Berhasil, Data Anda sedang diteruskan ke Manager Anda, untuk dilakukan verifikasi terlebih dahulu",
//                                    Toast.LENGTH_LONG).show();
//
//
//
//
////                            ok1.setVisibility(View.VISIBLE);
////                            tabsendatang1.setTextColor(getResources().getColor(R.color.hijau));
////                            tabsendatang1.setText("TELAH ABSEN");
//
//                        } else {
//                            //hideDialog();
//                            Toast.makeText(getApplicationContext(), "HARI INI KAMU SUDAH ABSEN",
//                                    Toast.LENGTH_LONG).show();
////                            ga1.setVisibility(View.VISIBLE);
////                            tabsendatang1.setTextColor(getResources().getColor(R.color.merah));
////                            tabsendatang1.setText("GAGAL ABSEN");
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });
//    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


//    private void in() {
//
//        AndroidNetworking.post(Config.host + "cekabsenin.php")
//                .addBodyParameter("namasales", namasales.getText().toString())
//                .addBodyParameter("tanggal", tanggal.getText().toString())
//                //.addBodyParameter("bulan1", bulan1.getText().toString())
//                //.addBodyParameter("bulan2", bulan2.getText().toString())
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//
//
//                        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
//                        //username1.setText((response.optString("username")));
//                        in.setText((response.optString("jam")));
//                        status.setText((response.optString("status")));
//                        //nama221.setText((response.optString("nama")));
//                        //hideDialog();
//
//                        if (in.getText().toString().equals("null")) {
//                            //1
//                            in.setText("--:--");
//                            status.setText("PENDING");
//
//
//                            //Toast.makeText(getApplicationContext(), "Silahkan pilih terlebih dahulu", Toast.LENGTH_LONG).show();
//                        }else if (status.getText().toString().equals("null")) {
//                            //1
//                            status.setText("PENDING");
//
//
//
//                            //Toast.makeText(getApplicationContext(), "Silahkan pilih terlebih dahulu", Toast.LENGTH_LONG).show();
//                        } else {
//                            btnabsen.setText("ABSEN PULANG");
//                            btnabsen.setBackground(getResources().getDrawable(R.drawable.background3));;
//                            mainLayout.setBackground(getResources().getDrawable(R.drawable.bulat_merah));
//                            draggableButton.setBackground(getResources().getDrawable(R.drawable.drag_me_merah));
//                            teks_absen.setText("PULANG");
//                            absen.setText("PULANG");
//                            status.setText("APPROVE");
//
//
//                        }
//
//                    }
//
//
//                    @Override
//                    public void onError(ANError error) {
//
//                    }
//
//
//                });
//
//    }
//    private void out() {
//
//        AndroidNetworking.post(Config.host + "cekabsenout.php")
//                .addBodyParameter("namasales", namasales.getText().toString())
//                .addBodyParameter("tanggal", tanggal.getText().toString())
//                //.addBodyParameter("bulan1", bulan1.getText().toString())
//                //.addBodyParameter("bulan2", bulan2.getText().toString())
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//
//
//
//                        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
//                        //username1.setText((response.optString("username")));
//                        out.setText((response.optString("jam")));
//                        //nama221.setText((response.optString("nama")));
//                        //hideDialog();
//
//                        if ( out.getText().toString().equals("null")){
//                            //1
//                            out.setText("--:--");
//
//                            //Toast.makeText(getApplicationContext(), "Silahkan pilih terlebih dahulu", Toast.LENGTH_LONG).show();
//                        }else{
////                                btnabsen.setText("ABSEN PULANG");
////                                absen.setText("pulang");
//
//                        }
//
//                    }
//
//
//
//
//                    @Override
//                    public void onError(ANError error) {
//
//                    }
//
//
//
//                });
//
//
//
//
//
//    }


}