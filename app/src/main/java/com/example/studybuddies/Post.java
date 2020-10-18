package com.example.studybuddies;

import java.util.HashMap;

public class Post {
    String posterUsername;
    String postID;
    String postTitle;
    String postDescription;
    String subject;
    HashMap<String, Comment> comments;
    int rating;
    HashMap<String, Integer> raterUID; // value controls what upvote and downvote does

    public Post() {
    }

    public Post(String posterUsername, String postID, String postTitle, String postDescription, String subject, HashMap<String, Comment> comments, int rating, HashMap<String, Integer> raterUID) {
        this.posterUsername = posterUsername;
        this.postID = postID;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.subject = subject;
        this.comments = comments;
        this.rating = rating;
        this.raterUID = raterUID;
    }

    public String getPosterUsername() {
        return posterUsername;
    }

    public void setPosterUID(String posterUsername) {
        this.posterUsername = posterUsername;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HashMap<String, Comment> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, Comment> comments) {
        this.comments = comments;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public HashMap<String, Integer> getRaterUID() {
        return raterUID;
    }

    public void setRaterUID(HashMap<String, Integer> raterUID) {
        this.raterUID = raterUID;
    }
}
