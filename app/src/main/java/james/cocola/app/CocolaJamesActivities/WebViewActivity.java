package james.cocola.app.CocolaJamesActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.cocola.app.R;

import java.util.Stack;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView ;
    private AlertDialog builder;
    private String mainUrl  = "https://sketchfab.com/3d-models/jwst-james-webb-space-telescope-6c92c08a672640afb58ee44d248fd0fe";
    private Boolean loadStatus = true;
    private RelativeLayout loadingLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_view);
        //finding
        LoadWeb();
    }

    public void LoadWeb(){
        loadStatus = true ;
        webView=findViewById(R.id.cocola_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(WebView_Config.JavaScriptEnable());
        webSettings.setDomStorageEnabled(WebView_Config.DomStorageEnabled());
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.setWebViewClient(new UriWebViewClient());
        webView.setWebChromeClient(new UriChromeClient());
        webSettings.setSavePassword(WebView_Config.SavePassword());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView,true);
        }
        webSettings.setSaveFormData(WebView_Config.SaveFormData());
        webView.getSettings().setJavaScriptEnabled(WebView_Config.JavaScriptEnable());
        webView.getSettings().setAppCacheEnabled(WebView_Config.AppCache());
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setSupportZoom(WebView_Config.SupportZoom());
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        try{
            webView.loadUrl(mainUrl);
        }catch (Exception e){

        }
        webView.setWebViewClient(new WebViewClient(){

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                loadStatus = false ;
                new AlertDialog.Builder(WebViewActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("No Internet Connection!")
                        .setMessage("Please check your connection and try again. ")
                        .setPositiveButton("RETRY", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    LoadWeb();
                                }catch (Exception e){

                                }
                            }

                        })
                        .setNegativeButton("CANCEL", null)
                        .show();
                webView.setVisibility(View.GONE);
            }
            public  void  onPageFinished(WebView view, String url){
                if (loadStatus){
                    try{
                        webView.setVisibility(View.VISIBLE);
                        loadingLayout.setVisibility(View.GONE);
                    }catch (Exception e){

                    }
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if(url.equals("mailto:support@kistimath.com")){
                        LoadWeb();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        new AlertDialog.Builder(this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Closing Activity")
                                .setMessage("Are you sure you want to close this Activity?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
    }
    class UriChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            webView = new WebView(WebViewActivity.this);
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setWebViewClient(new UriWebViewClient());
            webView.setWebChromeClient(new UriChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSavePassword(true);
            webView.getSettings().setSaveFormData(true);
            builder = new AlertDialog.Builder(WebViewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();


            builder.setTitle("");
            builder.setView(webView);

            builder.setButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    webView.destroy();
                    dialog.dismiss();
                }
            });

            builder.show();
            builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.setAcceptThirdPartyCookies(webView,true);
            }

            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(webView);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            try {
                webView.destroy();
            } catch (Exception e) {

            }
            try {
                builder.dismiss();

            } catch (Exception e) {

            }
        }
    }

    private class UriWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {

        }
    }

}