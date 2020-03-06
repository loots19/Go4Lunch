package com.e.go4lunch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.models.Workmates;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesHolder> {


    Context mContext;
    private List<Workmates> mWorkmates;


    public WorkmatesAdapter(Context context, List<Workmates> workmates) {
        mContext = context;
        mWorkmates = workmates;
    }


    @NonNull
    @Override
    public WorkmatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates_fragment,parent,false);
        return new WorkmatesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesHolder holder, int position) {

        Workmates workmates = mWorkmates.get(position);
        Glide.with(holder.mImageView.getContext())
                .load(workmates.getUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mWorkmates.size();
    }
}
