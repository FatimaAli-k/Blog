package com.example.blog.controller.ui.allBlogs;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.blog.R;
import com.example.blog.controller.notification.NotificationUtils;

public class AllBlogsFragment extends Fragment {
    NotificationManager notificationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        allBlogsViewModel =
//                ViewModelProviders.of(this).get(AllBlogsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_all_blogs, container, false);
//        final TextView textView = root.findViewById(R.id.text_all_blogs);
//        allBlogsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


//        NotificationUtils notificationUtils=new NotificationUtils(getContext());
//        notificationUtils.createChannels();
//
//        final NotificationCompat.Builder notification1 = new NotificationCompat.Builder(getActivity(), NotificationUtils.CHANNEL1_ID)
//                .setSmallIcon(R.drawable.pic)
//                .setContentTitle("titlllle")
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setContentText("textxtxt")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setAutoCancel(true);
//
//        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
////        notificationManager.notify(1,notification1.build());
//        final NotificationCompat.Builder notification2 = new NotificationCompat.Builder(getContext(), NotificationUtils.CHANNEL2_ID)
//                .setSmallIcon(R.drawable.pic)
//                .setContentTitle("22222222222222")
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setContentText("ooooooooooo")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setAutoCancel(true);
//
//
//        Button ch2=root.findViewById(R.id.ch2Btn);
//        ch2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                notificationManager.notify((int)(System.currentTimeMillis()/1000),notification2.build());
//
////                notificationManager.notify(1,notification1.build());
//
//            }
//        });


//
        return root;
    }
}