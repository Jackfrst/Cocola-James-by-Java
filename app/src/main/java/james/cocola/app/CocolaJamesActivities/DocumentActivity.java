package james.cocola.app.CocolaJamesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.cocola.app.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.w3c.dom.Document;

public class DocumentActivity extends AppCompatActivity {
    private WebView webView ;
    private AlertDialog builder;
    private String mainUrl  = "https://www.flickr.com/photos/nasawebbtelescope/albums/72177720301006030";
    private Boolean loadStatus = true;
    private LottieAnimationView lottieAnimationView;
    private LinearLayout documentQuizLayout , documentQuizBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        lottieAnimationView = findViewById(R.id.web_view_loading_lottie);
        webView = findViewById(R.id.cocola_doc_webview);
        LoadWeb(mainUrl);

        documentQuizLayout = findViewById(R.id.document_quiz_layout);
        documentQuizBtn = findViewById(R.id.document_add_zem_btn);

        documentQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeIn)
                        .duration(500).withListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                documentQuizLayout.setVisibility(View.VISIBLE);
                            }
                            @Override
                            public void onAnimationEnd(Animator animator) {

                            }
                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .repeat(0)
                        .playOn(findViewById(R.id.document_quiz_layout));
            }
        });

    }
    public void LoadWeb(String getMainUrl){
        lottieAnimationView.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);

        loadStatus = true ;
        webView=findViewById(R.id.cocola_doc_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(WebView_Config.JavaScriptEnable());
        webSettings.setDomStorageEnabled(WebView_Config.DomStorageEnabled());
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.setWebViewClient(new DocumentActivity.UriWebViewClient());
        webView.setWebChromeClient(new DocumentActivity.UriChromeClient());
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
            webView.loadUrl(getMainUrl);
        }catch (Exception e){

        }
        webView.setWebViewClient(new WebViewClient(){

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                loadStatus = false ;
                new AlertDialog.Builder(DocumentActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("No Internet Connection!")
                        .setMessage("Please check your connection and try again. ")
                        .setPositiveButton("RETRY", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    LoadWeb(getMainUrl);
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
                        lottieAnimationView.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                    }catch (Exception e){

                    }
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if(url.equals("mailto:support@kistimath.com")){
                        LoadWeb(getMainUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
    private class UriWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {

        }
    }
    class UriChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            webView = new WebView(DocumentActivity.this);
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setWebViewClient(new DocumentActivity.UriWebViewClient());
            webView.setWebChromeClient(new DocumentActivity.UriChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSavePassword(true);
            webView.getSettings().setSaveFormData(true);
            builder = new AlertDialog.Builder(DocumentActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();


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
}