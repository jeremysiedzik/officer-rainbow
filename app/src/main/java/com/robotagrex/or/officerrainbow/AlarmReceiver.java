package com.robotagrex.or.officerrainbow;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "I'm running Jer! Look at me run!!", Toast.LENGTH_LONG).show();
    }
}