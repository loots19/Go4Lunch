package com.e.go4lunch.workmates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.go4lunch.R;
import com.e.go4lunch.models.Workmates;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesHolder> {

    // ----------------- FOR DATA -----------------
    private List<Workmates> mWorkmates;
    private OnNoteListener mOnNoteListener;


    WorkmatesAdapter(OnNoteListener onNoteListener) {
        this.mOnNoteListener = onNoteListener;
        this.mWorkmates = new ArrayList<>();

    }

    @NonNull
    @Override
    public WorkmatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates_fragment, parent, false);
        return new WorkmatesHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesHolder holder, int position) {
        holder.update(this.mWorkmates.get(position));


    }

    public void setWorkmates(List<Workmates> workmatesList) {
        mWorkmates = workmatesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mWorkmates != null) {
            return mWorkmates.size();
        }
        return 0;
    }

    public interface OnNoteListener {
        void onItemClick(int position);

    }


}
