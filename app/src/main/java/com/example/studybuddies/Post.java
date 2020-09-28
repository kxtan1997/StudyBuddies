package com.example.studybuddies;

public class Post {
    String postID;
    String postTitle;
    String subject;
    String postDescription;

    public Post(){

    }

    public Post(String postID, String postTitle, String postDescription, String subject){
        this.postID = postID;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.subject = subject;

    }

    public String getPostID(){
        return postID;
    }

    public String getPostTitle(){
        return postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public String getSubject() {
        return subject;
    }
}
