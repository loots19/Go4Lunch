package com.e.go4lunch.workmates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.R;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.restaurant.RestaurantAdapter;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesHolder>  {


    Context mContext;
    private List<Workmates> mWorkmates;
    private OnNoteListener mOnNoteListener;
    private List<Result>mResults;



    public WorkmatesAdapter(Context context,OnNoteListener onNoteListener) {
        mContext = context;

        this.mOnNoteListener = onNoteListener;

    }


    @NonNull
    @Override
    public WorkmatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates_fragment,parent,false);
        return new WorkmatesHolder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesHolder holder, int position) {

        Workmates workmates = mWorkmates.get(position);
        Glide.with(holder.mImageView.getContext())
                .load(workmates.getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount()
    {
        return 10;
    }
    public interface OnNoteListener {
        void onNoteClick(int position);
    }

}