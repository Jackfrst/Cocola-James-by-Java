package com.s21tasks.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.s21tasks.app.AppClass.AppUrl;
import com.s21tasks.app.AppClass.LoginPreferences;
import com.rabbil.toastsiliconlibrary.ToastSilicon;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {
    private LinearLayout updatePassBtn ;
    //dialog components
    private TextView dialogTitle ;
    private Dialog updatePassDialog;
    private EditText passEditText , conPassEditText ;
    private Button updateBtn ;
    private LoginPreferences loginPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("App Settings");
        loginPreferences = new LoginPreferences(this);
        updatePassBtn  = findViewById(R.id.setting_update_password);


        //select type dialog
        updatePassDialog = new Dialog(this);
        updatePassDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updatePassDialog.setCancelable(true);
        updatePassDialog.setContentView(R.layout.update_pass_dialog);
        Window feedbackWindow = updatePassDialog.getWindow();
        feedbackWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        passEditText = updatePassDialog.findViewById(R.id.pass_dialog_new_pass_edittext);
        conPassEditText = updatePassDialog.findViewById(R.id.pass_dialog_confirm_pass_edit_text);
        dialogTitle = updatePassDialog.findViewById(R.id.dialog_title_view);
        updateBtn = updatePassDialog.findViewById(R.id.pass_dialog_confirm_btn);

        dialogTitle.setText("Update Password");
        updatePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassDialog.show();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passEditText.getText().toString().isEmpty()) {
                    ToastSilicon.toastDangerTwo(SettingActivity.this,"Please Insert Password!",Toast.LENGTH_SHORT);
                }else if (passEditText.getText().toString().length()<6) {
                    ToastSilicon.toastDangerTwo(SettingActivity.this, "Required Over 6 digit Password!", Toast.LENGTH_SHORT);
                }else if (!passEditText.getText().toString().equals(conPassEditText.getText().toString())) {
                    ToastSilicon.toastDangerTwo(SettingActivity.this,"Please Enter the Same Password!",Toast.LENGTH_SHORT);
                }else {
                    updatePassword();
                }
            }
        });

    }
    private void updatePassword(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String urlString = AppUrl.updatePassUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.equals("1")) {
                            ToastSilicon.toastPrimaryTwo(SettingActivity.this, "Password Updated", Toast.LENGTH_SHORT);
                            passEditText.setText("");
                            conPassEditText.setText("");
                            updatePassDialog.dismiss();
                        } else {
                            ToastSilicon.toastPrimaryTwo(SettingActivity.this, "Failed To Update Password!", Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        ToastSilicon.toastDangerTwo(SettingActivity.this,"Please Check Your Internet Connection !",Toast.LENGTH_SHORT);
                    }
                }
        ){
            protected Map<String , String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("emp_id",loginPreferences.onLoginIdGet());
                params.put("password",passEditText.getText().toString());
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