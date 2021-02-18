package com.keo.onsite.linkalinpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keo.onsite.linkalinpay.R;

import java.util.List;

public class Product_list_Adapter  extends RecyclerView.Adapter<Product_list_Adapter.ViewHolder> {

    Context context;
//    List<CompletedModel> completedModels;

//    public CompletedAdapter(Context context, List<CompletedModel> completedModels) {
//        this.context = context;
//        this.completedModels = completedModels;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.productlist_layout, viewGroup, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

//        if (completedModels.get(i).getTotal_Amount().isEmpty() || completedModels.get(i).getTotal_Amount().equalsIgnoreCase("0")){
//            viewHolder.orderamount.setText("-");
//        }else {
//            viewHolder.orderamount.setText(completedModels.get(i).getTotal_Amount() + " /-");
//        }
//
//
//        viewHolder.ord_no.setText("Order #" + completedModels.get(i).getOrder_No());
//        viewHolder.status.setText(" "+completedModels.get(i).getOrder_Status());
//        viewHolder.shipingdate.setText(completedModels.get(i).getDelivery_Date());
//        viewHolder.del_time.setText(completedModels.get(i).getDelivery_Time());
//
//        if (completedModels.get(i).getOrder_Status().equals("Delivered")) {
//            viewHolder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check1, 0, 0, 0);
//            viewHolder.status.setTextColor(Color.rgb(37, 255, 86));
//        } else {
//            viewHolder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.close, 0, 0, 0);
//            viewHolder.status.setTextColor(Color.rgb(255, 51, 0));
//        }
//        viewHolder.details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context.getApplicationContext(), CompletedOrderActivity.class);
//                intent.putExtra("master_id", completedModels.get(i).getMaster_Order_Id());
//                intent.putExtra("status", completedModels.get(i).getOrder_Status());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView ord_no, details, status, shipingdate, orderamount, del_time, ordid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            ord_no = itemView.findViewById(R.id.c_ord_no);
//            details = itemView.findViewById(R.id.c_details);
//            status = itemView.findViewById(R.id.c_status);
//            del_time = itemView.findViewById(R.id.c_shiping_time);
//            shipingdate = itemView.findViewById(R.id.c_shiping_date);
//            orderamount = itemView.findViewById(R.id.c_order_amount);


        }
    }
}
