package by.iit.rssreader;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import by.iit.rssreader.model.RssFeed;

public class RetrieveFeedTask extends AsyncTask<String, Void, List<RssFeed>> {

    private MainActivity activity;

    public RetrieveFeedTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected List<RssFeed> doInBackground(String... urls) {
        Log.d("empty", "doInBackground: retrieving RSS");
        List<RssFeed> feed = new ArrayList<>();
        try {
            URL rssUrl = new URL(urls[0].startsWith("http") ? urls[0] : "http://" + urls[0]);
            HttpURLConnection conn = (HttpURLConnection) rssUrl.openConnection();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document document = db.parse(is);
                Element element = document.getDocumentElement();

                //take rss nodes to NodeList
                NodeList nodeList = element.getElementsByTagName("item");

                if (nodeList.getLength() > 0) {
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Element entry = (Element) nodeList.item(i);

                        Element titleE = (Element) entry.getElementsByTagName("title").item(0);
                        Element descriptionE = (Element) entry.getElementsByTagName("description").item(0);
                        Element pubDateE = (Element) entry.getElementsByTagName("pubDate").item(0);
                        Element linkE = (Element) entry.getElementsByTagName("link").item(0);

                        String title = titleE.getFirstChild().getNodeValue();
                        String description = descriptionE.getFirstChild().getNodeValue();
                        LocalDateTime pubDate = LocalDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(pubDateE.getFirstChild().getNodeValue()));
                        String link = linkE.getFirstChild().getNodeValue();

                        RssFeed rssItem = new RssFeed();
                        rssItem.setTitle(title);
                        rssItem.setDescription(description);
                        rssItem.setPubDate(pubDate);
                        rssItem.setLink(link);

                        feed.add(rssItem);
                    }
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return feed;
    }

    @Override
    protected void onPostExecute(List<RssFeed> rssFeeds) {
        activity.setFeed(rssFeeds);
    }
}