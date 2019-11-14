package com.sonusourav.merazoverflow;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sonusourav.merazoverflow.fragment.ProfileFragment;
import com.sonusourav.merazoverflow.fragment.QuestionsFragment;
import com.sonusourav.merazoverflow.fragment.TagsFragment;
import com.sonusourav.merazoverflow.fragment.UsersFragment;
import com.sonusourav.merazoverflow.helper.BottomNavigationBehavior;

public class MainActivity extends AppCompatActivity {

  private ActionBar toolbar;
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
        case R.id.navigation_tags:
          toolbar.setTitle("Tags");
          fragment = new TagsFragment();
          loadFragment(fragment);
          return true;
        case R.id.navigation_users:
          toolbar.setTitle("Users");
          fragment = new UsersFragment();
          loadFragment(fragment);
          return true;
        case R.id.navigation_profile:
          toolbar.setTitle("Profile");
          fragment = new ProfileFragment();
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
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    // attaching bottom sheet behaviour - hide / show on scroll
    CoordinatorLayout.LayoutParams layoutParams =
        (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
    layoutParams.setBehavior(new BottomNavigationBehavior());

    toolbar.setTitle("Questions");
    loadFragment(new QuestionsFragment());
  }

  /**
   * loading fragment into FrameLayout
   */
  private void loadFragment(Fragment fragment) {
    // load fragment
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.frame_container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
