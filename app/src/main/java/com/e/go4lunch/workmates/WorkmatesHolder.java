package com.e.go4lunch.workmates;

import android.graphics.Color;
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

public class WorkmatesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // ----------------- FOR DESIGN -----------------
    @BindView(R.id.item_list_avatar)
    ImageView mImageView;
    @BindView(R.id.item_list_text)
    TextView mTextView;

    private WorkmatesAdapter.OnNoteListener OnNoteListener;

    WorkmatesHolder(@NonNull View itemView, WorkmatesAdapter.OnNoteListener onNoteListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.OnNoteListener = onNoteListener;
        itemView.setOnClickListener(this);

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
        getNameOfRestaurant(workmates);


    }

    private void getNameOfRestaurant(Workmates workmates) {
        if (workmates.getRestaurantChosen() != null) {
            String name = workmates.getRestaurantChosen().getName();
            String text = (workmates.getWorkmateName() + " " + itemView.getContext().getResources().getString(R.string.is_eating) + " " + name);
            mTextView.setText(text);
            mTextView.setTextColor(Color.parseColor("#0A0A0A"));
        } else {
            String text1 = (workmates.getWorkmateName() + " " + itemView.getContext().getResources().getString(R.string.no_choice));
            mTextView.setText(text1);
        }

    }


    @Override
    public void onClick(View v) {
        OnNoteListener.onItemClick(getAdapterPosition());
    }

}
