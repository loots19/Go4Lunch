package com.e.go4lunch.util;

import androidx.lifecycle.LiveData;


//AbsentLiveData is a LiveData class that has a ‘null’ value. This can be used as a fallback mechanism if any function fails to return a valid LiveData object.

public class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }

    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}
