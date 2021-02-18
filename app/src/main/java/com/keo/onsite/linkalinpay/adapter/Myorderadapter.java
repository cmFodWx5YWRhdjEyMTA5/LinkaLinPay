package com.keo.onsite.linkalinpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.OrderDetailActivity_seller;
import com.keo.onsite.linkalinpay.activity.OrderdetailActivity_cust;
import com.keo.onsite.linkalinpay.activity.TrackOrderActivity;
import com.keo.onsite.linkalinpay.activity.model.Myordermodelclass;
import com.keo.onsite.linkalinpay.fragement.ProductListFragment;
import com.keo.onsite.linkalinpay.fragement.TrackOrderFragment;
import com.keo.onsite.linkalinpay.utils.Translator;

import org.apache.http.entity.mime.MultipartEntity;

import java.util.ArrayList;

public class Myorderadapter extends RecyclerView.Adapter<Myorderadapter.MyViewHolder> {

    private ArrayList<Myordermodelclass> dataSet;
    Context ctx;
    private int layoutId;
    String Imageview;
    TextView addcart_tv;
    private MultipartEntity reqEntity;

    Translator t ;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView vieworder,trackorder;
        TextView status,  orderid, totalamnt,date;
        //cat_name,

        public MyViewHolder(View itemView) {
            super(itemView);
           // productimage = (ImageView) itemView.findViewById(R.id.product_img);
            //cat_name = (TextView) itemView.findViewById(R.id.category);
            status = (TextView) itemView.findViewById(R.id.status);
            orderid = (TextView) itemView.findViewById(R.id.orderid);
            totalamnt = (TextView) itemView.findViewById(R.id.totalamnt);
            date=(TextView)itemView.findViewById(R.id.date);
            vieworder=(ImageView)itemView.findViewById(R.id.vieworder);
            trackorder=(ImageView)itemView.findViewById(R.id.trackorder);

        }
    }

    public Myorderadapter(Context ctx, ArrayList<Myordermodelclass> data) {
        this.ctx = ctx;
        this.layoutId = layoutId;
        this.dataSet = data;
        t =  new Translator();
        t.getTranslateService(ctx);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_myorder, parent, false);



        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

        @Override
    public void onBindViewHolder(MyViewHolder holder, final int listPosition) {



         holder.totalamnt.setText(dataSet.get(listPosition).currency+" "+dataSet.get(listPosition).total_amount);
        holder.status.setText(t.translate(ctx,dataSet.get(listPosition).orderstatus));
         holder.orderid.setText(dataSet.get(listPosition).order_id);
        holder.date.setText(dataSet.get(listPosition).order_date);
          holder.vieworder.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(ctx, OrderdetailActivity_cust.class);
               i.putExtra("orderid",dataSet.get(listPosition).order_id);
               ctx.startActivity(i);
           }
       });

     holder.trackorder.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            /* TrackOrderFragment fragmentB=new TrackOrderFragment();
             FragmentManager fragmentManager = ((AppCompatActivity)ctx).getSupportFragmentManager();
             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
             fragmentTransaction.replace(R.id.frame, fragmentB);
             fragmentTransaction.commit();
             Bundle bundle=new Bundle();
             bundle.putString("orderid","1505");


             fragmentB.setArguments(bundle);
*/
         Intent i=new Intent(ctx, TrackOrderActivity.class);
         i.putExtra("orderid",dataSet.get(listPosition).order_id);
         ctx.startActivity(i);

         }
     });

        //Imageview = dataSet.get(listPosition).product_image;




        /*if (!Imageview.equals("")) {
            try {
                //String apiLink = Link+Imgview;

                String apiLink = "Imageview";

                String encodedurl = "";
                encodedurl = apiLink.substring(0, apiLink.lastIndexOf('/')) + "/" + Uri.encode(apiLink.substring(
                        apiLink.lastIndexOf('/') + 1));
                Log.d("Social List adapter", "encodedurl:" + encodedurl);
                if (!apiLink.equals("") && apiLink != null) {
                    Picasso.with(ctx)
                            .load(encodedurl) // load: This path may be a remote URL,
                            //.placeholder(R.drawable.no_image)
                            //.error(R.drawable.no_image)
                            //.resize(130, 130)
                            .into(holder.productimage); // Into: ImageView into which the final image has to be passed

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }



    private void showToastLong(String msg) {
        Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();

    }












}
