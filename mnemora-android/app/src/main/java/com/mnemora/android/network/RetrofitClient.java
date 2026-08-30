package com.mnemora.android.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// A simple singleton (single shared instance) that provides a configured
// Retrofit client and our API service, so we don't rebuild the connection
// every time we need to make a network call.
public class RetrofitClient {

    // IMPORTANT: 10.0.2.2 is a special alias that Android's EMULATOR uses
    // to reach "localhost" on your actual development machine.
    // If testing on a REAL physical device instead, replace this with
    // your computer's actual local network IP address (e.g. 192.168.1.5),
    // since the phone and computer need to be on the same Wi-Fi network.
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    private static Retrofit retrofit = null;

    // Returns a single shared Retrofit instance, creating it only once
    // (lazy initialization) - avoids rebuilding the HTTP client repeatedly
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    // Tells Retrofit to use Gson to convert JSON <-> Java objects
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Convenience method - gives us a ready-to-use MnemoraApiService instance
    public static MnemoraApiService getApiService() {
        return getClient().create(MnemoraApiService.class);
    }
}