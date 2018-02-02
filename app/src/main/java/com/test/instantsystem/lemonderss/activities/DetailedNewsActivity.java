package com.test.instantsystem.lemonderss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.instantsystem.lemonderss.R;
import com.test.instantsystem.lemonderss.models.News;
import com.test.instantsystem.lemonderss.utils.Constants;

public class DetailedNewsActivity extends AppCompatActivity {

    private ImageView mDetailedNewsImage;
    private TextView mDetailedNewsTitle;
    private TextView mDetailedNewsDescription;
    private TextView mDetailedNewsDate;
    private TextView mDetailedNewsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        // Recover news
        Intent intent = getIntent();
        News news = (News) intent.getSerializableExtra(Constants.NEWS_INTENT_TAG);

        // Image
        mDetailedNewsImage = (ImageView) findViewById(R.id.detailed_news_image);
        Picasso.with(this).load(news.getImageSrc()).into(mDetailedNewsImage);

        // Title
        mDetailedNewsTitle = (TextView) findViewById(R.id.detailed_news_title);
        mDetailedNewsTitle.setText(news.getTitle());

        // Date in french
        mDetailedNewsDate = (TextView) findViewById(R.id.detailed_news_date);
        mDetailedNewsDate.setText(news.getLocalDate());

        // Description
        mDetailedNewsDescription = (TextView) findViewById(R.id.detailed_news_description);
        mDetailedNewsDescription.setText(news.getDescription());

        // Source Url
        mDetailedNewsUrl = (TextView) findViewById(R.id.detailed_news_url);
        String sourceUrl = getString(R.string.source_url, news.getLink());
        mDetailedNewsUrl.setMovementMethod(LinkMovementMethod.getInstance());
        mDetailedNewsUrl.setText(Html.fromHtml(sourceUrl));
    }

}
