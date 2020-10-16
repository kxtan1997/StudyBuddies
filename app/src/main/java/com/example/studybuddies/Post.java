package com.example.studybuddies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Post {
    //TODO add pictures to Post
    //TODO link User to Post
    String postID;
    String postTitle;
    String postDescription;
    String subject;
    HashMap<String, Comment> comments;
    int rating;
    HashMap<String,Integer> raterUID = new HashMap<String, Integer>(); //if value at 0 or -1, allow upvote only. else if value at 0 or 1, allow downvote only
    //User user;

    public Post() {
    }

    public Post(String postID, String postTitle, String postDescription, String subject, HashMap<String, Comment> comments, int rating) {
        this.postID = postID;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.subject = subject;
        this.comments = comments;
        this.rating = rating;
        raterUID.put("MItaHYgH6qwertyuiop", 0);
        //this.user = user;
    }

    public String getPostID() {
        return postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public String getSubject() {
        return subject;
    }

    public HashMap<String, Comment> getComments() {
        return comments;
    }

    public int getRating() {
        return rating;
    }

    public HashMap<String, Integer> getRaterUID(){
        return raterUID;
    }



    //public User getUser(){ return user; }
}
