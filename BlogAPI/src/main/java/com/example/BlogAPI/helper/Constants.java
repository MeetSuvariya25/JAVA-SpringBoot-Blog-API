package com.example.BlogAPI.helper;

public class Constants {
    public static final String SECRET_KEY = "your_secret_key";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    // APIs
    public static final String ROOT_API = "/api/v1";
    public static final String LOGIN_API = ROOT_API + "/login";
    public static final String USER_API = ROOT_API + "/users";
    public static final String CHANGE_PASSWORD = USER_API + "/password";
    public static final String CATEGORY_API = ROOT_API + "/Category";
    public static final String CATEGORY_UPDATE_API = CATEGORY_API + "/{categoryId}";

    public static final String POST_API = ROOT_API + "/Posts";
    public static final String POST_BY_AUTHOR_API = POST_API + "/author/{userId}";
    public static final String POST_BY_CATEGORY_API = POST_API + "/{categoryId}";
    public static final String POST_DELETE_API = POST_API + "/{postId}";


    // Messages
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String INVALID_TOKEN = "Token Invalid";

}