package com.steve_md.smartmkulima.ui.fragments.directpayment.bottom_sheet_merchant_payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.steve_md.smartmkulima.R;
import com.steve_md.smartmkulima.utils.Options;

import java.util.ArrayList;


public class AdapterSpinnerPaymentsProviders extends ArrayAdapter<Options> {

    public AdapterSpinnerPaymentsProviders(Context context, ArrayList<Options> countryList) {
        super(context, 0, countryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.spinner_payment_options_merchant, parent, false
            );
        }

        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);

        Options currentItem = getItem(position);

        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getOptionImage());
            textViewName.setText(currentItem.getOptionString());
        }

        return convertView;
    }
}

