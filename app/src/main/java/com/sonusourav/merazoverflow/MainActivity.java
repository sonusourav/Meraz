package com.sonusourav.merazoverflow;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sonusourav.merazoverflow.fragment.QuestionsFragment;
import com.sonusourav.merazoverflow.fragment.UsersFragment;
import com.sonusourav.merazoverflow.helper.BottomNavigationBehavior;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity {

  private ActionBar toolbar;
  private SearchView searchView;
  private int PERMISSION_REQUEST_CODE = 101;
  private CoordinatorLayout coordinatorLayout;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      Fragment fragment;
      switch (item.getItemId()) {
        case R.id.navigation_question:
          toolbar.setTitle("Question");
          fragment = new QuestionsFragment();
          loadFragment(fragment);
          return true;
        case R.id.navigation_users:
          toolbar.setTitle("Camera");
          fragment = new UsersFragment();
          loadFragment(fragment);
          return true;
      }

      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    toolbar = getSupportActionBar();

    BottomNavigationView navigation = findViewById(R.id.navigation);
    coordinatorLayout = findViewById(R.id.container);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    CoordinatorLayout.LayoutParams layoutParams =
        (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
    layoutParams.setBehavior(new BottomNavigationBehavior());

    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchView = findViewById(R.id.search_view);
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    searchView.setIconifiedByDefault(false);

    toolbar.setTitle("Questions");

    Log.d("Questions", checkPermission() + "");
    if (checkPermission()) {
      loadFragment(new QuestionsFragment());
    } else {
      requestPermission();
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      if (!query.isEmpty()) {
        Log.d("Questions", query);
        Bundle bundle = new Bundle();
        bundle.putString("question", query);
        QuestionsFragment fragment = new QuestionsFragment(bundle);
        loadFragment(fragment);
      }

    }
  }

  private void loadFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.frame_container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }


  private boolean checkPermission() {
    int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
    int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

    return (result1 == PackageManager.PERMISSION_GRANTED
        && result2 == PackageManager.PERMISSION_GRANTED);
  }

  private void requestPermission() {

    ActivityCompat.requestPermissions(this, new String[] { RECORD_AUDIO, CAMERA },
        PERMISSION_REQUEST_CODE);

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
      int[] grantResults) {
    if (requestCode == PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0) {

        boolean audioAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

        if (audioAccepted && cameraAccepted) {
          Toast.makeText(getApplicationContext(), "Permission granted",
              Toast.LENGTH_SHORT).show();
          loadFragment(new QuestionsFragment());
        } else {
          Toast.makeText(getApplicationContext(), "Permission Denied",
              Toast.LENGTH_SHORT).show();

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(RECORD_AUDIO)) {
              showMessageOKCancel("Please allow to use mic",
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      requestPermissions(new String[] { RECORD_AUDIO },
                          PERMISSION_REQUEST_CODE);
                    }
                  });
            } else if (shouldShowRequestPermissionRationale(CAMERA)) {
              showMessageOKCancel("Please allow to use camera",
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      requestPermissions(new String[] { CAMERA },
                          PERMISSION_REQUEST_CODE);
                    }
                  });
            } else {
              showMessageOKCancel("Please allow to use camera and mic",
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      requestPermissions(new String[] { CAMERA, RECORD_AUDIO },
                          PERMISSION_REQUEST_CODE);
                    }
                  });
            }
          }
        }
      }
    }
  }

  private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(MainActivity.this)
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .create()
        .show();
  }
}
