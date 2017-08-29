package com.myoffersapp.retrofit;

import com.myoffersapp.helper.AllKeys;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.fabric.sdk.android.services.common.AbstractSpiCall.DEFAULT_TIMEOUT;


public class ApiClient {

    public static final String BASE_URL = AllKeys.WEBSITE;
    private static Retrofit retrofit = null;




    public static Retrofit getClient() {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();


        }
        return retrofit;
    }
}
