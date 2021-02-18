package com.keo.onsite.linkalinpay.adapter;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

import com.keo.onsite.linkalinpay.R;

import com.keo.onsite.linkalinpay.activity.model.Sellerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Vendorlistmodelclass;
import com.keo.onsite.linkalinpay.fragement.ProductListFragment;
import com.keo.onsite.linkalinpay.utils.Translator;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Vendorlistadapter extends RecyclerView.Adapter<Vendorlistadapter.ViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;
    private List<Vendorlistmodelclass> productList;
    Translator t;

    onItemClickListner onItemClickListner;
    public Vendorlistadapter(Context mCtx, List<Vendorlistmodelclass> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        t =  new Translator();
        t.getTranslateService(mCtx);

    }

    @Override
    public Vendorlistadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_sellerlist, null);
        return new Vendorlistadapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(Vendorlistadapter.ViewHolder holder, final int position) {

        //binding the data with the viewholder views
        holder.pNametxt.setText(t.translate(mCtx,productList.get(position).seller_name));
        holder.quantity.setText(t.translate(mCtx,productList.get(position).totalproduct));


        String image=productList.get(position).seller_logo;

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


      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              ProductListFragment fragmentB=new ProductListFragment();
              FragmentManager fragmentManager = ((AppCompatActivity)mCtx).getSupportFragmentManager();
              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
              fragmentTransaction.replace(R.id.frame, fragmentB);
              fragmentTransaction.commit();
              Bundle bundle=new Bundle();
              bundle.putString("seller_id",productList.get(position).seller_id);
              bundle.putString("category_id",productList.get(position).category_id);
              bundle.putString("sellernamr",productList.get(position).seller_name);


              fragmentB.setArguments(bundle);

              //onItemClickListner.onClick(data);


          }
      });


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        TextView pNametxt,quantity ;

        Button viewdetails;
        ImageView userImg;
        CardView cardview;
        public ViewHolder(View itemView) {
            super(itemView);

            pNametxt = itemView.findViewById(R.id.sellername);
            quantity=itemView.findViewById(R.id.totalproduct);
            userImg=(ImageView)itemView.findViewById(R.id.logo);
            cardview=(CardView)itemView.findViewById(R.id.cardview);


        }



    }

    public void setOnItemClickListner(Vendorlistadapter.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface onItemClickListner{
        void onClick(String str);//pass your object types.
    }



    public void filter(){

        if(productList.isEmpty())
        {
            notifyDataSetChanged();


        }
        else {

            productList.clear();
            notifyDataSetChanged();
        }




    }
















}
