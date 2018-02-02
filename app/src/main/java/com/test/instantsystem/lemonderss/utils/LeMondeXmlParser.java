package com.test.instantsystem.lemonderss.utils;

import android.util.Xml;

import com.test.instantsystem.lemonderss.models.News;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexis Migueres on 28/01/2018.
 */

public class LeMondeXmlParser {
    // no namespace used
    private static final String ns = XmlPullParser.NO_NAMESPACE;

    /**
     *
     * @param in
     * @return List of News
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static List<News> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    /**
     *
     * @param parser from le monde RSS
     * @return List of News
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static List<News> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        // Instantiate list of news we will populate
        List<News> newsList = new ArrayList<>();

        int eventType = parser.getEventType();
        boolean done = false;
        News news = null;

        try {
            // Loop through the stream
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(Constants.ITEM_TAG)) {
                            newsList.add(readItem(parser));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(Constants.CHANNEL_TAG)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Sorting in decreasing order by pubDate
        Collections.sort(newsList, Collections.reverseOrder());

        return newsList;
    }

    /**
     * Parses the contents of news. If it encounters an  hands them off
     * to their respective "read" methods for processing.
     *
     * @param parser
     * @return News
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static News readItem(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        String title = null, description = null, link = null, imageSrc = null;
        Date pubDate = null;

        // pubDate format
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_PATTERN, Locale.ENGLISH);

        int eventType = parser.getEventType();
        try {
            while (eventType != XmlPullParser.END_TAG || !parser.getName().equalsIgnoreCase(Constants.ITEM_TAG)) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    eventType = parser.next();
                    continue;
                }
                String name = parser.getName();
                switch (name) {
                    case Constants.TITLE_TAG:
                        title = readValue(parser, Constants.TITLE_TAG);
                        break;
                    case Constants.DESCRIPTION_TAG:
                        description = readValue(parser, Constants.DESCRIPTION_TAG);
                        break;
                    case Constants.LINK_TAG:
                        link = readValue(parser, Constants.LINK_TAG);
                        break;
                    case Constants.PUB_DATE_TAG:
                        pubDate = sdf.parse(readValue(parser, Constants.PUB_DATE_TAG));
                        break;
                    case Constants.ENCLOSURE_TAG:
                        imageSrc = readUrlAttribute(parser);
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new News(title, description, link, imageSrc, pubDate);
    }

    /**
     *
     * Processes text tags in the feed.
     *
     * @param parser
     * @param tagName
     * @return value string
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static String readValue(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        String value = "";
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        if (parser.next() == XmlPullParser.TEXT) {
            value = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return value;
    }

    /**
     * Processes enclosure tags in the feed.
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static String readUrlAttribute(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, Constants.ENCLOSURE_TAG);
        return parser.getAttributeValue(ns, Constants.URL_TAG);
    }
}
