package com.keo.onsite.linkalinpay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.CouponListmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Influencerlistmodelclass;
import com.keo.onsite.linkalinpay.utils.Translator;

import java.util.ArrayList;

public class Couponadapter extends ArrayAdapter<CouponListmodelclass> {
    /*public ListAdapter(Context context, int resource) {
                    super(context, resource);
                    // TODO Auto-generated constructor stub
                }*/
    // Declare Variables
    private Context ctx;
    private int layoutId;
    private ArrayList<CouponListmodelclass> catDataList;
    Translator t ;

    String _Img;
    public Couponadapter(Context ctx, ArrayList<CouponListmodelclass> catDataList, int layoutId) {
        super(ctx,layoutId);
        this.ctx = ctx;
        this.catDataList = catDataList;
        this.layoutId = layoutId;

        t =  new Translator();
        t.getTranslateService(ctx);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return catDataList.size() ;
    }

    @Override
    public CouponListmodelclass getItem(int position) {
        // TODO Auto-generated method stub
        return catDataList.get(position);
    }

	/*@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}*/


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return super.getDropDownView(position, convertView, parent);


        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.GRAY);

        if (position == 0) {
            label.setText(t.translate(ctx,"Please select Coupon"));
        } else {
            label.setText(t.translate(ctx,this.getItem(position).name));



        }


        //label.setText(this.getItem(position).email);
        return label;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View result = convertView;

        if (result == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            result = inflater.inflate(layoutId, parent, false);
        }
        TextView title=(TextView)result.findViewById(R.id.cat_name);
        if (position == 0) {
            title.setText(t.translate(ctx,"Please select Coupon"));
        } else {
            title.setText(t.translate(ctx,this.getItem(position).name));

        }



        //title.setText(catDataList.get(position).email);
        return result;

    }

}
