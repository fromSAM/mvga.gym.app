package com.gadware.mvga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gadware.mvga.R;
import com.gadware.mvga.models.PackageInfo;


import java.util.ArrayList;
import java.util.List;

public class SubscriptionAdapter extends ArrayAdapter<PackageInfo> {

    List<PackageInfo> itemsFull;

    public SubscriptionAdapter(Context context, List<PackageInfo> items) {
        super(context, 0, items);
        itemsFull = new ArrayList<>(items);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.subs_list_card, parent, false
            );
        }

        PackageInfo model = itemsFull.get(position);
        if (model != null) {

            TextView name = convertView.findViewById(R.id.tv_sub_name);
            TextView price = convertView.findViewById(R.id.tv_charge);
            TextView disc = convertView.findViewById(R.id.tv_discount);


            if (name != null) {
                name.setText(model.getSubName());
                price.setText("Charge: "+model.getCharge());
                disc.setText("Discount: "+model.getDiscount());
            }
        }
        return convertView;
    }
}
