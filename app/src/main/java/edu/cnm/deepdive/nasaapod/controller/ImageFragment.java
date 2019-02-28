package edu.cnm.deepdive.nasaapod.controller;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.nasaapod.ApodApplication;
import edu.cnm.deepdive.nasaapod.BuildConfig;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.controller.DateTimePickerFragment.Mode;
import edu.cnm.deepdive.nasaapod.controller.DateTimePickerFragment.OnChangeListener;
import edu.cnm.deepdive.nasaapod.model.Apod;
import edu.cnm.deepdive.nasaapod.service.ApodService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageFragment extends Fragment {

  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final String CALENDAR_KEY = "calendar_ms";
  private static final String APOD_KEY = "apod";

  private WebView webView;
  private String apiKey;
  private ProgressBar loading;
  private ApodService service;
  private Apod apod;
  private Calendar calendar;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    setRetainInstance(true);
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_image, container, false);
    setupWebView(view);
    setupDatePicker(view);
    setupService();
    calendar = Calendar.getInstance();
    if (savedInstanceState != null) {
      long savedMillis = savedInstanceState.getLong(CALENDAR_KEY, calendar.getTimeInMillis());
      calendar.setTimeInMillis(savedMillis);
      apod = (Apod) savedInstanceState.getSerializable(APOD_KEY);
    }
    if (apod != null) {
      loading.setVisibility(View.VISIBLE);//TODO only is visible
      webView.loadUrl(apod.getUrl());
    } else {
      new ApodTask().execute(calendar.getTime());
    }
    return view;
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.options, menu);

  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.info) {
      showInfo();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putLong(CALENDAR_KEY, calendar.getTimeInMillis());
    outState.putSerializable(APOD_KEY, apod);
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void setupWebView(View view) {
    webView = view.findViewById(R.id.web_view);
    loading = view.findViewById(R.id.loading);
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        loading.setVisibility(View.GONE);
        if (isVisible()) {
          showInfo();
        }
      }
    });
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
  }

  private void showInfo() {
    if (apod != null) {
      Toast.makeText(getContext(), apod.getTitle(), Toast.LENGTH_LONG).show();
    }
  }

  private void setupDatePicker(View view) {
    FloatingActionButton jumpDate = view.findViewById(R.id.jump_date);
    jumpDate.setOnClickListener(v -> {
      DateTimePickerFragment picker = new DateTimePickerFragment();
      picker.setMode(Mode.DATE);
      picker.setCalendar(calendar);
      picker.setListener((cal) -> new ApodTask().execute(cal.getTime()));
      picker.show(getFragmentManager(), picker.getClass().getSimpleName());
    });
  }

  private void setupService() {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .setDateFormat(DATE_FORMAT)
        .create();
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    service = retrofit.create(ApodService.class);
    apiKey = BuildConfig.API_KEY;
  }

  private class ApodTask extends AsyncTask<Date, Void, Apod> {

    private static final int BUFFER_SIZE = 4096;
    private static final String IMAGE_MEDIA_TYPE = "image";

    @Override
    protected void onPreExecute() {
      loading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Apod apod) {
      ImageFragment.this.apod = apod;
      String url = apod.getUrl();
      if (apod.getMediaType().equals(IMAGE_MEDIA_TYPE)) {
        url = urlFromFilename(filenameFromUrl(url));
      }
      webView.loadUrl(apod.getUrl());
    }

    @Override
    protected void onCancelled(Apod apod) {
      loading.setVisibility(View.GONE);
      Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Apod doInBackground(Date... dates) {
      Apod apod = loadFromDatabase(dates[0]);
      try {
        if (apod == null) {
          @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat(DATE_FORMAT);
          Response<Apod> response = service.get(apiKey, format.format(dates[0])).execute();
          if (response.isSuccessful()) {
            apod = response.body();
            ApodApplication.getInstance().getDatabase().getApodDao().insert(apod);
            calendar.setTime(dates[0]);
          }
        } else {
          calendar.setTime(dates[0]);
        }
        if (apod != null
            && apod.getMediaType().equals(IMAGE_MEDIA_TYPE)
            && !fileExists(filenameFromUrl(apod.getUrl()))) {
          saveImage(apod);
        }
      } catch (IOException e) {
        Log.e(getClass().getSimpleName(), e.toString());
      } finally {
        if (apod == null) {
          cancel(true);
        }
      }
      return apod;
    }

    private Apod loadFromDatabase(Date date) {
      Date dateOnly = new Date(date.getYear(), date.getMonth(), date.getDate());
      List<Apod> records =
          ApodApplication.getInstance().getDatabase().getApodDao().find(dateOnly);
      return (records.size() > 0) ? records.get(0) : null;
    }

    private String filenameFromUrl(String url) {
      try {
        URI uri  = new URL(url).toURI();
        String[] parts = uri.getPath().split("/");
        return parts[parts.length - 1];
      } catch (URISyntaxException | MalformedURLException e) {
        Log.e(getClass().getSimpleName(), e.toString());
        return null;
      }
    }

    private void saveImage(Apod apod) throws IOException {
      URL url = new URL(apod.getUrl());
      String filename = filenameFromUrl(apod.getUrl());
      URLConnection connection = url.openConnection();
      try (
          OutputStream output = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
          InputStream input = connection.getInputStream();
      ) {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) > -1) {
          output.write(buffer, 0, bytesRead);
        }
      }
    }

    private String urlFromFilename(String filename) {
      return "file://" + new File(getContext().getFilesDir(), filename).toString();
    }

    private boolean fileExists(String filename) {
      return new File(getContext().getFilesDir(), filename).exists();
    }

  }

}
