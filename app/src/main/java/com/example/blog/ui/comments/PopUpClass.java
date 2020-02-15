//package com.example.blog.ui.comments;
//
//import android.graphics.drawable.Drawable;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//
//import com.example.blog.R;
//
//public class PopUpClass {
//
//
//
//    //PopupWindow display method
//
//    public void showPopupWindow(final View view, Drawable img) {
//
//
//        //Create a View object yourself through inflater
//        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
//        View popupView = inflater.inflate(R.layout.fragment_comments, null);
//
//        //Specify the length and width through constants
//        int width = LinearLayout.LayoutParams.MATCH_PARENT;
//        int height = LinearLayout.LayoutParams.MATCH_PARENT;
//
//        //Make Inactive Items Outside Of PopupWindow
//        boolean focusable = true;
//
//        //Create a window with our parameters
//        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//        //Set the location of the window on the screen
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//        //Initialize the elements of our window, install the handler
//
//
//        ImageView imgContainer= popupView.findViewById(R.id.expanded_image);
//
//
//        imgContainer.setImageDrawable(img);
//        Button close = popupView.findViewById(R.id.closePopup);
//       close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                popupWindow.dismiss();
//
//            }
//        });
//
//
//
//        //Handler for clicking on the inactive zone of the window
//
//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                //Close the window when clicked
//                popupWindow.dismiss();
//                return true;
//            }
//        });
//    }
//
//}