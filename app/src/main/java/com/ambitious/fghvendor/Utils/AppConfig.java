package com.ambitious.fghvendor.Utils;


import com.ambitious.fghvendor.Interface.LoadInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by user1 on 11/16/2017.
 */

public class AppConfig {
    public static int amountOfPercentage =2;
    private static Retrofit retrofit = null;
    private static LoadInterface loadInterface = null;


    private static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5000, TimeUnit.MINUTES)
                    .readTimeout(5000, TimeUnit.MINUTES).build();
//            retrofit = new Retrofit.Builder().baseUrl("http://webuddys.in/FGH/index.php/api/")
            retrofit = new Retrofit.Builder().baseUrl("https://fghdoctors.com/admin/index.php/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        }
        return retrofit;
    }

    public static LoadInterface loadInterface() {
        if (loadInterface == null) {
            loadInterface = AppConfig.getClient().create(LoadInterface.class);
        }
        return loadInterface;
    }


}
