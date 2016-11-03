package com.temoa.startor2.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.temoa.startor2.Constants;
import com.temoa.startor2.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_about));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.about_boss).setOnClickListener(this);
        findViewById(R.id.about_girl).setOnClickListener(this);
        findViewById(R.id.about_boy).setOnClickListener(this);
        findViewById(R.id.about_me).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.about_boss:
                open(Constants.BOSS_WEIBO);
                break;
            case R.id.about_girl:
                open(Constants.GIRL_WEIBO);
                break;
            case R.id.about_boy:
                open(Constants.BOY_WEIBO);
                break;
            case R.id.about_me:
                open(Constants.MY_GETHUB);
                break;
        }
    }

    private void open(String url) {
        Uri uri = Uri.parse(url);
        Intent coinIntent = new Intent("android.intent.action.VIEW");
        coinIntent.setData(uri);
        startActivity(coinIntent);
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }
}
