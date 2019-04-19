package by.iit.rssreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import by.iit.rssreader.adapter.RssFeedAdapter;
import by.iit.rssreader.model.RssFeed;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    loadRss(s);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
        loadRss("news.tut.by/rss");

        return true;
    }

    private void loadRss(String url) {
        try {
            new RetrieveFeedTask(this).execute(url);
        } catch (Exception e) {
            Toast.makeText(this, "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void setFeed(List<RssFeed> feed) {
        RecyclerView noteRecycler = findViewById(R.id.feed);
        RssFeedAdapter adapter = new RssFeedAdapter(feed);
        noteRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        noteRecycler.setItemAnimator(new DefaultItemAnimator());
        noteRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
