package edu.cnm.deepdive.nasaapod;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.nasaapod.model.ApodDB;

public class ApodApplication extends Application {

  private static ApodApplication instance = null;
  private ApodDB database;



  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    Stetho.initializeWithDefaults(this);
    database = ApodDB.getInstance(this);


  }

  @Override
  public void onTerminate() {
    ApodDB.forgetInstance();
    super.onTerminate();
  }


  public static ApodApplication getInstance(){

    return instance;
  }

  public ApodDB getDatabase(){

    return database;
  }
  public void loadFragment(FragmentActivity activity, int containerId, Fragment fragment, String tag,boolean visible) {

    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
    transaction.add(containerId, fragment, tag);
    if(!visible){

      transaction.hide(fragment);
    }

    transaction.commit();

  }

  public void showFragment (FragmentActivity activity, int containerId, Fragment fragment ){

    FragmentManager manager = activity.getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    boolean modified = false;
    for(Fragment frag : manager.getFragments()){

      if(frag.getId() == containerId) {

        if(frag == fragment && !frag.isVisible()){
          transaction.show(frag);
          modified = true;
        } else if (frag != fragment && frag.isVisible()){

          transaction.hide(frag);
          modified = true;
        }
      }
          }

    if(modified){
      transaction.commit();
    }

  }

  public Fragment findFragment(FragmentActivity activity, int containerId, String tag ){

    FragmentManager manager = activity.getSupportFragmentManager();

    Fragment fragment = manager.findFragmentByTag(tag);
    return (fragment != null && fragment.getId() == containerId) ? fragment:null;


  }
}
