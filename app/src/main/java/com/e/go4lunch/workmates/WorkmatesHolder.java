package com.e.go4lunch.workmates;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.e.go4lunch.R;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmatesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.item_list_avatar)
    ImageView mImageView;
    @BindView(R.id.item_list_text)
    TextView mTextView;

    private WorkmatesAdapter.OnNoteListener OnNoteListener;

    public WorkmatesHolder(@NonNull View itemView, WorkmatesAdapter.OnNoteListener onNoteListener) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.OnNoteListener = onNoteListener;
        itemView.setOnClickListener(this);

    }
    public void update(Workmates workmates){
        Glide.with(itemView)
                .load(workmates.getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(mImageView);
        String text = (workmates.getWorkmateName()+"is eating");
        mTextView.setText(text);

    }




    @Override
    public void onClick(View v) {
        OnNoteListener.onNoteClick(getAdapterPosition());
    }
}
