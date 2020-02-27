//package com.example.blog.controller.notification;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//
//import com.example.blog.MainActivity;
//import com.example.blog.R;
//
//
//public class ForegroundService extends Service {
//
//
//    public android.os.Handler handler  = new Handler();
//    public static Runnable runnable = null;
//
////    public static final String CHANNEL_ID = "ForegroundServiceChannel";
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        String input = intent.getStringExtra("inputExtra");
//        NotificationUtils nu=new NotificationUtils(getApplicationContext());
//        nu.createChannels();
//
////        createNotificationChannel();
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);
//        final Notification notification = new NotificationCompat.Builder(this, nu.CHANNEL1_ID)
//                .setContentTitle("Foreground Service")
//                .setContentText(input)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentIntent(pendingIntent)
//                .build();
//        runnable = new Runnable() {
//            public void run() {
//                Log.e("Service", "Service is still running!");
//                Toast.makeText(getApplicationContext(), "Service is still running", Toast.LENGTH_SHORT).show();
////                handler.postDelayed(runnable, 50000);
//                startForeground(1, notification);
//            }
//        }; handler.postDelayed(runnable, 1500);
//
////        startForeground(1, notification);
//        //do heavy work on a background thread
//        //stopSelf();
//        return START_NOT_STICKY;
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
////    private void createNotificationChannel() {
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            NotificationChannel serviceChannel = new NotificationChannel(
////                    CHANNEL_ID,
////                    "Foreground Service Channel",
////                    NotificationManager.IMPORTANCE_HIGH
////            );
////            NotificationManager manager = getSystemService(NotificationManager.class);
////            manager.createNotificationChannel(serviceChannel);
////        }
////    }
//}