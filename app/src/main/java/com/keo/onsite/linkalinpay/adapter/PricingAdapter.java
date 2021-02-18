package com.keo.onsite.linkalinpay.adapter;//


// Created by Rajat Saha on 05-12-2020.
// Copyright (c) 2020 HTSM. All rights reserved.
//

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Orderlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Pricing;
import com.keo.onsite.linkalinpay.activity.other.Choice;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;

import java.util.List;

public class PricingAdapter extends RecyclerView.Adapter<PricingAdapter.ViewHolder>  {

    UserShared psh;
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<Pricing> pricingList;

    public  Pricing updatedList;
    private Choice choiceInterface;



    public void setViewItemInterface(Choice choiceInterface) {
        this.choiceInterface = choiceInterface;
    }


    public PricingAdapter(Context mCtx, List<Pricing> pricingList) {
        this.mCtx = mCtx;
        this.pricingList = pricingList;
    }





    @NonNull
    @Override
    public PricingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_purchase, null);
        return new PricingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PricingAdapter.ViewHolder holder, final int position) {

        //binding the data with the viewholder views
        psh=new UserShared(mCtx);


        holder.price_name.setText(pricingList.get(position).getName());
        holder.price_percentage.setText("( "+pricingList.get(position).getCharge()+" )");

        Log.d("Images>>>",pricingList.get(position).getImage());
        if(pricingList.get(position).getPay().equals("cust_pay"))
        {

            holder.radio_one.setChecked(true);
            holder.radio_two.setChecked(false);

//            holder.radio_one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                    holder.radio_one.setChecked(false);
////                    holder.radio_two.setChecked(true);
//                   // updatedList = pricingList.get(position);
//
//                    if (choiceInterface != null) {
//                        choiceInterface.onItemClick(holder.getAdapterPosition(),"cust_pay");
//                    }
//
//                }
//            });
//
//            holder.radio_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                    holder.radio_two.setChecked(false);
////                    holder.radio_one.setChecked(true);
//                    if (choiceInterface != null) {
//                        choiceInterface.onItemClick(holder.getAdapterPosition(),"i_pay");
//                    }
//                }
//            });


        }
        else {
            holder.radio_one.setChecked(false);
            holder.radio_two.setChecked(true);


        }

//        holder.radio_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                    holder.radio_one.setChecked(false);
////                    holder.radio_two.setChecked(true);
//
//                    choiceInterface.onItemClick(holder.getAdapterPosition(),"i_pay");
//
//            }
//        });

        holder.radio_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceInterface.onItemClick(holder.getAdapterPosition(),"i_pay");

            }
        });

        holder.radio_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceInterface.onItemClick(holder.getAdapterPosition(),"cust_pay");

            }
        });


//        holder.radio_one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                    holder.radio_two.setChecked(false);
////                    holder.radio_one.setChecked(true);
//
//                    choiceInterface.onItemClick(holder.getAdapterPosition(),"cust_pay");
//
//            }
//        });

        Glide.with(mCtx)
                .load(pricingList.get(position).getImage())
                .error(R.drawable.user)
                .into(holder.price_img);






    }

    @Override
    public int getItemCount() {
        Log.d("response>>",pricingList.size()+"");

        return pricingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView price_name, price_percentage;
        RadioButton radio_one, radio_two;
        ImageView price_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            price_img = itemView.findViewById(R.id.img);
            price_name = itemView.findViewById(R.id.name);
            price_percentage = itemView.findViewById(R.id.percent);
            radio_one = itemView.findViewById(R.id.item1);
            radio_two = itemView.findViewById(R.id.item2);


        }
    }

}
