package com.keo.onsite.linkalinpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.QuickInvoiceviewActivity;
import com.keo.onsite.linkalinpay.activity.model.Invoicelist;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.utils.Translator;

import java.util.List;

public class Invlistadapter extends RecyclerView.Adapter<Invlistadapter.ViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<Invoicelist> productList;
    //getting the context and product list with constructor
      UserShared psh;
    Translator t ;

    public Invlistadapter(Context mCtx, List<Invoicelist> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        t =  new Translator();
        t.getTranslateService(mCtx);
    }

    @Override
    public Invlistadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_invoice, null);
        return new Invlistadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Invlistadapter.ViewHolder holder, final int position) {
       psh=new UserShared(mCtx);
        //binding the data with the viewholder views
        //holder.invoiceid.setText("#"+productList.get(position).inv_id);
        holder.refrencenumber.setText(productList.get(position).inv_refno);
        //holder.name.setText(productList.get(position).inv_name);
        holder.date.setText(productList.get(position).inv_date);
        holder.amount.setText(psh.getCurrencysymbol()+" "+productList.get(position).inv_amount);
       holder.view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(mCtx, QuickInvoiceviewActivity.class);
               i.putExtra("invid",productList.get(position).inv_id);
               i.putExtra("orderdate",productList.get(position).inv_date);
               i.putExtra("tatalamnt",productList.get(position).inv_amount);
               i.putExtra("ordernumber",productList.get(position).inv_refno);
               i.putExtra("orderstatus",t.translate(mCtx,productList.get(position).inv_status));
               i.putExtra("personname",productList.get(position).inv_name);
               i.putExtra("phonenumber",productList.get(position).inv_mobile);
               i.putExtra("email",productList.get(position).inv_email);
                i.putExtra("totrecords",productList.get(position).totalrecords);
                i.putExtra("amnttype",productList.get(position).inv_amount_type);

               mCtx.startActivity(i);
           }
       });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView invoiceid, refrencenumber, name,date,amount;
        ImageView view;

        public ViewHolder(View itemView) {
            super(itemView);

            //invoiceid = itemView.findViewById(R.id.invoiceid);
            refrencenumber = itemView.findViewById(R.id.item_number);
            //name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            amount=itemView.findViewById(R.id.amount);
            view=itemView.findViewById(R.id.view);


        }

    }




}
