package com.s21tasks.app.TabLayoutHelper;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.s21tasks.app.AppClass.AppUrl;
import com.s21tasks.app.AppClass.LoginPreferences;
import com.s21tasks.app.HisoryRecycler.HistoryAdapter;
import com.s21tasks.app.HisoryRecycler.HistoryItems;
import com.s21tasks.app.R;
import com.rabbil.toastsiliconlibrary.ToastSilicon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InProgressFragment extends Fragment {
    private RecyclerView tabProgressRecyclerview ;
    private List<HistoryItems> historyList;
    private HistoryAdapter tabProgressAdapter;
    private LoginPreferences loginPreferences ;
    private RelativeLayout noDataLayout , noInternetLayout  , lodingProgressLayout  ;
    private TextView statusView ;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab_inprogress, container, false);
        loginPreferences = new LoginPreferences(getContext());
        //finding components
        noDataLayout = root.findViewById(R.id.no_data_layout);
        noInternetLayout = root.findViewById(R.id.no_internet_layout);
        lodingProgressLayout = root.findViewById(R.id.loding_progress_layout);
        statusView = root.findViewById(R.id.tab_progress_status);
        Log.e("tabtest","callingTabProgress");
        //configuing recyclerview
        tabProgressRecyclerview = root.findViewById(R.id.tab_progress_recyclerview);
        tabProgressRecyclerview.setNestedScrollingEnabled(true);
        tabProgressRecyclerview.setHasFixedSize(false);
        tabProgressRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        historyList = new ArrayList<>();
        loadHistory();

        return root;
    }
    private void loadHistory(){
        statusView.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.GONE);
        lodingProgressLayout.setVisibility(View.VISIBLE);
        historyList.clear();
        String urlString = AppUrl.totalHistoryUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        historyList.clear();
                        lodingProgressLayout.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()>=1){
                                for(int i = 0 ;i<jsonArray.length();i++){
                                    JSONObject issueObj = jsonArray.getJSONObject(i);
                                    if (!issueObj.getString("status").equals("1")){
                                        continue;
                                    }
                                    HistoryItems myItems = new HistoryItems(
                                            issueObj.getString("issue_image_url"),
                                            issueObj.getString("issue_title"),
                                            issueObj.getString("created_at"),
                                            issueObj.getString("issue_details"),
                                            issueObj.getString("location"),
                                            issueObj.getString("issue_no"),
                                            issueObj.getString("status")

                                    );
                                    historyList.add(myItems);
                                }
                                tabProgressAdapter = new HistoryAdapter(getContext(),historyList);
                                tabProgressRecyclerview.setAdapter(tabProgressAdapter);
                            }else{
                                noDataLayout.setVisibility(View.VISIBLE);
                                noInternetLayout.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            lodingProgressLayout.setVisibility(View.GONE);
                            e.printStackTrace();
                            ToastSilicon.toastDangerTwo(getContext(),"Something Went Wrong !", Toast.LENGTH_SHORT);
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
                params.put("emp_id",loginPreferences.onLoginIdGet());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
}
