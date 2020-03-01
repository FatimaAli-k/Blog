package com.example.blog;

public class URLs {
    private String baseUrl="http://192.168.9.108:8000/api/";
    private String route;
    private String categories="cat";
    private String sendPost="addposts";
    private String sendComment="addcomment";
    private String postsFeed ="posttpagination";
    private String commentsFeed="commentpagination";
    private String profileInfo="profile";
    private String login="loginuser";
    private String register="registerusers";
    private String users="userpagination";
    private String postByUserId="PosttPaginationByUserId";
    private String deletePost="deletepost";
    private String deleteComment="";



    private String incViews="updateviews";


    private String imagePath="http://192.168.9.108/blog/public/images/";




    public URLs(){}

    public String getUrl(String route)
    {
        return baseUrl+route;
    }

    public String getCategoriesUrl()
    {
        return baseUrl+categories;
    }

    public String getPostsFeedUrl()
    {
        return baseUrl+ postsFeed;
    }

    public String getCommentsFeedUrl()
    {
        return baseUrl+commentsFeed;
    }

    public String getSendPostUrl()
    {
        return baseUrl+sendPost;
    }

    public String getIncViewsUrl()
    {
        return baseUrl+incViews;
    }



    public String getNextPageUrl(String route, int pageNumber)
    {
        return baseUrl+route+"?page="+pageNumber;
    }

    public String getImagePath(String pic)
    {
        return imagePath+pic;
    }

    public String getCategories() {
        return categories;
    }

    public String getSendPost() {
        return sendPost;
    }

    public String getPostsFeed() {
        return postsFeed;
    }

    public String getCommentsFeed() {
        return commentsFeed;
    }

    public String getIncViews() {
        return incViews;
    }

    public String getLogin() {
        return login;
    }

    public String getProfileInfo() {
        return profileInfo;
    }

    public String getRegister() {
        return register;
    }

    public String getSendComment() {
        return sendComment;
    }
    public String getPostByUserId() {
        return postByUserId;
    }

    public String getUsers() {
        return users;
    }
    public String getDeletePost() {
        return deletePost;
    }

    public String getDeleteComment() {
        return deleteComment;
    }

    public String getImagePath() {
        return imagePath;
    }
}
