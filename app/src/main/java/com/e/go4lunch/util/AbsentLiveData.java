package com.e.go4lunch.util;

import androidx.lifecycle.LiveData;

public class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }
    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}
