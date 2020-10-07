package com.example.studybuddies;

public class User {
    String userID;
    String username;
    int rating;
    String[] weakSubs;
    String[] strongSubs;

    public User(){
    }

    public User(String userID, String username, int rating, String[] weakSubs, String[] strongSubs){
        this.userID = userID;
        this.username = username;
        this.rating = rating;
        this.weakSubs = weakSubs;
        this.strongSubs = strongSubs;
    }

    public String getUserID(){
        return userID;
    }

    public String getUsername(){
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String[] getWeakSubs() {
        return weakSubs;
    }

    public String[] getStrongSubs() {
        return strongSubs;
    }
}
