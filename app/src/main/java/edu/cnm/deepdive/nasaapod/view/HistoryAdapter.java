package edu.cnm.deepdive.nasaapod.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.cnm.deepdive.nasaapod.ApodApplication;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.controller.HistoryFragment;
import edu.cnm.deepdive.nasaapod.controller.ImageFragment;
import edu.cnm.deepdive.nasaapod.controller.NavActivity;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import java.text.DateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder>
    implements View.OnClickListener {

  private Context context;
  private HistoryFragment historyFragment;
  private List<Apod> items;

  public HistoryAdapter(HistoryFragment historyFragment, List<Apod> items) {
    this.context = historyFragment.getContext();
    this.historyFragment = historyFragment;
    this.items = items;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(context).inflate(R.layout.history_item, viewGroup, false);
    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int i) {
    holder.bind();
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  @Override
  public void onClick(View v) {
    Apod apod = (Apod) v.getTag();
    ImageFragment imageFragment = historyFragment.getImageFragment();
    NavActivity activity = (NavActivity) historyFragment.getActivity();
    ApodApplication.getInstance().showFragment(activity, R.id.fragment_container, imageFragment);
    imageFragment.setApod(apod);
    activity.getNavigation().setSelectedItemId(R.id.navigation_image);
  }

  public class Holder extends RecyclerView.ViewHolder {

    // Implement a click listener for the views.

    private final DateFormat format = android.text.format.DateFormat.getDateFormat(context);

    private Apod apod;
    private View view;
    private TextView dateView;
    private TextView titleView;

    public Holder(@NonNull View itemView) {
      super(itemView);
      view = itemView;
      view.setOnClickListener(HistoryAdapter.this);
      dateView = itemView.findViewById(R.id.date_view);
      titleView = itemView.findViewById(R.id.title_view);
    }

    private void bind() {
      apod = items.get(getAdapterPosition());
      view.setTag(apod);
      dateView.setText(format.format(apod.getDate()));
      titleView.setText(apod.getTitle());
    }

  }

}
