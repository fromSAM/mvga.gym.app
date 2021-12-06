package com.gadware.mvga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gadware.mvga.databinding.ReviewCardBinding;
import com.gadware.mvga.models.ReviewInfo;
import com.gadware.mvga.models.ReviewInfoModel;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final Context context;
    private final List<ReviewInfoModel> modelList;

    public ReviewAdapter(Context context, List<ReviewInfoModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReviewCardBinding binding = ReviewCardBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewInfoModel model = modelList.get(position);

        holder.binding.dateTv.setText(model.getDate());
        holder.binding.guestNameTv.setText(model.getUserName());
        holder.binding.reviewTv.setText(model.getReview());

        holder.binding.ratingBar.setRating((float) model.getRating());

    }



    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ReviewCardBinding binding;

        public ViewHolder(@NonNull ReviewCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}