package com.e.go4lunch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.e.go4lunch.R;
import com.e.go4lunch.models.myPlace.MyPlace;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.models.placedetail.Photo;
import com.e.go4lunch.models.placedetail.PlaceDetail;
import com.e.go4lunch.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantHolder> {

    private Context mContext;
    private OnNoteListener mOnNoteListener;
    private List<Result>mResults;





    public RestaurantAdapter(Context context, OnNoteListener onNoteListener) {
        this.mContext = context;
        this.mOnNoteListener = onNoteListener;

        mResults = new ArrayList<>();



    }

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_restaurant,parent,false);
        return new RestaurantHolder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHolder holder, int position) {
        //String url = "https://maps.googleapis.com/maps/api/place/photo" +
              //  "?maxwidth=400" +
             //   "&photoreference= +
            //    "&key=AIzaSyCbD-Ektsu_fCIS7YIU0G5BWic30ZXpDiA";

       // Glide.with(mContext)
        //        .load(url)
        //        .into(holder.mImageRestaurant);

       Glide.with(mContext)
              .load( "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+Constants.API_KEY)
              .into(holder.mImageRestaurant);
       holder.mTvName.setText(mResults.get(position).getName());
       holder.mTvAdress.setText(mResults.get(position).getVicinity());


    }

    @Override
    public int getItemCount() {
        if (mResults != null){
            return mResults.size();
    }
    return 0;
    }

    public interface OnNoteListener{
        void onNoteClick (int position);
    }
    public void setRestaurants (List<Result> results){
        mResults = results;
        notifyDataSetChanged();
    }


}
