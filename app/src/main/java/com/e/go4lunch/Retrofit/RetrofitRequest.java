package com.e.go4lunch.Retrofit;

import com.e.go4lunch.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static ApiRequest mApiRequest = retrofit.create(ApiRequest.class);

    public static ApiRequest getApiRequest(){
        return mApiRequest;
    }


}
