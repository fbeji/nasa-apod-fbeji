package edu.cnm.deepdive.nasaapod.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import edu.cnm.deepdive.nasaapod.ApodApplication;
import edu.cnm.deepdive.nasaapod.R;

public class NavActivity extends AppCompatActivity
    implements OnNavigationItemSelectedListener {

 private Fragment imageFragment;
 private Fragment historyFragment;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nav);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(this);

    ApodApplication application = ApodApplication.getInstance();

    if (savedInstanceState == null) {
      imageFragment = new ImageFragment();
      ApodApplication.getInstance().loadFragment(
          this, R.id.fragment_container, imageFragment, imageFragment.getClass().getSimpleName(),true);

      historyFragment = new HistoryFragment();
      ApodApplication.getInstance().loadFragment(this, R.id.fragment_container, historyFragment,
          historyFragment.getClass().getSimpleName(), false);
    }else {
      imageFragment = application.findFragment(
          this,R.id.fragment_container,ImageFragment.class.getSimpleName());
      historyFragment = application.findFragment(
          this,R.id.fragment_container,HistoryFragment.class.getSimpleName());

    }
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    boolean handled = true;
    switch (menuItem.getItemId()) {
      case R.id.navigation_image:
        ApodApplication.getInstance().showFragment(this, R.id.fragment_container, imageFragment);
        break;
      case R.id.navigation_history:
        ApodApplication.getInstance().showFragment(this, R.id.fragment_container, historyFragment);
        break;
      default:
        handled = false;
    }
    return handled;
  }

}
