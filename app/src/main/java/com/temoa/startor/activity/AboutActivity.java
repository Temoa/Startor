package com.temoa.startor.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.temoa.startor.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout cToolbarLayout
                = (CollapsingToolbarLayout) findViewById(R.id.about_collapsing_layout);
        cToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        TextView introductionTv = (TextView) findViewById(R.id.about_text_introduction);
        String introduction = "        " + getResources().getString(R.string.introduction);
        introductionTv.setText(introduction);

        TextView myWordTv = (TextView) findViewById(R.id.about_content_text_my_word);
        String myWord = "        " + getResources().getString(R.string.my_word) + "\n" +
                "        " + getResources().getString(R.string.copyright);
        myWordTv.setText(myWord);

        findViewById(R.id.about_content_text_boss).setOnClickListener(this);
        findViewById(R.id.about_content_text_girl).setOnClickListener(this);
        findViewById(R.id.about_content_text_boy).setOnClickListener(this);
        findViewById(R.id.about_content_text_me).setOnClickListener(this);

    }

    private void open(String url) {
        Uri uri = Uri.parse(url);
        Intent coinIntent = new Intent("android.intent.action.VIEW");
        coinIntent.setData(uri);
        startActivity(coinIntent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.about_content_text_boss:
                open("http://www.weibo.com/344530832");
                break;
            case R.id.about_content_text_girl:
                open("http://www.weibo.com/wuxymsn");
                break;
            case R.id.about_content_text_boy:
                open("http://weibo.com/p/1005055935748466?is_hot=1");
                break;
            case R.id.about_content_text_me:
                open("http://github.com/Temoa");
                break;
        }
    }
}
