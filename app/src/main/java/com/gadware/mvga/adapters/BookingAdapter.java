package com.gadware.mvga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gadware.mvga.databinding.BookingCardBinding;
import com.gadware.mvga.models.BookingInfoModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;


public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private final Context context;
    private final List<BookingInfoModel> modelList;
     BookInterface bookInterface;
    public BookingAdapter(Context context, List<BookingInfoModel> modelList, BookInterface bookInterface) {
        this.context = context;
        this.modelList = modelList;
        this.bookInterface = bookInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookingCardBinding binding = BookingCardBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingInfoModel model = modelList.get(position);

        holder.binding.descTv.setText(model.getDescription());
        holder.binding.trainerTv.setText("Trainer: "+model.getTrainerName());
        holder.binding.servTv.setText("Service: "+model.getServName());
        holder.binding.startTimeTv.setText("S. Time"+new SimpleDateFormat("hh:mm a").format(model.getSTime()));
        holder.binding.startTimeTv.setText(String.valueOf(new Date(model.getSTime())));
        holder.binding.endTimeTv.setText("Duration: "+model.getDuration());


        holder.binding.bookBtn.setOnClickListener(v -> {
            bookInterface.RemoveBooking(model.getBookingId(),model.getTrainerId());
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
        BookingCardBinding binding;

        public ViewHolder(@NonNull BookingCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface BookInterface{
        void RemoveBooking(long bookingId,long trainerId);
    }
}