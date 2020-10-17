package com.example.studybuddies;

import java.util.HashMap;

public class Comment {
    //TODO create string variable for user ID
    String commentID;
    String subject;
    int rating;
    HashMap<String,Integer> commentUID = new HashMap<String, Integer>(); //if value at 0 or -1, allow upvote only. else if value at 0 or 1, allow downvote only

    public Comment() {
    }

    public Comment(String commentID, String subject, int rating) {
        this.commentID = commentID;
        this.subject = subject;
        this.rating = rating;
        commentUID.put("MItaHYgH6qwertyuiop", 0);
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

    public HashMap<String, Integer> getRaterUID(){
        return commentUID;
    }
}
