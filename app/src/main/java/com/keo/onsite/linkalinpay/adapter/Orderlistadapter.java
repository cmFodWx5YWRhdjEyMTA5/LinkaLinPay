package com.keo.onsite.linkalinpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.OrderDetailActivity_seller;
import com.keo.onsite.linkalinpay.activity.model.Orderlistmodelclass;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.utils.Translator;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Orderlistadapter extends RecyclerView.Adapter<Orderlistadapter.ViewHolder> {

     UserShared psh;
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<Orderlistmodelclass> productList;
    Translator t;

    //getting the context and product list with constructor
    public Orderlistadapter(Context mCtx, List<Orderlistmodelclass> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        t =  new Translator();
        t.getTranslateService(mCtx);
    }

    @Override
    public Orderlistadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_orderlist, null);
        return new Orderlistadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Orderlistadapter.ViewHolder holder, final int position) {

        //binding the data with the viewholder views
         psh=new UserShared(mCtx);

        holder.orderid.setText(productList.get(position).order_id);
        holder.orderamount.setText(psh.getCurrencysymbol()+" "+productList.get(position).total_amount);
        holder.orderdate.setText(productList.get(position).order_date);
        holder.amount.setText(psh.getCurrencysymbol()+" "+productList.get(position).product_offerprice);
        holder.status.setText(t.translate(mCtx,productList.get(position).orderstatus));
        holder.pJobProfiletxt.setText(t.translate(mCtx,productList.get(position).product_name));
        String image=productList.get(position).product_image;
        holder.total.setText(psh.getCurrencysymbol()+" "+productList.get(position).total_amount);
       //holder.description.setText(productList.get(position).);

        if (!image.equals("")) {
            try {
                //String apiLink = context.getResources().getString(R.string.banner)+IMg;
                String apiLink = image;
                //Log.d("User Profile Image Parsing", "apiLink:"+apiLink);
                String encodedurl = "";
                encodedurl = apiLink.substring(0, apiLink.lastIndexOf('/')) + "/" + Uri.encode(apiLink.substring(
                        apiLink.lastIndexOf('/') + 1));
                Log.d("User", "encodedurl:" + encodedurl);
                if (!apiLink.equals("") && apiLink != null) {
                    Picasso.with(mCtx)
                            .load(encodedurl) // load: This path may be a remote URL,
                            .placeholder(R.drawable.defaultpic)
                            //.resize(130, 130)
                            .error(R.drawable.defaultpic)
                            .into(holder.userImg); // Into: ImageView into which the final image has to be passed
                    //  .resize(130, 130)
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        holder. viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mCtx, OrderDetailActivity_seller.class);
                i.putExtra("orderid",productList.get(position).order_id);
                mCtx.startActivity(i);
            }
       });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderid, orderamount, orderdate,amount,status,pJobProfiletxt,total,description;
        Button viewdetails;
        ImageView userImg;

        public ViewHolder(View itemView) {
            super(itemView);

            orderid = itemView.findViewById(R.id.orderid);
            orderamount = itemView.findViewById(R.id.amount);
            orderdate=itemView.findViewById(R.id.date);
            viewdetails=(Button)itemView.findViewById(R.id.viewdetails);
            userImg=(ImageView)itemView.findViewById(R.id.userImg);
            amount=(TextView)itemView.findViewById(R.id.amount);
            status=(TextView)itemView.findViewById(R.id.status);
            pJobProfiletxt=(TextView)itemView.findViewById(R.id.pname);
            total=(TextView)itemView.findViewById(R.id.total);
            description=(TextView)itemView.findViewById(R.id.description);

        }

}


}
