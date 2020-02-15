package com.example.blog.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;

import com.example.blog.model.Posts;

import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private List<Posts> postsList;
    private LayoutInflater mInflater;

    private ItemClickListener mClickListener;
    private CommentClickListener mCommentClickListener;

    // data is passed into the constructor
//    MyRecyclerViewAdapter(Context context, List<String> data) {
//
//        this.mInflater = LayoutInflater.from(context);
//        this.mData = data;
//    }
    PostRecyclerViewAdapter(Context context, List<Posts> itemData) {

        this.mInflater = LayoutInflater.from(context);
        this.postsList = itemData;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.post_single_item, parent, false);

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

        Posts posts= postsList.get(position);
       // holder.itemId.setText(item.getId());
        holder.postTitle.setText(posts.getTitle());
        holder.postDetails.setText(posts.getContent());
//

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return postsList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView postTitle,postDetails,seeMore,postId,viewCount,comments;


       LinearLayout contentll;

        ViewHolder(View itemView) {
            super(itemView);

            postTitle=itemView.findViewById(R.id.postTitle);
            postDetails=itemView.findViewById(R.id.postDetails);
            postId=itemView.findViewById(R.id.psi_postId);
            seeMore=itemView.findViewById(R.id.seeMore);
            contentll=itemView.findViewById(R.id.contentLL);
            viewCount=itemView.findViewById(R.id.viewsCount);
            comments=itemView.findViewById(R.id.commentsCount);


            itemView.setOnClickListener(this);

            contentll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(postDetails.getMaxLines()==3) {
                        postDetails.setMaxLines(40);
                        seeMore.setVisibility(View.GONE);
                    }


                    else {
                        postDetails.setMaxLines(3);
                        seeMore.setVisibility(View.VISIBLE);

                    }
                }
            });

            comments.setOnClickListener(commnetListener);



        }


//public  void CmClick

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


        View.OnClickListener commnetListener = new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (mCommentClickListener != null)mCommentClickListener.onCommentClick(view, getAdapterPosition());

            }
        };
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




    void setCommentClickListener(CommentClickListener commentClickListener) {
        this.mCommentClickListener = commentClickListener;
    }
    public interface CommentClickListener {
        void onCommentClick(View view, int position);
    }

}