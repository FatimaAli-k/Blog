package com.example.blog.tools;

public class URLs {
    private String baseUrl="http://192.168.9.108:8000/api/";
    private String route;
    private String categories="cat";
    private String sendPost="getpost";
    private String postsFeed ="posttpagination";
    private String commentsFeed="commentpagination";
    private String imagePath="https://alkafeelblog.edu.turathalanbiaa.com/aqlam/image/";

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



    public String getNextPageUrl(String route, int pageNumber)
    {
        return baseUrl+route+"?page="+pageNumber;
    }

    public String getImagePath(String pic)
    {
        return imagePath+pic;
    }
}
