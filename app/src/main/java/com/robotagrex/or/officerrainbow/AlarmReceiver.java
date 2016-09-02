package com.robotagrex.or.officerrainbow;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.widget.Toast;

        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;

        import java.io.IOException;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = "http://ec2-52-42-215-71.us-west-2.compute.amazonaws.com/aggregate.html";
        // String title;
        try {
            // Connect to the web site
            Document document = Jsoup.connect(url).get();
            // Get the html document title
            // title = document.title();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
    }
}