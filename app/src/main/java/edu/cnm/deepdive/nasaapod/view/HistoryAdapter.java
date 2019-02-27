package edu.cnm.deepdive.nasaapod.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.model.Apod;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder>{

  private Context context;

  private List<Apod> items;

  public HistoryAdapter(Context context, List<Apod> items) {
    this.context = context;
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

  public class Holder extends RecyclerView.ViewHolder {

    private Apod apod;
    private TextView dateView;
    private TextView titleView;
    private final DateFormat format = android.text.format.DateFormat.getDateFormat(context);

    public Holder(@NonNull View itemView) {
      super(itemView);

      dateView = itemView.findViewById(R.id.date_view);
      titleView = itemView.findViewById(R.id.title_view);
    }
    private void bind(){


      apod = items.get(getAdapterPosition());
      dateView.setText(format.format(apod.getDate()));
      titleView.setText(apod.getTitle());


    }
  }
}
