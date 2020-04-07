package com.e.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.WorkmatesRepository;


import java.util.List;

public class ListWorkmateViewModel extends ViewModel {

    private MutableLiveData<List<Workmates>> mWorkmates;
    private WorkmatesRepository mWorkmatesRepository;

    public void init(){
        if(mWorkmates != null){
            return;
        }
        mWorkmatesRepository = WorkmatesRepository.getInstance();
        mWorkmates = mWorkmatesRepository.getWorkmates();
    }

    public LiveData<List<Workmates>> getWorkmates(){
        return mWorkmates;
    }
}
