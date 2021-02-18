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
import com.keo.onsite.linkalinpay.utils.Translator;

import java.util.ArrayList;

public class SellerCategoryadapter extends ArrayAdapter<Categorymodelclass> {
/*public ListAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}*/
    // Declare Variables
    private Context ctx;
    private int layoutId;
    private ArrayList<Categorymodelclass> catDataList;

    Translator t;
    String _Img;
    public SellerCategoryadapter(Context ctx, ArrayList<Categorymodelclass> catDataList, int layoutId) {
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
    public Categorymodelclass getItem(int position) {
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
        if (position == 0) {
            label.setText(ctx.getResources().getString(R.string.pleaseselectcategory));
        } else {
            label.setText(t.translate(ctx,this.getItem(position).category_name));
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
        if (position == 0) {
            title.setText(t.translate(ctx,"Please select Category"));
        } else {
            title.setText(t.translate(ctx,this.getItem(position).category_name));

       }

      //title.setText(catDataList.get(position).category_name);
        return result;

    }





















}
