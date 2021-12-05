package com.gadware.mvga.adapters;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gadware.mvga.databinding.TrainerCardBinding;
import com.gadware.mvga.models.TrainerInfo;
import com.gadware.mvga.ui.activities.TrainerProfile;
import com.gadware.mvga.utils.ImageHelper;

import java.util.List;


public class TrainersAdapter extends RecyclerView.Adapter<TrainersAdapter.VH> {

    private Context context;
    private  List<TrainerInfo> modelList;



    public TrainersAdapter(Context context, List<TrainerInfo> modelList) {
        this.context = context;
        this.modelList = modelList;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TrainerCardBinding binding = TrainerCardBinding.inflate(LayoutInflater.from(context),parent,false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {

        final TrainerInfo model = modelList.get(position);


        holder.binding.nameTv.setText(model.getName());
        holder.binding.emailTv.setText(model.getEmail());
        holder.binding.expTv.setText(model.getExperience()+" years");

        try{
            holder.binding.ProfileIcon.setImageBitmap(SetImageResource(model.getImage()));
        } catch (Exception ignored) {
        }

        holder.binding.revBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, TrainerProfile.class);
            intent.putExtra("id", model.getTrainerId());
            context.startActivity(intent);
        });




    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Bitmap SetImageResource(byte[] bytes) {
        return ImageHelper.toBitmap(bytes);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }




    public static class VH extends RecyclerView.ViewHolder{
        TrainerCardBinding binding;
        public VH(@NonNull TrainerCardBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }


    }
}
