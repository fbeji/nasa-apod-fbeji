package edu.cnm.deepdive.nasaapod.model;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import edu.cnm.deepdive.nasaapod.model.ApodDB.Converters;
import edu.cnm.deepdive.nasaapod.model.dao.ApodDao;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import java.util.Date;

@Database(

    entities = {Apod.class},
    version = 1,
    exportSchema = true
)

@TypeConverters(Converters.class)

public abstract class ApodDB extends RoomDatabase {

  // object that gives the database itself

  private static final String DB_NAME = "apod_db";

  private static ApodDB instance = null;

  public synchronized static ApodDB getInstance(Context context) {

    if (instance == null) {

      instance = Room.databaseBuilder(context.getApplicationContext(), ApodDB.class, DB_NAME)
          .build();
    }

    return instance;

  }

  public synchronized static void forgetInstance() {

    instance = null;
  }

  public abstract ApodDao getApodDao();

  public static class Converters {

    @TypeConverter
    public static Date dateFromLong(Long time) {
      return (time != null) ? new Date(time) : null;

    }

    @TypeConverter
    public static long longFromDate(Date date) {

      return (date != null) ? date.getTime() : null;
    }


  }


}
