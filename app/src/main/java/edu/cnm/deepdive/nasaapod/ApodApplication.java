package edu.cnm.deepdive.nasaapod;

import android.app.Application;
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
}
