package com.gadware.mvga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gadware.mvga.databinding.ReviewCardBinding;
import com.gadware.mvga.databinding.ServiceCardBinding;
import com.gadware.mvga.models.ReviewInfoModel;
import com.gadware.mvga.models.ServiceInfo;

import java.util.List;


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private final Context context;
    private final List<ServiceInfo> modelList;
     BookInterface bookInterface;
    public ServiceAdapter(Context context, List<ServiceInfo> modelList,BookInterface bookInterface) {
        this.context = context;
        this.modelList = modelList;
        this.bookInterface = bookInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServiceCardBinding binding = ServiceCardBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceInfo model = modelList.get(position);

        holder.binding.descTv.setText(model.getServName());
        holder.binding.nameTv.setText(model.getDescription());


        holder.binding.bookBtn.setOnClickListener(v -> {
            bookInterface.BookService(model.getServiceId(),model.getServName(),model.getDescription());
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
        ServiceCardBinding binding;

        public ViewHolder(@NonNull ServiceCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface BookInterface{
        void BookService(long serviceId,String serv,String desc);
    }
}