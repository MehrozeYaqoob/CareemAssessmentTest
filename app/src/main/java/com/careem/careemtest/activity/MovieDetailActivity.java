package com.careem.careemtest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.careem.careemtest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mehroze on 12/13/2017.
 */

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.title) TextView tvTitle;
    @BindView(R.id.overview) TextView tvOverView  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_detail);
        ButterKnife.bind(this);

        String title = getIntent().getStringExtra("title");
        String overView = getIntent().getStringExtra("overview");

        tvTitle.setText(title);
        tvOverView.setText(overView);

    }
}
