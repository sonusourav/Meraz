package com.sonusourav.merazoverflow.api;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.sonusourav.merazoverflow.R;

import static android.view.View.GONE;

public class MyWebViewClient extends WebViewClient {

  private Activity activity = null;
  private ProgressBar progressBar;

  public MyWebViewClient(Activity activity) {
    this.activity = activity;
    this.progressBar = activity.findViewById(R.id.web_progress_bar);
  }

  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
    super.onPageStarted(view, url, favicon);
  }

  @Override public void onPageCommitVisible(WebView view, String url) {
    super.onPageCommitVisible(view, url);
    progressBar.setVisibility(GONE);
  }

  @Override
  public void onPageFinished(WebView view, String url) {
    if (progressBar.getVisibility() == View.VISIBLE) {
      progressBar.setVisibility(GONE);
    }
  }

  @Override
  public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
    Toast.makeText(activity, "Check your internet connection", Toast.LENGTH_SHORT).show();
    progressBar.setVisibility(GONE);
  }

  @Override
  public void onReceivedHttpError(
      WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
    Toast.makeText(activity, "Check your internet connection", Toast.LENGTH_SHORT).show();
    progressBar.setVisibility(GONE);
  }

  @Override
  public void onReceivedSslError(WebView view, SslErrorHandler handler,
      SslError error) {
    Toast.makeText(activity, "Check your internet connection", Toast.LENGTH_SHORT).show();
    progressBar.setVisibility(GONE);
  }

  @Override
  public boolean shouldOverrideUrlLoading(WebView webView, String url) {
    if (url.contains("stackoverflow.com")) {
      return false;
    }
    progressBar.setVisibility(View.VISIBLE);

    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    activity.startActivity(intent);
    return true;
  }
}