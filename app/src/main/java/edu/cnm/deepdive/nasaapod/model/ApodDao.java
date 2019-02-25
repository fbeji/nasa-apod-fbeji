package edu.cnm.deepdive.nasaapod.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.Date;
import java.util.List;



@Dao
public interface ApodDao {

  @Insert
  long insert(Apod apod);

  @Query("SELECT * FROM Apod WHERE date = :date")
  List<Apod> find(Date date);

  @Delete
  int delete(Apod apod);

}

