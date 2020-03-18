package com.e.go4lunch;

import com.e.go4lunch.models.MyPlace;
import com.e.go4lunch.models.Results;
import com.e.go4lunch.remote.IGoogleAPIService;
import com.e.go4lunch.remote.RetrofitClient;
import com.google.android.gms.common.api.Result;

public class Common {

    public static Results currentResult;

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";

    public static IGoogleAPIService getGoogleAPIService(){
      return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }

}
