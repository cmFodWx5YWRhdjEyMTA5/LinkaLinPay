package com.keo.onsite.linkalinpay.activity.shared;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.LoginActivity;
import com.keo.onsite.linkalinpay.activity.MainActivity;

public class UserShared {
	private final Context context;
	private final SharedPreferences prefs;
    SharedPreferences.Editor editor;
	 public UserShared(Context context) {
		this.context = context;
		prefs = context.getSharedPreferences("MY_SHARED_PREF", context.MODE_PRIVATE);
		editor = prefs.edit();

	}
	public boolean getLoggedInStatus_seller(){
		return prefs.getBoolean(context.getString(R.string.shared_loggedin_status_seller), false);
	}
	public boolean getLoggedInStatus_customer(){
		return prefs.getBoolean(context.getString(R.string.shared_loggedin_status_customer), false);
	}

    public String getSellerEmail(){
	 return prefs.getString(context.getString(R.string.shared_seller_email), context.getString(R.string.shared_no_data));
	}

	public String getSellerName(){
		return prefs.getString(context.getString(R.string.shared_seller_name), context.getString(R.string.shared_no_data));
	}

	public String getSellermobile(){
		return prefs.getString(context.getString(R.string.shared_seller_mobile), context.getString(R.string.shared_no_data));
	}

	public String getSellerlogo(){
		return prefs.getString(context.getString(R.string.shared_seller_logo), context.getString(R.string.shared_no_data));
	}

	public String getSellerbusinessname(){
		return prefs.getString(context.getString(R.string.shared_seller_businessname), context.getString(R.string.shared_no_data));
	}

	public String getSellerinstagramname(){
		return prefs.getString(context.getString(R.string.shared_seller_instagramname), context.getString(R.string.shared_no_data));
	}


	public String getSellerid(){
		return prefs.getString(context.getString(R.string.shared_seller_id), context.getString(R.string.shared_no_data));
	}
	public String getCustomerid(){
		return prefs.getString(context.getString(R.string.shared_customer_id), context.getString(R.string.shared_no_data));
	}

	public String getVendorid(){
		return prefs.getString(context.getString(R.string.shared_vendor_id), context.getString(R.string.shared_no_data));
	}

	public String getUsername_cust(){
		return prefs.getString(context.getString(R.string.shared_customer_name), context.getString(R.string.shared_no_data));
	}
	public String getUsername_mail(){
		return prefs.getString(context.getString(R.string.shared_customer_email), context.getString(R.string.shared_no_data));
	}

	public String getUsername_mobile(){
		return prefs.getString(context.getString(R.string.shared_customer_mobilenumber), context.getString(R.string.shared_no_data));
	}
	public String getUsername_pic(){
		return prefs.getString(context.getString(R.string.shared_customer_pic), context.getString(R.string.shared_no_data));
	}

	public String getType(){
	return prefs.getString(context.getString(R.string.shared_usertype), context.getString(R.string.shared_no_data));
	}
	public String getCurrencysymbol(){
		return prefs.getString(context.getString(R.string.shared_seller_currency), context.getString(R.string.shared_no_data));
	}




	public void aleartLogOut() {
		// Check login status

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(R.string.logout_dialog_title));
		builder.setMessage(context.getResources().getString(R.string.logout_dialog_message));

		String positiveText = context.getResources().getString(R.string.logout_ok);
		builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				logoutUser();
				Intent i = new Intent(context, MainActivity.class);
				i.putExtra("Type", "User");
				context.startActivity(i);
				dialog.dismiss();
				Toast.makeText(context, "Logout Successfully", Toast.LENGTH_SHORT).show();
				((Activity) context).finish();
			}
		});

		String negativeText = context.getResources().getString(R.string.logout_cancel);
		builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// negative button logic
			}
		});

		AlertDialog dialog = builder.create();
		// display dialog
		dialog.show();

	}

	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Loing Activity
		Intent i = new Intent(context, LoginActivity.class);

		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		context.startActivity(i);

	}

}
