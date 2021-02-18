package com.keo.onsite.linkalinpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.CustomerInfo;
import com.keo.onsite.linkalinpay.activity.QuickInvoiceviewActivity;
import com.keo.onsite.linkalinpay.activity.model.Customermodel;
import com.keo.onsite.linkalinpay.activity.model.Invoicelist;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.utils.Translator;

import java.util.List;

public class custlistadapter extends RecyclerView.Adapter<custlistadapter.ViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<Customermodel> CustomerList;
    //getting the context and product list with constructor
      UserShared psh;
    Translator t ;

    public custlistadapter(Context mCtx, List<Customermodel> CustomerList) {
        this.mCtx = mCtx;
        this.CustomerList = CustomerList;
        t =  new Translator();
        t.getTranslateService(mCtx);
    }

    @Override
    public custlistadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_cust, null);
        return new custlistadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(custlistadapter.ViewHolder holder, final int position) {
       psh=new UserShared(mCtx);
        //binding the data with the viewholder views
        //holder.invoiceid.setText("#"+CustomerList.get(position).inv_id);
        holder.name.setText(CustomerList.get(position).getCust_name());
        //holder.name.setText(CustomerList.get(position).inv_name);
        holder.email.setText(CustomerList.get(position).getCust_email());
        holder.mobile.setText(CustomerList.get(position).getCust_mobile());

        Glide.with(mCtx)
                .load(CustomerList.get(position).getCust_profile())
                .into(holder.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(mCtx, CustomerInfo.class);
               i.putExtra("custid",CustomerList.get(position).getCust_id());
               mCtx.startActivity(i);
           }
       });

    }


    @Override
    public int getItemCount() {
        return CustomerList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, mobile, email;
        ImageView profile;

        public ViewHolder(View itemView) {
            super(itemView);

            //invoiceid = itemView.findViewById(R.id.invoiceid);
            name = itemView.findViewById(R.id.name);
            //name=itemView.findViewById(R.id.name);
            mobile=itemView.findViewById(R.id.mobile);
            email=itemView.findViewById(R.id.email);
            profile =itemView.findViewById(R.id.profile);


        }

    }




}
