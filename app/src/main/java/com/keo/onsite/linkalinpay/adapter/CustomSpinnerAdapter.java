package com.keo.onsite.linkalinpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.keo.onsite.linkalinpay.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    String[] objects;
    String firstElement;
    boolean isFirstTime;

    public CustomSpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
        this.isFirstTime = true;
        //setDefaultText(defaultText);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(isFirstTime) {
            objects[0] = firstElement;
            isFirstTime = false;
        }
        return getCustomView(position, convertView, parent);
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getCustomView(position, convertView, parent);
    }
*/
    /*public void setDefaultText(String defaultText) {
        this.firstElement = objects[0];
        objects[0] = defaultText;
    }*/

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.contact_spinner_row_nothing_selected, parent, false);
        TextView label = (TextView) row.findViewById(R.id.text1);
        label.setText(objects[position]);

        return row;
    }













}
