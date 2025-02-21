package com.example.muzamil.helper;

import com.example.muzamil.helper.Config;

import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit _retrofit_ = null;

    public static Retrofit getClient() {
        if (_retrofit_ == null) {
            try {
                // Membuat TrustManager yang mengizinkan semua sertifikat
                TrustManager[] trustAllCertificates = new TrustManager[]{
                        new X509TrustManager() {
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[]{}; }
                        }
                };

                // Inisialisasi SSLContext
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

                // Inisialisasi OkHttpClient dengan SSL Socket Factory yang baru dibuat
                OkHttpClient client = new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCertificates[0])
                        .hostnameVerifier((hostname, session) -> true) // Mengizinkan hostname apapun
                        .build();

                // Setup Retrofit dengan OkHttpClient yang dikonfigurasi
                _retrofit_ = new Retrofit.Builder()
                        .baseUrl(Config.host) // URL dasar dari konfigurasi
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return _retrofit_;
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}