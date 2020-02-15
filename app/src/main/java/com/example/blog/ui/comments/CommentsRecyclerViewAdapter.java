package com.example.blog.ui.comments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.Comments;

import java.util.List;

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private List<Comments> commentsList;
    private LayoutInflater mInflater;

    private ItemClickListener mClickListener;

    // data is passed into the constructor
//    MyRecyclerViewAdapter(Context context, List<String> data) {
//
//        this.mInflater = LayoutInflater.from(context);
//        this.mData = data;
//    }
    CommentsRecyclerViewAdapter(Context context, List<Comments> itemData) {

        this.mInflater = LayoutInflater.from(context);
        this.commentsList = itemData;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.comment_single_item, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        String id = mData.get(position);
//
//        holder.itemId.setText(id);
//    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Comments comments= commentsList.get(position);
       // holder.itemId.setText(item.getId());
        holder.comment.setText(comments.getContent());

//

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return commentsList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView comment,userId,postId;



        ViewHolder(View itemView) {
            super(itemView);

            comment=itemView.findViewById(R.id.comment);
            userId=itemView.findViewById(R.id.userId);
            postId=itemView.findViewById(R.id.postId);

            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
//    String getItem(int id) {
//        return mData.get(id);
//    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}