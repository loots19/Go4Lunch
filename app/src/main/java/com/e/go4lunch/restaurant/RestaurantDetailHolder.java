package com.e.go4lunch.restaurant;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.R;
import com.e.go4lunch.models.Workmates;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RestaurantDetailHolder extends RecyclerView.ViewHolder {

    // ----------------- FOR DESIGN -----------------
    @BindView(R.id.item_list_avatar)
    ImageView mImageView;
    @BindView(R.id.item_list_text)
    TextView mTextView;

    RestaurantDetailHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void update(Workmates workmates) {
        if (workmates.getUrlPicture() != null) {
            Glide.with(itemView)
                    .load(workmates.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mImageView);
        } else {
            Glide.with(itemView)
                    .load(R.drawable.ic_people_black_24dp)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mImageView);

        }
        String text = (workmates.getWorkmateName() + " " + itemView.getContext().getResources().getString(R.string.is_joining));
        mTextView.setText(text);
    }
}