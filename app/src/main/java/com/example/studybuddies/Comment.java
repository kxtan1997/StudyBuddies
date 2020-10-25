package com.example.studybuddies;

import java.util.HashMap;

public class Comment {
    String userID;
    String username;
    String commentID;
    String subject;
    int rating;
    HashMap<String, Integer> commentUID; //if value at 0 or -1, allow upvote only. else if value at 0 or 1, allow downvote only

    public Comment() {
    }

    public Comment(String userID, String username, String commentID, String subject, int rating, HashMap<String, Integer> commentUID) {
        this.userID = userID;
        this.username = username;
        this.commentID = commentID;
        this.subject = subject;
        this.rating = rating;
        this.commentUID = commentUID;
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

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public HashMap<String, Integer> getCommentUID() {
        return commentUID;
    }

    public void setCommentUID(HashMap<String, Integer> commentUID) {
        this.commentUID = commentUID;
    }
}
