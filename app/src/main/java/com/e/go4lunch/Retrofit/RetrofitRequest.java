package com.e.go4lunch.Retrofit;

import com.e.go4lunch.util.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {


    private static Retrofit retrofit = buildRetrofit().build();
    private static ApiRequest mApiRequest = retrofit.create(ApiRequest.class);

    public static ApiRequest getApiRequest() {
        return mApiRequest;
    }

    // Setting Up the Retrofit Interface
    private static Retrofit.Builder buildRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit.Builder retrofitBuilder =
                new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create());
        return retrofitBuilder;
    }


}
