package com.s21tasks.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.s21tasks.app.AppClass.AppUrl;
import com.s21tasks.app.AppClass.OnCallItemClick;
import com.s21tasks.app.NumbersRecyclerview.NumbersAdapter;
import com.s21tasks.app.NumbersRecyclerview.NumbersItems;
import com.rabbil.toastsiliconlibrary.ToastSilicon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumbersActivity extends AppCompatActivity implements OnCallItemClick {
    //recyclerview
    private RecyclerView numberRecyclerview ;
    private List<NumbersItems> numberList;
    private NumbersAdapter numbersAdapter;

    private RelativeLayout noDataLayout , noInternetLayout  , lodingProgressLayout  ;
    private TextView statusView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_numbers);
        setTitle("Emergency Numbers");

        //finding components
        noDataLayout = findViewById(R.id.no_data_layout);
        noInternetLayout = findViewById(R.id.no_internet_layout);
        lodingProgressLayout = findViewById(R.id.loding_progress_layout);
        statusView = findViewById(R.id.number_status_view);

        //configuing recyclerview
        numberRecyclerview = findViewById(R.id.numbers_recyclerview);
        numberRecyclerview.setNestedScrollingEnabled(true);
        numberRecyclerview.setHasFixedSize(false);
        numberRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        numberList = new ArrayList<>();
        loadAllNumbers();
    }
    private void loadAllNumbers(){
        statusView.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.GONE);
        lodingProgressLayout.setVisibility(View.VISIBLE);
        numberList.clear();
        String urlString = AppUrl.emergencyContactNumberUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        numberList.clear();
                        lodingProgressLayout.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()>=1){
                                for(int i = 0 ;i<jsonArray.length();i++){
                                    JSONObject numberObj = jsonArray.getJSONObject(i);
                                    NumbersItems myItems = new NumbersItems(
                                            numberObj.getString("phone_no"),
                                            numberObj.getString("name")
                                    );
                                    numberList.add(myItems);
                                }
                                numbersAdapter = new NumbersAdapter(getApplicationContext(),numberList,NumbersActivity.this);
                                numberRecyclerview.setAdapter(numbersAdapter);
                            }else{
                                noDataLayout.setVisibility(View.VISIBLE);
                                noInternetLayout.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            lodingProgressLayout.setVisibility(View.GONE);
                            e.printStackTrace();
                            ToastSilicon.toastDangerTwo(NumbersActivity.this,"Something Went Wrong !", Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lodingProgressLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.GONE);
                        noInternetLayout.setVisibility(View.VISIBLE);
                    }
                }
        ){
            protected Map<String , String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("search_key","a");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCallClick(String number) {
        goCall(number);
    }
    private void goCall(String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" +number));
        startActivity(intent);
    }
}