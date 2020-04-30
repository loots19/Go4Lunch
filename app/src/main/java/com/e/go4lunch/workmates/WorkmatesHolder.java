package com.e.go4lunch.workmates;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;
import com.e.go4lunch.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmatesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.item_list_avatar)
    ImageView mImageView;

    WorkmatesAdapter.OnNoteListener OnNoteListener;

    public WorkmatesHolder(@NonNull View itemView, WorkmatesAdapter.OnNoteListener onNoteListener) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.OnNoteListener = onNoteListener;
        itemView.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        OnNoteListener.onNoteClick(getAdapterPosition());
    }
}
