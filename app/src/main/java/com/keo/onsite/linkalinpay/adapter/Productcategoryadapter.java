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
import com.keo.onsite.linkalinpay.activity.model.Parentcategorymodelclass;
import com.keo.onsite.linkalinpay.utils.Translator;

import java.util.ArrayList;

public class Productcategoryadapter extends ArrayAdapter<Parentcategorymodelclass> {
    /*public ListAdapter(Context context, int resource) {
            super(context, resource);
            // TODO Auto-generated constructor stub
        }*/
    // Declare Variables
    private Context ctx;
    private int layoutId;
    private ArrayList<Parentcategorymodelclass> catDataList;
    Translator t;

    String _Img;
    public Productcategoryadapter(Context ctx, ArrayList<Parentcategorymodelclass> catDataList, int layoutId) {
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
        return catDataList.size()+1 ;
    }

    @Override
    public Parentcategorymodelclass getItem(int position) {
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
        if(position==0){
            return initialSelection(true);

        }
        /*TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.GRAY);
        label.setText(this.getItem(position).category_name);
        return label;*/

        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

     if(position==0){

         return initialSelection(false);
     }
        return getCustomView(position, convertView, parent);


     /*View result = convertView;

        if (result == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            result = inflater.inflate(layoutId, parent, false);
        }
        TextView title=(TextView)result.findViewById(R.id.cat_name);
        title.setText(catDataList.get(position).category_name);
        return result;
*/
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        // Distinguish "real" spinner items (that can be reused) from initial selection item
        View row = convertView != null && !(convertView instanceof TextView)
                ? convertView :
                LayoutInflater.from(getContext()).inflate(R.layout.catgry_item, parent, false);

        position = position - 1; // Adjust for initial selection item
        Parentcategorymodelclass item = getItem(position);

        // ... Resolve views & populate with data ...
        TextView title=(TextView)row.findViewById(R.id.cat_name);
        title.setText(t.translate(ctx,item.category_name));
        return row;
    }
    private View initialSelection(boolean dropdown) {
        // Just an example using a simple TextView. Create whatever default view
        // to suit your needs, inflating a separate layout if it's cleaner.
        TextView view = new TextView(getContext());
        view.setText(t.translate(ctx,"Select Category"));
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_smaller);
        view.setPadding(0, spacing, 0, spacing);

        if (dropdown) { // Hidden when the dropdown is opened
            view.setHeight(0);
        }

        return view;
    }




}
