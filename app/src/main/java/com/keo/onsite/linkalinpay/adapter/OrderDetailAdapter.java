package com.keo.onsite.linkalinpay.adapter;

import android.content.Context;
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
import com.keo.onsite.linkalinpay.activity.model.Sellerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.utils.Translator;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<Sellerlistmodelclass> productList;
    UserShared psh;

    Translator t ;
    //getting the context and product list with constructor
    public OrderDetailAdapter(Context mCtx, List<Sellerlistmodelclass> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        t =  new Translator();
        t.getTranslateService(mCtx);
    }

    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_product_detail, null);
        return new OrderDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderDetailAdapter.ViewHolder holder, final int position) {
       psh=new UserShared(mCtx);
        //binding the data with the viewholder views
        holder.pNametxt.setText(t.translate(mCtx,productList.get(position).product_name));
        holder.quantity.setText(productList.get(position).qty);
        holder.amount.setText(psh.getCurrencysymbol()+" "+productList.get(position).product_offerprice);

        String image=productList.get(position).product_image;

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





    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView pNametxt, date, amount,total,orderid;
        Button viewdetails;
        ImageView userImg;
        TextView quantity;
        public ViewHolder(View itemView) {
            super(itemView);

            pNametxt = itemView.findViewById(R.id.pNametxt);
            quantity=itemView.findViewById(R.id.quantity);
            userImg=(ImageView)itemView.findViewById(R.id.userImg);
            amount=(TextView)itemView.findViewById(R.id.amount);

        }

    }



}
