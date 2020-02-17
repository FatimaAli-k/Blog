package com.example.blog.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;

import com.example.blog.model.Posts;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {


    private List<Posts> postsList;
    private LayoutInflater mInflater;

    private ItemClickListener mClickListener;
    private CommentClickListener mCommentClickListener;
    private PicClickListener mPicClickListener;

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    boolean type;

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
        switch (viewType){
            case VIEW_TYPE_NORMAL:
                type=true;
                return new ViewHolder( mInflater.inflate(R.layout.post_single_item, parent, false));

            case VIEW_TYPE_LOADING:
                type=false;
                return new ViewHolder( mInflater.inflate(R.layout.item_loading, parent, false));

            default:
                return null;

        }

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


        if(type) {
            holder.postTitle.setText(posts.getTitle());
            holder.postDetails.setText(posts.getContent());


            String img = posts.getImage();

            if (img != null) {

                holder.postPic.setVisibility(View.VISIBLE);
                Picasso.with(holder.postPic.getContext()).load(img).fit().centerCrop().into(holder.postPic);
            }
//
            type=false;
        }
        else{

            type=true;
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return postsList == null ? 0 : postsList.size();
    }

    public void addItems(List<Posts> postItems) {
        postsList.addAll(postItems);
        notifyDataSetChanged();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ProgressBar progressBar;
       TextView postTitle,postDetails,seeMore,postId,viewCount,comments;
       ImageView postPic;
       LinearLayout contentll,catLL;

        ViewHolder(View itemView) {
            super(itemView);
            if(type){

            postPic = itemView.findViewById(R.id.post_pic);
            postTitle = itemView.findViewById(R.id.postTitle);
            postDetails = itemView.findViewById(R.id.postDetails);
            postId = itemView.findViewById(R.id.psi_postId);
            seeMore = itemView.findViewById(R.id.seeMore);
            contentll = itemView.findViewById(R.id.contentLL);
            viewCount = itemView.findViewById(R.id.viewsCount);
            comments = itemView.findViewById(R.id.commentsCount);

            catLL=itemView.findViewById(R.id.post_catLL);


            if(postDetails.getLineCount()<3){
                seeMore.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(this);

            contentll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postDetails.getMaxLines() == 3) {
                        postDetails.setMaxLines(40);
                        seeMore.setVisibility(View.GONE);
                    } else {
                        postDetails.setMaxLines(3);
                        seeMore.setVisibility(View.VISIBLE);

                    }
                }
            });

            comments.setOnClickListener(commnetListener);
            postPic.setOnClickListener(picListener);

//
        }
            else{
                progressBar = itemView.findViewById(R.id.progressBar);
            }

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

        View.OnClickListener picListener = new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (mPicClickListener != null)mPicClickListener.onPicClick(view, getAdapterPosition());

            }
        };


    }
//    public class ProgressHolder extends PostRecyclerViewAdapter.ViewHolder {
//        ProgressHolder(View itemView) {
//            super(itemView);
//
//
//        }
//    }
    Posts getItem(int position) {
        return postsList.get(position);
    }

    public void addLoading() {
        isLoaderVisible = true;
        postsList.add(new Posts());
        notifyItemInserted(postsList.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = postsList.size() - 1;
       Posts item = getItem(position);
        if (item != null) {
            postsList.remove(position);
            notifyItemRemoved(position);
        }
    }



    // convenience method for getting data at click position
//    String getItem(int id) {
//        return mData.get(id);
//    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == postsList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }



    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }




    //on comment click
    void setCommentClickListener(CommentClickListener commentClickListener) {
        this.mCommentClickListener = commentClickListener;
    }
    public interface CommentClickListener {
        void onCommentClick(View view, int position);
    }



    //on pic click
    void setPicClickListener(PicClickListener picClickListener) {
        this.mPicClickListener = picClickListener;
    }
    public interface PicClickListener {
        void onPicClick(View view, int position);
    }



}