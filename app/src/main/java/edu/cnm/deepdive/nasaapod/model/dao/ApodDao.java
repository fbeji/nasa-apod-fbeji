package edu.cnm.deepdive.nasaapod.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import java.util.Date;
import java.util.List;

@Dao
public interface ApodDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  long insert(Apod apod);

  @Query("SELECT * FROM Apod WHERE date = :date")
  List<Apod> find(Date date);
  @Query("SELECT * FROM Apod ORDER BY date DESC")
  List<Apod> list();


  @Delete
  int delete(Apod apod);

}


