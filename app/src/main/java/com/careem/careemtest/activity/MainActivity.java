package com.careem.careemtest.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.careem.careemtest.R;
import com.careem.careemtest.adapter.MoviesAdapter;
import com.careem.careemtest.listener.ClickListener;
import com.careem.careemtest.listener.RecyclerTouchListener;
import com.careem.careemtest.model.Movie;
import com.careem.careemtest.model.MoviesResponse;
import com.careem.careemtest.network.ApiClient;
import com.careem.careemtest.network.ApiInterface;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "c734b5a805ced2e66d23dd54e61615de";
    private List<Movie> movies;
    private Button dateFilterBtn;
    private int mYear, mMonth, mDay;

    private  String NOW_PLAYING = "Now_Playing";
    private  String DATE_FILTER = "Date_Filter";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if Key is Not Empty
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "API Key is Empty", Toast.LENGTH_LONG).show();
            return;
        }

        recyclerView =  findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        dateFilterBtn = findViewById(R.id.btn_date_filter);
        dateFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateFilterBtn.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        // This will call the API using retrofit
                        populateRecyclerView(apiService,DATE_FILTER, dateFilterBtn.getText().toString());
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });



        // This will call the API using retrofit
        populateRecyclerView(apiService,NOW_PLAYING, null);

        // Adding Click Listener to Movie Item
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movies.get(position);

                // lets navigate to movie detail view
                launchDetailView(movie);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

        /*
        * This methods is invoked when an Item on Recycler View is clicked
        * This will navigate user to its detail view
        * */
    private  void launchDetailView(Movie movie)
    {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(MainActivity.this, MovieDetailActivity.class);

        i.putExtra("title", movie.getTitle());
        i.putExtra("overview", movie.getOverview());
        startActivity(i);
    }

    /*
     * This Method will populate the recycler view on the basis of requested API*/
    private  void populateRecyclerView(ApiInterface apiService,String API, String dateFilter)
    {
        retrofit2.Call<MoviesResponse> call;

        if(API.equalsIgnoreCase("Now_Playing"))
        {
            call = apiService.getNowPlayingMovies(API_KEY);
        }
        else
        {
            call = apiService.getDateWiseFilteredMovies(API_KEY,dateFilter);
        }


        // Getting Callback of API
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                //int statusCode = response.code();
                movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }
}

