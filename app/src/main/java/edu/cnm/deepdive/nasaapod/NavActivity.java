package edu.cnm.deepdive.nasaapod;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemReselectedListener;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class NavActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {





  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nav);


    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(this);
  }
  @Override

public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){

    boolean handled = true;

    switch(menuItem.getItemId()){
      case R.id.navigation_image:

        //TODO LOAD IMAGE DISPLAY FRAGMENT
        break;
      case R.id.navigation_history:

        // TODO
        break;
      default:
        handled = false;
    }
    return handled;
  }
}
