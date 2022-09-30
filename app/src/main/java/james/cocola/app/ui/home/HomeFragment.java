package james.cocola.app.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import james.cocola.app.AppClass.AppUrl;
import james.cocola.app.AppClass.LoginPreferences;
import james.cocola.app.HomeIssueRecycler.HomeIssueAdapter;
import james.cocola.app.HomeIssueRecycler.HomeIssueItems;
import james.cocola.app.NotificationActivity;
import james.cocola.app.NumbersActivity;
import com.cocola.app.R;
import com.rabbil.toastsiliconlibrary.ToastSilicon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    //recyclerview
    private RecyclerView homeIssueRecycler;
    private List<HomeIssueItems> homeIssueItemsList;
    private HomeIssueAdapter homeIssueAdapter;
    private LinearLayout loadingLayout ,statusLayout ;
    private TextView statusView ;
    private SwipeRefreshLayout homeSwipe ;
    private CardView emergencyNumberBtn , notificationsBtn , call999Btn ;
    private LoginPreferences loginPreferences ;
    private boolean canResume  = false ;
    private TextView totalIssueView , pendingView , rejectedView , solvedView ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        loadingLayout = root.findViewById(R.id.home_progress_layout);
        statusLayout = root.findViewById(R.id.home_status_layout);
        statusView = root.findViewById(R.id.home_status_text_view);
        homeSwipe = root.findViewById(R.id.home_swipe_id);
        emergencyNumberBtn = root.findViewById(R.id.home_emergency_call_btn);
        notificationsBtn = root.findViewById(R.id.home_notification);
        call999Btn = root.findViewById(R.id.home_call_999);

        loginPreferences = new LoginPreferences(getContext());

        totalIssueView = root.findViewById(R.id.home_total);
        pendingView = root.findViewById(R.id.home_pending);
        rejectedView = root.findViewById(R.id.home_rejected);
        solvedView = root.findViewById(R.id.home_solved);
        //configuing recyclerview
        homeIssueRecycler = root.findViewById(R.id.home_issue_recyclerview);
        homeIssueRecycler.setNestedScrollingEnabled(true);
        homeIssueRecycler.setHasFixedSize(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),4);
        homeIssueRecycler.setLayoutManager(gridLayoutManager);
        homeIssueItemsList = new ArrayList<>();
        loadIssueItems();
        loadCount();

        //onswipe
        homeSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadIssueItems();
                loadCount();
            }
        });
        emergencyNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NumbersActivity.class);
                startActivity(i);
            }
        });
        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NotificationActivity.class);
                startActivity(i);
            }
        });
        call999Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAdminNumber();
            }
        });

        return root;
    }

    private void loadIssueItems(){
        homeSwipe.setRefreshing(false);
        homeIssueItemsList.clear();
        loadingLayout.setVisibility(View.VISIBLE);
        homeIssueRecycler.setVisibility(View.GONE);
        statusLayout.setVisibility(View.GONE);
        String urlString = AppUrl.homeIssueUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("pkerr",response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()>=1){
                                for(int i = 0 ;i<jsonArray.length();i++){
                                    JSONObject catObject = jsonArray.getJSONObject(i);
                                    HomeIssueItems myItems = new HomeIssueItems(
                                            catObject.getString("cat_code"),
                                            catObject.getString("cat_name"),
                                            catObject.getString("cat_image")
                                    );
                                    homeIssueItemsList.add(myItems);
                                }
                                homeIssueAdapter = new HomeIssueAdapter(getContext().getApplicationContext(),homeIssueItemsList);
                                homeIssueRecycler.setAdapter(homeIssueAdapter);
                                loadingLayout.setVisibility(View.GONE);
                                homeIssueRecycler.setVisibility(View.VISIBLE);
                                statusLayout.setVisibility(View.GONE);
                            }else{
                                loadingLayout.setVisibility(View.GONE);
                                homeIssueRecycler.setVisibility(View.GONE);
                                statusLayout.setVisibility(View.VISIBLE);
                                statusView.setText("No Data Found");

                                ToastSilicon.toastPrimaryTwo(getContext(),"No Data Found!", Toast.LENGTH_SHORT);
                            }

                        } catch (JSONException e) {
                            //progressDialog.dismiss();
                            e.printStackTrace();
                            Log.e("catError",e.toString());
                            loadingLayout.setVisibility(View.GONE);
                            homeIssueRecycler.setVisibility(View.GONE);
                            statusLayout.setVisibility(View.VISIBLE);
                            statusView.setText("Something Went Wront! Try Again");

                            ToastSilicon.toastDangerTwo(getContext(),"Something Went Wrong !",Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingLayout.setVisibility(View.GONE);
                        homeIssueRecycler.setVisibility(View.GONE);
                        statusLayout.setVisibility(View.VISIBLE);
                        statusView.setText("Please Check Your Internet Connection");

                        ToastSilicon.toastDangerTwo(getContext(),"Internet Error",Toast.LENGTH_SHORT);
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
    private void loadCount(){
        totalIssueView.setText("-");
        pendingView.setText("-");
        rejectedView.setText("-");
        solvedView.setText("-");
        String urlString = AppUrl.homeCountUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("pkerr",response);
                        try {
                           JSONObject jsonObject = new JSONObject(response);

                           totalIssueView.setText(jsonObject.getString("totalIssue"));
                           pendingView.setText(jsonObject.getString("totalPendingIssues"));
                           rejectedView.setText(jsonObject.getString("totalRejectedIssues"));
                           solvedView.setText(jsonObject.getString("totalSolved"));

                        } catch (JSONException e) {
                            //progressDialog.dismiss();
                            e.printStackTrace();
                            Log.e("catError",e.toString());
                            totalIssueView.setText("-");
                            pendingView.setText("-");
                            rejectedView.setText("-");
                            solvedView.setText("-");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        totalIssueView.setText("-");
                        pendingView.setText("-");
                        rejectedView.setText("-");
                        solvedView.setText("-");
                        Log.e("err",error.toString());

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
    private void loadAdminNumber(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String urlString = AppUrl.adminNumUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("typeresponse",response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" +jsonObject.getString("phone_no")));
                            startActivity(intent);
                        } catch (JSONException e) {
                            ToastSilicon.toastDangerTwo(getContext(),"Something Went Wrong !", Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        ToastSilicon.toastDangerTwo(getContext(),"Please Check Your Internet Connection !",Toast.LENGTH_SHORT);
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(canResume){
            loadCount();
        }else{
            canResume = true ;
        }
    }

}