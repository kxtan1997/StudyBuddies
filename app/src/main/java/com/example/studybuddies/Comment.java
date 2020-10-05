package com.example.studybuddies;

public class Comment {
    //TODO create string variable for user ID
    String commentID;
    String subject;
    int rating;

    public Comment() {
    }

    public Comment(String commentID, String subject, int rating) {
        this.commentID = commentID;
        this.subject = subject;
        this.rating = rating;
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
}
