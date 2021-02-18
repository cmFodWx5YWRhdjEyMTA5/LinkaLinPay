package com.keo.onsite.linkalinpay.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.activity.tabswaper.MainActicityNewTwo;
import com.keo.onsite.linkalinpay.fragement.CategoryFragment;
import com.keo.onsite.linkalinpay.fragement.HelpSupportFragment;
import com.keo.onsite.linkalinpay.fragement.MyOrderFragment;
import com.keo.onsite.linkalinpay.fragement.ProductListFragment;
import com.keo.onsite.linkalinpay.fragement.ProfileFragment;
import com.keo.onsite.linkalinpay.fragement.ReferFragment;
import com.keo.onsite.linkalinpay.fragement.TrackOrderFragment;
import com.keo.onsite.linkalinpay.fragement.WishListFragment;
import com.keo.onsite.linkalinpay.utils.DialogCallLang;
import com.keo.onsite.linkalinpay.utils.LanguageChange;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //shop dash
    private AppBarConfiguration mAppBarConfiguration;
    private static final String TAG_HOME = "HomeFragement";
    public static String CURRENT_TAG = TAG_HOME;
    Fragment fragment;
    private Handler mHandler;
    LinearLayout line_shopper,line_Product,line_profile,line_Order,line_wishlist,line_help,line_logout,line_refer,line_trackOrder;
    DrawerLayout drawer;
    ImageView img_profile;
    UserShared psh;

    TextView name;
    ImageView langtrans;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageChange().LoadLocal(getBaseContext());
        setContentView(R.layout.activity_main_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        psh=new UserShared(this);
        mHandler = new Handler();
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        View header = navigationView.getHeaderView(0);

 //       View header = navigationView.inflateHeaderView(R.layout.navigation_guest_drawer_layout);
//        LayoutInflater.from(this).inflate(R.layout.navigation_guest_drawer_layout, null);

//       header.layout(R.layout.navigation_guest_drawer_layout,,,);
        img_profile=header.findViewById(R.id.img_profile);
        line_shopper=header.findViewById(R.id.line_shopper);
        //line_Product=header.findViewById(R.id.line_Product);
        line_profile=header.findViewById(R.id.line_profile);
        line_Order=header.findViewById(R.id.line_Order);
        line_wishlist=header.findViewById(R.id.line_wishlist);
        line_help=header.findViewById(R.id.line_help);
        line_logout=header.findViewById(R.id.line_logout);
        line_refer=header.findViewById(R.id.line_refer);
        line_trackOrder=header.findViewById(R.id.line_trackOrder);
        langtrans = header.findViewById(R.id.langtrans);

        name = header.findViewById(R.id.name);



        langtrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DialogCallLang dl = new DialogCallLang();
                dl.dialogchangelang(MainDashboardActivity.this);


            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        loadFragment(new CategoryFragment());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                loadFragment(new ProfileFragment());
                setTitle(R.string.profile);            }
        });
        line_shopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                loadFragment(new CategoryFragment());
                setTitle(R.string.shopper);
            }
        });


        drawerimgsetup();


        /*line_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                loadFragment(new ProductListFragment());
                setTitle("Products");
            }
        });*/
        line_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                loadFragment(new ProfileFragment());
                setTitle(R.string.profile);
            }
        });
        line_trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                loadFragment(new TrackOrderFragment());
                setTitle(R.string.trackorder);



            }
        });

        line_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                //loadFragment(new MyOrderFragment());
                //setTitle("My Order");
                Intent i=new Intent(MainDashboardActivity.this,MainActicityNewTwo.class);
                startActivity(i);


            }
        });
        line_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                loadFragment(new WishListFragment());
                setTitle(R.string.wishlist);
            }
        });
        line_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                loadFragment(new HelpSupportFragment());
                setTitle(R.string.helpands);
            }
        });
        line_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   showLogoutDialog();
            }
        });
        line_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                loadFragment(new ReferFragment());
                setTitle(R.string.referfriend);
            }
        });
    }


        private void drawerimgsetup() {
            if (VolleySingleton.getInstance(MainDashboardActivity.this).isConnected()) {
//                showProgress();
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.customerdashboard , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jobjMain = new JSONObject(response);
                            String status = jobjMain.getString("status");
                            // String msg = jobjMain.getString("msg");
                            if (status.equals("success")) {

                                Log.d("Profiledash>>>",response.toString());

                                Glide.with(getApplicationContext())
                                    .load(jobjMain.getJSONObject("dashboard").getString("cust_profile"))
                                    .into(img_profile);

                                name.setText(jobjMain.getJSONObject("dashboard").getString("cust_name"));


//                           JSONObject jobj=jobjMain.getJSONObject("trackorderdetail");
//                           orderdate.setText(jobj.getString("order_date"));
                                //ordertime.setText(jobj.getString(""));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //parseVolleyError(error);


                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("cust_id", psh.getCustomerid());


                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        // headers.put(Constants.authKEY, Constants.authValue);
                        // headers.put("header2", "header2");

                        return headers;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                VolleySingleton.getInstance(MainDashboardActivity.this).addToRequestQueue(stringRequest);
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }





        }


    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.cart) {
            Intent intent=new Intent(MainDashboardActivity.this,CartlistActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_dashboard, menu);
        return true;
    }
*/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
      //  displayItemSelectedScreen(item.getItemId());
        return true;

    }
//    private void displayItemSelectedScreen(int itemId) {
//
//        fragment = null;
//
//        switch (itemId) {
//            case R.id.line_shopper:
//                fragment = new ProfileFragment();
//                setTitle(R.string.profile);
//                break;
//
//            case R.id.nav_category:
//                fragment = new CategoryFragment();
//                setTitle(R.string.shopper);
//                break;
//
//
//            case R.id.nav_order:
//                fragment = new MyOrderFragment();
//                setTitle(R.string.myorder);
//                break;
//
//            case R.id.nav_trackorder:
//                fragment = new TrackOrderFragment();
//                setTitle(R.string.trackorder);
//                break;
//
//            case R.id.nav_refer:
//                fragment = new ReferFragment()   ;
//                setTitle(R.string.referfriend);
//                break;
//
//            case R.id.nav_wishlist:
//                fragment = new WishListFragment()   ;
//                setTitle(R.string.wishlist);
//                break;
//
//            case R.id.nav_help_support:
//                fragment = new HelpSupportFragment();
//                setTitle(R.string.helpands);
//                break;
//
//            case R.id.nav_product_list:
//                fragment = new ProductListFragment();
//                setTitle(R.string.productlist);
//                break;
//
//
//            case R.id.nav_logout:
//                showLogoutDialog();
//                break;
//        }
//        if (fragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.addToBackStack(null);
//            ft.replace(R.id.frame, fragment);
//            ft.commit();
//        }
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        //int count = getSupportFragmentManager().getBackStackEntryCount();

        /*if (count == 0) {
//            super.onBackPressed();
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainDashboardActivity.this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainDashboardActivity.this
                                    .finish();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }*/
    }
    public void showLogoutDialog() {
        psh.aleartLogOut();
        /*AlertDialog.Builder builder = new AlertDialog.Builder(MainDashboardActivity.this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        sessionManager.logoutUser();
                        //finish();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
 }}   }*/
}
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }



}