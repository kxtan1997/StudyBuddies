package com.example.studybuddies;

public class User {
    String userID;
    String username;
    int rating;
    String[] weakSubs;
    String[] strongSubs;

    public User() {
    }

    public User(String userID, String username, int rating, String[] weakSubs, String[] strongSubs) {
        this.userID = userID;
        this.username = username;
        this.rating = rating;
        this.weakSubs = weakSubs;
        this.strongSubs = strongSubs;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String[] getWeakSubs() {
        return weakSubs;
    }

    public void setWeakSubs(String[] weakSubs) {
        this.weakSubs = weakSubs;
    }

    public String[] getStrongSubs() {
        return strongSubs;
    }

    public void setStrongSubs(String[] strongSubs) {
        this.strongSubs = strongSubs;
    }
}
