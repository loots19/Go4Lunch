package com.e.go4lunch;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmatesHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_list_avatar)
    ImageView mImageView;

    public WorkmatesHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
