package com.gadware.mvga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gadware.mvga.databinding.PkgListCardBinding;
import com.gadware.mvga.models.PackageInfo;

import java.util.List;


public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {

    private final Context context;
    private boolean admin;
    private final List<PackageInfo> modelList;
    EditInterface editInterface;

    public PackageAdapter(Context context,boolean admin, List<PackageInfo> modelList, EditInterface editInterface) {
        this.context = context;
        this.admin = admin;
        this.modelList = modelList;
        this.editInterface = editInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PkgListCardBinding binding = PkgListCardBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PackageInfo model = modelList.get(position);

        holder.binding.tvName.setText("Subscription Type: "+model.getSubName());
        holder.binding.tvCharge.setText("Charge($): "+model.getCharge());
        holder.binding.tvDiscount.setText("Discount($): "+model.getDiscount());


        if (!admin){
            holder.binding.editBtn.setVisibility(View.GONE);
        }

        holder.binding.editBtn.setOnClickListener(v -> {
            editInterface.EditPackageInfo(model);
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PkgListCardBinding binding;

        public ViewHolder(@NonNull PkgListCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface EditInterface {
        void EditPackageInfo(PackageInfo model);
    }
}