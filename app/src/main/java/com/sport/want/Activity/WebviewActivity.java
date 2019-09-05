package com.sport.want.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.sport.want.Language.LanguageListener;
import com.sport.want.Language.SetLanguage;
import com.sport.want.R;
import com.sport.want.Support.InternetImage;
import com.sport.want.Support.Value;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import pl.droidsonroids.gif.GifImageView;

public class WebviewActivity extends AppCompatActivity implements LanguageListener {

    private String TAG = "WebviewActivity";
    private SetLanguage setLanguage = new SetLanguage();
    private TextView title, back, copyright, nowTime;
    private LinearLayout lineargif;
    private String title_text, geturl, company, account;
    private GifImageView gifImageView1;
    private Bitmap preview_bitmap;
    private WebView webview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean swipe = false;
    private InternetImage internetImage = new InternetImage();
    private Handler handler = new Handler(), refreshHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //隱藏標題欄
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        get_Intent();
    }

    private Runnable refreshUrl = new Runnable() {
        @Override
        public void run() {
            if (swipe) {
                swipe = false;
                refreshHandler.postDelayed(() -> {
                    webview.clearCache(true);
                    webview.reload();
                    swipeRefreshLayout.setRefreshing(false);
                }, 2000);
            }
        }
    };

    private void get_Intent() {
        Intent intent = getIntent();
        title_text = intent.getStringExtra("title");
        geturl = intent.getStringExtra("url");
        company = intent.getStringExtra("company");
        account = intent.getStringExtra("account");
        Log.e(TAG, "title = " + title_text);
        Log.e(TAG, "geturl = " + geturl);
        Log.e(TAG, "company = " + company);
        Log.e(TAG, "account = " + account);

        watchView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void watchView() {
        setContentView(R.layout.watchweb);

        lineargif = findViewById(R.id.linear_gif);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        title = findViewById(R.id.textView);   //對帳通知
        back = findViewById(R.id.textView1);   //返回
        copyright = findViewById(R.id.copyright);
        nowTime = findViewById(R.id.nowTime);
        gifImageView1 = findViewById(R.id.imageView1); //廣告欄
        Runnable getimage = () -> {
            String imageUri = "https://dl.kz168168.com/img/android-ad02.png";
            preview_bitmap = internetImage.fetchImage(imageUri);
            handler.post(() -> {
                gifImageView1.setImageBitmap(preview_bitmap);
                gifImageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            });
        };
        new Thread(getimage).start();
        gifImageView1.setOnClickListener(view -> {
            Uri uri = Uri.parse("http://3singsport.win");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        back.setOnClickListener(view -> homePage());
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipe = true;
            new Thread(refreshUrl).start();
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.progressColor);
        Value.updateTime = getDateTime();
        setLanguage.setListener(this);
        setLanguage.isSet();

        webview = findViewById(R.id.web_view);
        String userAgent = webview.getSettings().getUserAgentString();
        if (!TextUtils.isEmpty(userAgent)) {    //去除浮窗式廣告
            webview.getSettings().setUserAgentString(userAgent
                    .replace("Android", "")
                    .replace("android", "") + " cldc");
        }
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true); // 支持缩放
        // 设置出现缩放工具
        webview.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webview.getSettings().setUseWideViewPort(true);
        //不显示webview缩放按钮
        webview.getSettings().setDisplayZoomControls(false);
        //自适应屏幕
        //noinspection deprecation
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setAllowFileAccessFromFileURLs(true);
        webview.getSettings().setBlockNetworkImage(false);//不阻塞网络图片
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webview.setDownloadListener((url, userAgent1, contentDisposition, mimeType, contentLength) -> {
            // TODO: 处理下载事件
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        webview.setWebViewClient(new WebViewClient() {
            /*@SuppressLint("NewApi")
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request){
                if (request != null && request.getUrl() != null) {
                    String scheme = request.getUrl().getScheme().trim();
                    if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
                        return super.shouldInterceptRequest(view, new WebResourceRequest() {
                            @Override
                            public Uri getUrl() {
                                return Uri.parse(injectIsParams(request.getUrl().toString()));
                            }

                            @SuppressLint("NewApi")
                            @Override
                            public boolean isForMainFrame() {
                                return request.isForMainFrame();
                            }

                            @SuppressLint("NewApi")
                            @Override
                            public boolean hasGesture() {
                                return request.hasGesture();
                            }

                            @SuppressLint("NewApi")
                            @Override
                            public String getMethod() {
                                return request.getMethod();
                            }

                            @SuppressLint("NewApi")
                            @Override
                            public Map<String, String> getRequestHeaders() {
                                return request.getRequestHeaders();
                            }
                        });
                    }
                }
                return super.shouldInterceptRequest(view, request);
            }*/

            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重寫此方法表明點選網頁裡面的連結還是在當前的webview裡跳轉,不跳到瀏覽器那邊
                view.loadUrl(url);
                return true;
            }
        });
        webview.loadUrl(geturl);
    }

    private String getDateTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTime.setTimeZone(TimeZone.getTimeZone("America/New_York")); //美東時區
        return dateTime.format(date);
    }

    private void homePage() {
        if (company.matches("") && account.matches("")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginMainActivity.class);
            intent.putExtra("company", company);
            intent.putExtra("account", account);
            startActivity(intent);
            finish();
        }
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    homePage();
                }
            }
            break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        webview.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        webview.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.destroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
            lineargif.setVisibility(View.GONE);
            gifImageView1.setVisibility(View.GONE);
            copyright.setVisibility(View.GONE);
            nowTime.setVisibility(View.GONE);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            lineargif.setVisibility(View.VISIBLE);
            gifImageView1.setVisibility(View.VISIBLE);
            copyright.setVisibility(View.VISIBLE);
            nowTime.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setLanguage() {    //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
        if (Value.language_flag == 0) {
            title.setText(title_text);
            back.setText("back");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 1) {
            title.setText(title_text);
            back.setText("返回");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        } else if (Value.language_flag == 2) {
            title.setText(title_text);
            back.setText("返回");
            copyright.setText(Value.copyright_text + Value.ver);
            nowTime.setText(Value.updatestring + Value.updateTime);
        }
    }
}
