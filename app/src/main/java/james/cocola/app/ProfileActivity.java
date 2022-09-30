package james.cocola.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import james.cocola.app.AppClass.AppUrl;
import james.cocola.app.AppClass.LoginPreferences;

import com.cocola.app.R;
import com.rabbil.toastsiliconlibrary.ToastSilicon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private LoginPreferences login ;
    private TextView name , department ;
    private TextView empId , empDesignation , empPhone , empEmail , empAddress , empPass;
    private LoginPreferences loginPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loginPreferences = new LoginPreferences(this);
        setTitle("Profile Details");

        login = new LoginPreferences(this);
        name = findViewById(R.id.profile_name);
        department = findViewById(R.id.profile_department);

        //finding
        empId = findViewById(R.id.profile_imp_id);
        empDesignation = findViewById(R.id.profile_designatin_id);
        empPhone = findViewById(R.id.profile_phone_no);
        empEmail = findViewById(R.id.profile_email);
        empAddress = findViewById(R.id.profile_address);
        empPass = findViewById(R.id.profile_password);

        name.setText(login.onNameGet());
        department.setText("Department : "+login.onDepartmentGet());


        loadProfile();

    }
    private void loadProfile(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String urlString = AppUrl.profileUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            empId.setText("Employee ID: "+jsonObject.getString("emp_id"));
                            if (!jsonObject.getString("designation").equals("null")){
                                empDesignation.setText("Designation: "+jsonObject.getString("designation"));
                            }
                            if (!jsonObject.getString("phone_no").equals("null")){
                                empPhone.setText("Phone No: "+jsonObject.getString("phone_no"));
                            }
                            if (!jsonObject.getString("email").equals("null")){
                                empEmail.setText("Email: "+jsonObject.getString("email"));
                            }
                            if (!jsonObject.getString("address").equals("null")){
                                empAddress.setText("Address: "+jsonObject.getString("address"));
                            }
                            if (!jsonObject.getString("password").equals("null")){
                                empPass.setText("Password: "+jsonObject.getString("password"));
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Log.e("ee",e.toString());
                            ToastSilicon.toastDangerTwo(ProfileActivity.this,"Something Went Wrong !", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        ToastSilicon.toastDangerTwo(ProfileActivity.this,"Please Check Your Internet Connection !",Toast.LENGTH_SHORT);
                    }
                }
        ){
            protected Map<String , String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("emp_id",loginPreferences.onLoginIdGet());
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
}