package com.robotagrex.or.officerrainbow;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

class HandleXML  {

    private String title = "title";
    private String link = "link";
    private String description = "description";
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    volatile boolean parsingComplete = true;

    HandleXML(String url){
        this.urlString = url;
    }

    public String getTitle(){
        return title;
    }

    String getLink(){
        return link;
    }

    String getDescription(){
        return description;
    }


    private void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        switch (name) {
                            case "title":
                                title = text;
                                break;
                            case "link":
                                link = text;
                                break;
                            case "description":
                                description = text;
                                break;
                            default:
                                break;
                        }

                        break;
                }

                event = myParser.next();
            }

            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //void fetchXML(){
        //Thread thread = new Thread(new Runnable(){

            //@Override
            //public void run() {
            //}

    class asyncxml extends AsyncTask<Void, Void, Void> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        URL url = new URL(urlString);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        conn.setReadTimeout(10000 /* milliseconds */);
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);

                        // Starts the query
                        conn.connect();
                        InputStream stream = conn.getInputStream();

                        xmlFactoryObject = XmlPullParserFactory.newInstance();
                        XmlPullParser myparser = xmlFactoryObject.newPullParser();

                        myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                        myparser.setInput(stream, null);

                        parseXMLAndStoreIt(myparser);
                        stream.close();
                    }

                    catch (XmlPullParserException | IOException a) {
                        a.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {

                }
            }
      //  });
      //  thread.start();
    //}

}