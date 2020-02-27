package com.example.blog.controller.ui.allBlogs;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.blog.MainActivity;
import com.example.blog.R;
//import com.example.blog.controller.notification.MyNotificationPublisher;
import com.example.blog.controller.notification.NotificationUtils;

public class AllBlogsFragment extends Fragment {
    NotificationManager notificationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//
       View root = inflater.inflate(R.layout.fragment_all_blogs, container, false);
//

//
//

////        notification publisher test
//                int delay=1000;
//                //
//                Intent goToIntent = new Intent(getContext(), MainActivity.class);
//                PendingIntent goToPendingIntent = PendingIntent.getActivity(getContext(),
//                        0, goToIntent, 0);
//                //
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), NotificationUtils.CHANNEL1_ID)
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentTitle("title")
//                        .setDefaults(Notification.DEFAULT_SOUND)
//                        .setContentText("notification details with delay="+delay)
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                        .setContentIntent(goToPendingIntent)
//                        .setAutoCancel(true);
//                Notification notification=builder.build();
//
//
//
//                Intent notificationIntent = new Intent( getContext(), MyNotificationPublisher. class ) ;
//                notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
//                notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
//                PendingIntent pendingIntent = PendingIntent.getBroadcast ( getContext(), 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
//                long futureInMillis = SystemClock. elapsedRealtime () + delay ;
//                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context. ALARM_SERVICE ) ;
//                assert alarmManager != null;
//                alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
//
//
//



//
        return root;
    }
}