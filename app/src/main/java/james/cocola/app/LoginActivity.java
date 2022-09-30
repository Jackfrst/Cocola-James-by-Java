package james.cocola.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import james.cocola.app.AppClass.AppUrl;
import james.cocola.app.AppClass.LoginPreferences;
import james.cocola.app.AppClass.ProgressDialogIndicatorColorChange;

import com.cocola.app.R;
import com.google.android.material.textfield.TextInputEditText;

import com.rabbil.toastsiliconlibrary.ToastSilicon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText empolyeeId, passwordField ;
    private Button loginBtn ;
    private LoginPreferences loginPreferences ;
    private String deviceId = "null" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Action Bar Hide
        getSupportActionBar().hide(); 
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //finding component
        empolyeeId = findViewById(R.id.login_emp_id);
        passwordField = findViewById(R.id.login_password_edittext_id);
        loginBtn = findViewById(R.id.login_btn_id);


//        deviceId = OneSignal.getDeviceState().getUserId();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
        //initialize login preference
         loginPreferences = new LoginPreferences(this);
    }

    private void validation(){
        if(empolyeeId.getText().toString().isEmpty()){
            ToastSilicon.toastDangerTwo(this, "Employee ID Required !", Toast.LENGTH_SHORT);
        }else if(passwordField.getText().toString().isEmpty()){
            ToastSilicon.toastDangerTwo(this, "Employee Password Required !", Toast.LENGTH_SHORT);
        }else{
            loginRequest();
        }
    }
    private void loginRequest(){
        String loginCode = empolyeeId.getText().toString();
        String loginPass =  passwordField.getText().toString();
        String url = AppUrl.loginUrl();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging....");
        progressDialog.setCancelable(false);
        ProgressDialogIndicatorColorChange.setProgressColor(progressDialog,LoginActivity.this);
        progressDialog.show();
        StringRequest str = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("1")){
                            getProfileInfo(loginCode,progressDialog);
                        }else{
                            progressDialog.dismiss();
                            ToastSilicon.toastDangerTwo(LoginActivity.this, "Invalid Employee ID or Password !", Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        ToastSilicon.toastWarningTwo(LoginActivity.this,"Please Check Your Internet Connection!",Toast.LENGTH_SHORT);
                    }
                }
        ){
            protected Map<String , String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("emp_id",loginCode);
                params.put("password",loginPass);
                params.put("master_key",deviceId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        str.setShouldCache(false);
        requestQueue.add(str);
    }
    private void getProfileInfo(String getLoginCode , ProgressDialog getProgressDialog){
        String url = AppUrl.fetchProfileUrl();
        getProgressDialog.setMessage("Loading....");
        StringRequest str = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject profileObj = jsonArray.getJSONObject(0);
                            getProgressDialog.dismiss();
                            ToastSilicon.toastSuccessTwo(LoginActivity.this, "Employee Login Successful", Toast.LENGTH_SHORT);
                            loginPreferences.onLoginSet(getLoginCode , profileObj.getString("emp_name"),profileObj.getString("department_name"));
                            Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(i);
                            finish();
                        }catch (Exception e){
                            getProgressDialog.dismiss();
                            ToastSilicon.toastWarningTwo(LoginActivity.this,"Please Check Your Internet Connection!",Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getProgressDialog.dismiss();
                        ToastSilicon.toastWarningTwo(LoginActivity.this,"Please Check Your Internet Connection!",Toast.LENGTH_SHORT);
                    }
                }
        ){
            protected Map<String , String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("emp_id",getLoginCode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        str.setShouldCache(false);
        requestQueue.add(str);
    }
  //  private String getMasterKey(){
//        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
//        status.getPermissionStatus().getEnabled();
//        String PushKey= status.getSubscriptionStatus().getUserId();
//        Log.e("pushKey",PushKey);
//        return PushKey ;
 //   }
}

