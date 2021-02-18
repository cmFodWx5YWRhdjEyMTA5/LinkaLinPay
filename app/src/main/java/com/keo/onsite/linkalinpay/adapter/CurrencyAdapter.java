package com.keo.onsite.linkalinpay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Countrymodelclass;
import com.keo.onsite.linkalinpay.activity.model.Currencymodelclass;

import java.util.ArrayList;

public class CurrencyAdapter extends ArrayAdapter<Currencymodelclass> {

    private Context ctx;
    private int layoutId;
    private ArrayList<Currencymodelclass> catDataList;


    String _Img;
    public CurrencyAdapter(Context ctx, ArrayList<Currencymodelclass> catDataList, int layoutId) {
        super(ctx,layoutId);
        this.ctx = ctx;
        this.catDataList = catDataList;
        this.layoutId = layoutId;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return catDataList.size() ;
    }

    @Override
    public Currencymodelclass getItem(int position) {
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



        if (position == -1) {
            label.setText("Please select Currency");
        } else {
            label.setText(this.getItem(position).currencyname);



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
        if (position == -1) {
            title.setText("Please select Coupon");
        } else {
            title.setText(catDataList.get(position).currencyname);

        }




        return result;

    }












}
