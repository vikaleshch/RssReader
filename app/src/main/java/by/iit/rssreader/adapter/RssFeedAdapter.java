package by.iit.rssreader.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import by.iit.rssreader.R;
import by.iit.rssreader.ShowRssActivity;
import by.iit.rssreader.model.RssFeed;

public class RssFeedAdapter extends RecyclerView.Adapter<RssFeedAdapter.RssFeedViewHolder> {

    private List<RssFeed> feed;

    public RssFeedAdapter(List<RssFeed> feed) {
        this.feed = feed;
    }

    @NonNull
    @Override
    public RssFeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row, viewGroup, false);
        RssFeedViewHolder holder = new RssFeedViewHolder(itemView);

        itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(v.getContext(), ShowRssActivity.class);
            intent.putExtra("rss", feed.get(holder.getAdapterPosition()));
            v.getContext().startActivity(intent);
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RssFeedViewHolder rssFeedViewHolder, int i) {
        RssFeed rss = feed.get(i);
        rssFeedViewHolder.title.setText(rss.getTitle());
        rssFeedViewHolder.description.setText(Html.fromHtml(rss.getDescription(), 1));
    }

    @Override
    public int getItemCount() {
        return feed.size();
    }

    public static class RssFeedViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;

        public RssFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }
}
