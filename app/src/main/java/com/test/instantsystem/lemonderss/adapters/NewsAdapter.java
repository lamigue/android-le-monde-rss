package com.test.instantsystem.lemonderss.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.instantsystem.lemonderss.R;
import com.test.instantsystem.lemonderss.activities.DetailedNewsActivity;
import com.test.instantsystem.lemonderss.models.News;
import com.test.instantsystem.lemonderss.utils.Constants;

import java.util.List;

/**
 * Created by Alexis Migueres on 28/01/2018.
 */


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<News> newsList;

    public NewsAdapter(){
    }

    public NewsAdapter(Context context, List newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list, parent, false);

        return new ViewHolder(v, context, newsList);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.title.setText(news.getTitle());
        Picasso.with(getContext()).load(news.getImageSrc()).into(holder.imageSrc);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView imageSrc;

        public ViewHolder(View view, Context ctx, List<News> newsList) {
            super(view);
            // get the Activity Context
            context = ctx;

            view.setOnClickListener(this);

            title = (TextView) view.findViewById(R.id.news_title);
            imageSrc = (ImageView) view.findViewById(R.id.news_image);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            News news = newsList.get(position);
            Intent intent = new Intent(context, DetailedNewsActivity.class);
            intent.putExtra(Constants.NEWS_INTENT_TAG, news);
            context.startActivity(intent);
        }
    }
}
