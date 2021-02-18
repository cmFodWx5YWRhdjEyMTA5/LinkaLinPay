package com.keo.onsite.linkalinpay.activity.filter;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.keo.onsite.linkalinpay.R;

public class FilterActivity extends AppCompatActivity   {
  RangeBar rangebar;

    
   
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filtercategory);

		rangebar = (RangeBar) findViewById(R.id.rangebar1);
		// Sets the display values of the indices
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {

            }

        });
        
        

	
	
	
	
	}
	







	
	
	
	
	
	








}
