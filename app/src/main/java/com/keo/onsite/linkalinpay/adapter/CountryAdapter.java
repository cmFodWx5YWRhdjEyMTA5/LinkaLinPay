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
import com.keo.onsite.linkalinpay.activity.model.CouponListmodelclass;
import com.keo.onsite.linkalinpay.utils.Translator;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<Countrymodelclass> {
    private Context ctx;
    private int layoutId;
    private ArrayList<Countrymodelclass> catDataList;
    Translator t ;
    String _Img;
    public CountryAdapter(Context ctx, ArrayList<Countrymodelclass> catDataList, int layoutId) {
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
    public Countrymodelclass getItem(int position) {
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

        label.setText(t.translate(ctx,this.getItem(position).name));


        /*if (position == 0) {
            label.setText("Please select Coupon");
        } else {
            label.setText(this.getItem(position).name);



        }

*/
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
        TextView title = (TextView)result.findViewById(R.id.cat_name);
        /*if (position == 0) {
            title.setText("Please select Coupon");
        } else {
            title.setText(this.getItem(position).name);

        }
*/


        title.setText(t.translate(ctx,catDataList.get(position).name));
        return result;

    }
















}
