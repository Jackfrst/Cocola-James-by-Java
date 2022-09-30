package james.cocola.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cocola.app.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import james.cocola.app.AppClass.AppUrl;
import james.cocola.app.AppClass.LoginPreferences;
import james.cocola.app.CocolaJamesActivities.ControlRoomActivity;

public class MainActivity extends AppCompatActivity {
    private LoginPreferences loginPreferences ;
    private LottieAnimationView lottieAnimationView;
    private Button updateNowBtn, updateLaterBtn, forceUpdateBtn;
    private TextView splashName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        loginPreferences = new LoginPreferences(this);
        lottieAnimationView = findViewById(R.id.lotti_main);
        splashName = findViewById(R.id.splash_name);

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                //goToActivity();

                YoYo.with(Techniques.FadeIn)
                        .duration(700).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        splashName.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Intent i = new Intent(MainActivity.this, ControlRoomActivity.class);
                        startActivity(i);
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                        .repeat(0)
                        .playOn(findViewById(R.id.splash_name));
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        //loadAppUpdate();
        //forceUpdateDialog();
        //softUpdateDialog();
    }
        private void loadAppUpdate(){
        lottieAnimationView.playAnimation();
        String urlString = AppUrl.homeCountUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("pkerr",response);
                        lottieAnimationView.pauseAnimation();
//                        try {
//                            JSONObject jsonObject=new JSONObject(response);
//                            JSONArray array=jsonObject.getJSONArray("contents");
//                            JSONObject o=array.getJSONObject(0);
//                            String VERSION_CODE=o.getString("version_code").trim();
//                            String VERSION_STATUS=o.getString("status").trim();
//
//
//                            if((!VERSION_CODE.equals(String.valueOf(BuildConfig.VERSION_CODE))) && (VERSION_STATUS.equals("Essential"))){
//                                forceUpdateDialog();
//                            }
//                            else if((!VERSION_CODE.equals(String.valueOf(BuildConfig.VERSION_CODE))) && (VERSION_STATUS.equals("Optional"))){
//                                softUpdateDialog();
//                            }
//                            else{
//                                goToActivity();
//                            }
//
//                        } catch (JSONException e) {
//                            //progressDialog.dismiss();
//                            e.printStackTrace();
//                            ToastSilicon.toastDangerTwo(MainActivity.this,"Something went Wrong!", Toast.LENGTH_SHORT);
//
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lottieAnimationView.pauseAnimation();
                        internetErrorDialog();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
        private void goToActivity() {
        if(loginPreferences.onLoginIdGet().equals("null")){
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }
    }
        private void forceUpdateDialog(){
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.force_update_now_dialog);
            dialog.show();
            forceUpdateBtn = dialog.findViewById(R.id.forceUpdateNowButtonId);
            forceUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName()));
                    startActivity(i);
                }
            });
        }
        private void softUpdateDialog(){
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.update_now_or_later_dialog);
            updateNowBtn = dialog.findViewById(R.id.updateNowButtonId);
            updateLaterBtn = dialog.findViewById(R.id.updateLaterButtonId);
            dialog.show();
            updateLaterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    goToActivity();
                }
            });
            updateNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName()));
                    startActivity(i);
                }
            });
        }
        private void internetErrorDialog(){
        new AlertDialog.Builder(this)
                .setIcon(getResources().getDrawable(R.drawable.ic_baseline_warning_24))
                .setTitle("Internet Error!")
                .setCancelable(false)
                .setMessage("Please Check Your Internet Connection!")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadAppUpdate();
                    }

                })
                .show();
    }
}