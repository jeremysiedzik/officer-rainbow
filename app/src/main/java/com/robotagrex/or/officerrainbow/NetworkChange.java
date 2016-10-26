package com.robotagrex.or.officerrainbow;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

public class NetworkChange extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        if(isConnected(context)) {
            Toast.makeText(context, "Connected.", Toast.LENGTH_LONG).show();
            jobrotate(context);
        }

        else {
            Toast.makeText(context, "Lost connection.", Toast.LENGTH_LONG).show();
        }

    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null) &&
                activeNetwork.isConnected();
    }

    private void jobrotate(Context context) {
        JobScheduler mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder( 1,
                new ComponentName( context.getPackageName(), JobSchedulerServiceAlarm.class.getName() ) );

                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        if( mJobScheduler.schedule( builder.build() ) <= 0 ) {
            Toast toast2= Toast.makeText(context.getApplicationContext(),
                    "JobService jobrotate in NetworkChange.java task is broken", Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast2.show();
        }

        mJobScheduler.cancelAll();
    }

}