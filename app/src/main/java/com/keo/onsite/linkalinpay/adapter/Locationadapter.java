package com.keo.onsite.linkalinpay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Categorymodelclass;
import com.keo.onsite.linkalinpay.activity.model.Locationmodelclass;

import java.util.ArrayList;

public class Locationadapter extends ArrayAdapter<Locationmodelclass> {

    private Context ctx;
    private int layoutId;
    private ArrayList<Locationmodelclass> catDataList;

    String _Img;
    public Locationadapter(Context ctx, ArrayList<Locationmodelclass> catDataList, int layoutId) {
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
    public Locationmodelclass getItem(int position) {
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
        label.setTextColor(Color.parseColor("#34495F"));
        label.setText(this.getItem(position).location);

        if (position == -1) {
            label.setText("Select Location");
        } else {
            label.setText(this.getItem(position).location);;

        }



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
            title.setText("Select Location");
        } else {
            title.setText(catDataList.get(position).location);

        }



        return result;

    }

















}
