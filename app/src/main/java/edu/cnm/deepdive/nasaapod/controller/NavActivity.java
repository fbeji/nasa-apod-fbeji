package edu.cnm.deepdive.nasaapod.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import edu.cnm.deepdive.nasaapod.ApodApplication;
import edu.cnm.deepdive.nasaapod.HistoryFragment;
import edu.cnm.deepdive.nasaapod.R;

public class NavActivity extends AppCompatActivity
    implements OnNavigationItemSelectedListener {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nav);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(this);
    if (savedInstanceState == null) {
      Fragment fragment = new ImageFragment();
      ApodApplication.getInstance().loadFragment(
          this, fragment, fragment.getClass().getSimpleName());
    }
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    boolean handled = true;
    switch (menuItem.getItemId()) {
      case R.id.navigation_image:
        // TODO Load image display fragment.
        break;
      case R.id.navigation_history:
        Fragment fragment = new HistoryFragment();
        ApodApplication.getInstance().loadFragment(
            this, fragment, fragment.getClass().getSimpleName());
        break;
      default:
        handled = false;
    }
    return handled;
  }

}
