package com.robotagrex.or.officerrainbow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.robotagrex.or.officerrainbow.NotifyAlarmReceiver;

/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file. When the user sets the alarm, the receiver is enabled.
 * When the user cancels the alarm, the receiver is disabled, so that rebooting the
 * device will not trigger this receiver.
 */
// BEGIN_INCLUDE(autostart)
public class NotifyBootReceiver extends BroadcastReceiver {
    NotifyAlarmReceiver alarm = new NotifyAlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setAlarm(context);
        }
    }
}
//END_INCLUDE(autostart)
