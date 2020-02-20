package com.example.blog;

public class URLs {
    private String baseUrl="http://192.168.9.108:8000/api/";
    private String route;

    public URLs(){}

    public String getUrl(String route)
    {
        return baseUrl+route;
    }

    public String getNextPageUrl(String route, int pageNumber)
    {
        return baseUrl+route+"?page="+pageNumber;
    }
}
