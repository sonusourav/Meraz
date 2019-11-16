package com.sonusourav.merazoverflow;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sonusourav.merazoverflow.helper.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

  private Intent splashIntent;
  private PreferenceManager splashPref;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setContentView(R.layout.activity_splash);
    splashPref = new PreferenceManager(this);

    ImageView splash = findViewById(R.id.splash_iv);
    Glide.with(this)
        .load(R.drawable.gif_splash)
        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
        .into(splash);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (splashPref.isFirstTimeLaunch()) {
          splashIntent = new Intent(SplashActivity.this, IntroScreen.class);
        } else {
          splashIntent = new Intent(SplashActivity.this, MainActivity.class);
        }
        startActivity(splashIntent);
        overridePendingTransition(R.anim.fade_in, 0);
        finish();
      }
    }, 4600);
  }
}
