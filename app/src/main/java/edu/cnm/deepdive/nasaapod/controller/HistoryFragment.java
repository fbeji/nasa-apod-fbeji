package edu.cnm.deepdive.nasaapod.controller;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.cnm.deepdive.nasaapod.ApodApplication;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.model.Apod;
import edu.cnm.deepdive.nasaapod.view.HistoryAdapter;
import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {


  private RecyclerView historyView;
  private List<Apod> history;

  private HistoryAdapter adapter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_history, container, false);

    historyView = view.findViewById(R.id.history_view);
    history = new ArrayList<>();
    adapter = new HistoryAdapter(getContext(), history);
    historyView.setAdapter(adapter);

    new ApodQuery().execute();


    return view;
  }



  @Override
  public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    if (!hidden) {
      new ApodQuery().execute();
    }
  }

  private class ApodQuery extends AsyncTask<Void, Void, List<Apod>> {


    @Override
    protected List<Apod> doInBackground(Void... voids) {
      return ApodApplication.getInstance().getDatabase().getApodDao().list();
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Apod> apods) {

    history.clear();
    history.addAll(apods);
    adapter.notifyDataSetChanged();

    }

    @Override
    protected void onCancelled(List<Apod> apods) {
      super.onCancelled(apods);
    }
  }

  ;
}
