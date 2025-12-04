package com.example.esmanager.UserStuff;

public final class CurrentUser {
    private static String userId;

    private static CurrentUser instance;

    private CurrentUser(){}

    public static CurrentUser getInstance(){
        if (instance == null){
            instance = new CurrentUser();
        }
        return instance;
    }

    public static void login(String id){
        userId = id;
        System.out.println("User" + id + "logged in");
    }

    public static void logout(){
        System.out.println("User " + userId + " logged out");
        userId = null;
    }

    public static String getUserId(){
        return userId;
    }
}
