package com.test.instantsystem.lemonderss.utils;

/**
 * Created by Alexis Migueres on 28/01/2018.
 */

public final class Constants {
    // Timeout arbitrarily set to 8000ms.
    public static final int NETWORK_TIMEOUT = 8000;

    // rss tags
    public static final String ITEM_TAG = "item";
    public static final String CHANNEL_TAG = "channel";
    public static final String TITLE_TAG = "title";
    public static final String DESCRIPTION_TAG = "description";
    public static final String LINK_TAG = "link";
    public static final String PUB_DATE_TAG = "pubDate";
    public static final String ENCLOSURE_TAG = "enclosure";
    public static final String URL_TAG = "url";

    // intent tags
    public static final String NETWORK_INTENT_TAG = "NetworkFragment";
    public static final String URL_INTENT_TAG = "UrlIntentKey";
    public static final String NEWS_INTENT_TAG = "NewsIntentTag";
    public static final String NEWS_LIST_INTENT_TAG = "NewsListTag";

    // RSS feed
    public static final String LE_MONDE_URL = "http://www.lemonde.fr/rss/une.xml";

    //
    public static final String DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss Z";

}
