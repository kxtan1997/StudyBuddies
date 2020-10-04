package com.example.studybuddies;

public class Post {
    //TODO create string variable for user ID
    //TODO comments array to be added
    //TODO add pictures to Post
    String postID;
    String postTitle;
    String postDescription;
    String subject;
    int ratings;

    public Post() {
    }

    public Post(String postID, String postTitle, String postDescription, String subject, int ratings) {
        this.postID = postID;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.subject = subject;
        this.ratings = ratings;
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

    public int getRatings() {
        return ratings;
    }
}
