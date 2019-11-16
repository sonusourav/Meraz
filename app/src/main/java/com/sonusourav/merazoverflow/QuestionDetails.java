package com.sonusourav.merazoverflow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import com.sonusourav.merazoverflow.api.MyWebViewClient;

public class QuestionDetails extends AppCompatActivity {

  StringBuilder url = new StringBuilder("https://stackoverflow.com/");

  WebView webView;

  @SuppressLint("SetJavaScriptEnabled") @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.question_details);
    webView = findViewById(R.id.webview);
    ProgressBar progressBar = findViewById(R.id.web_progress_bar);
    progressBar.setVisibility(View.VISIBLE);

    String bundle = getIntent().getStringExtra("url");
    if (!bundle.isEmpty()) {
      url.setLength(0);
      url.append(bundle);
    }

    WebSettings settings = webView.getSettings();

    settings.setJavaScriptEnabled(true);
    webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
    settings.setBuiltInZoomControls(true);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);

    progressBar.setVisibility(View.VISIBLE);

    webView.setWebViewClient(new MyWebViewClient(this));
    webView.loadUrl(url.toString());
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
      this.webView.goBack();
      return true;
    }

    return super.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
