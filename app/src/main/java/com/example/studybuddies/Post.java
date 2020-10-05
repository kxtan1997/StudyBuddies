package com.example.studybuddies;

import java.util.HashMap;

public class Post {
    //TODO create string variable for user ID
    //TODO comments array to be added
    //TODO add pictures to Post
    String postID;
    String postTitle;
    String postDescription;
    String subject;
    HashMap<String, Comment> comments;
    int rating;

    public Post() {
    }

    public Post(String postID, String postTitle, String postDescription, String subject, HashMap<String, Comment> comments, int rating) {
        this.postID = postID;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.subject = subject;
        this.comments = comments;
        this.rating = rating;
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
}
